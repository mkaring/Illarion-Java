/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.common.net;

import illarion.common.config.Config;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.BlockingQueue;

/**
 * The Receiver class handles all data that is send from the server, decodes the messages and prepares them for
 * execution.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Nop
 */
@NotThreadSafe
final class Receiver extends Thread implements NetCommReader {
    /**
     * Length of the byte buffer used to store the data from the server.
     */
    private static final int BUFFER_LENGTH = 10000;

    /**
     * The XOR mask the command ID is masked with to decode the checking ID and ensure that the start of a command
     * was found.
     */
    private static final int COMMAND_XOR_MASK = 0xFF;

    /**
     * The instance of the logger that is used to write out the data.
     */
    @Nonnull
    private static final Logger LOGGER = Logger.getLogger(Receiver.class);

    /**
     * Time the receiver waits for more data before throwing away the incomplete things it already got.
     */
    private static final int RECEIVER_TIMEOUT = 1000;

    /**
     * The default size of the header of each command and each message in byte.
     */
    private static final int HEADER_SIZE = 6;

    /**
     * The buffer that stores the byte that we received from the server for decoding.
     */
    @Nonnull
    private final ByteBuffer buffer;

    /**
     * The decoder that is used to decode the strings that are send to the client by the server.
     */
    @Nonnull
    private final CharsetDecoder decoder;

    /**
     * The buffer that is used to temporary store the decoded characters that were send to the player.
     */
    @Nonnull
    private final CharBuffer decodingBuffer = CharBuffer.allocate(65535);

    /**
     * The input stream of the connection socket of the connection to the server.
     */
    @Nonnull
    private final ReadableByteChannel inChannel;

    /**
     * The list that stores the commands there were decoded and prepared for the NetComm for execution.
     */
    @Nonnull
    private final BlockingQueue<ServerReply> queue;

    /**
     * Indicator if the Receiver is currently running.
     */
    private boolean running;

    /**
     * The time until a timeout occurs.
     */
    private long timeOut;

    /**
     * The factory that delivers the reply objects.
     */
    @Nonnull
    private final ServerReplyFactory replyFactory;

    /**
     * This flag stores if the network is currently debugged.
     */
    private boolean networkDebug;

    /**
     * The basic constructor for the receiver that sets up all needed data.
     *
     * @param inputQueue the list of decoded server messages that need to be executed by NetComm
     * @param in         the input stream of the socket connection to the server that contains the data that needs to
     *                   be decoded
     */
    @SuppressWarnings("nls")
    Receiver(@Nonnull final Config cfg, @Nonnull final ServerReplyFactory replyFactory,
             @Nonnull final BlockingQueue<ServerReply> inputQueue, @Nonnull final ReadableByteChannel in) {
        super("Illarion input thread");

        this.replyFactory = replyFactory;

        networkDebug = cfg.getBoolean(NetComm.CFG_DEBUG_NETWORK_KEY);

        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        queue = inputQueue;
        inChannel = in;

        buffer = ByteBuffer.allocateDirect(BUFFER_LENGTH);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.limit(0);

        decoder = NetComm.SERVER_STRING_ENCODING.newDecoder();

        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
    }

    /**
     * Read a string from the input buffer and encode it for further usage.
     *
     * @return the decoded string
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Nonnull
    @Override
    @SuppressWarnings("nls")
    public String readString() throws IOException {
        final int len = readUShort();

        if (len == 0) {
            return "";
        }

        if (len > buffer.remaining()) {
            throw new IndexOutOfBoundsException("reading beyond receive buffer " + (buffer.remaining() + len));
        }
        decodingBuffer.clear();
        final int lastLimit = buffer.limit();
        buffer.limit(buffer.position() + len);
        decoder.decode(buffer, decodingBuffer, false);
        buffer.limit(lastLimit);
        decodingBuffer.flip();

        return decodingBuffer.toString();
    }

    /**
     * Read two bytes from the buffer and handle them as a single unsigned value.
     *
     * @return The two bytes in the buffer handled as unsigned 2 byte value
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public int readUShort() throws IOException {
        final int data = readShort();
        if (data < 0) {
            return data + (1 << Short.SIZE);
        }
        return data;
    }

    /**
     * Read two bytes from the buffer and handle them as a single signed value.
     *
     * @return The two bytes in the buffer handled as signed 2 byte value
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public short readShort() throws IOException {
        return buffer.getShort();
    }

    /**
     * Read four bytes from the buffer and handle them as a single unsigned value.
     *
     * @return The two bytes in the buffer handled as unsigned 4 byte value
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public long readUInt() throws IOException {
        final long data = readInt();
        if (data < 0) {
            return data + (1L << Integer.SIZE);
        }
        return data;
    }

    /**
     * Read four bytes from the buffer and handle them as a single signed value.
     *
     * @return The two bytes in the buffer handled as signed 4 byte value
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public int readInt() throws IOException {
        return buffer.getInt();
    }

    /**
     * The main loop the the receiver thread. Decodes the data of the input stream and places the server messages in
     * the queue.
     * <p>
     * The decoding of the data happens as instantly as soon as a command is completely read from the input stream.
     * Searching the start of a command is done by looking for a valid ID with a valid XOR id right behind.
     * </p>
     */
    @SuppressWarnings("nls")
    @Override
    public void run() {
        running = true;
        int minRequiredData = HEADER_SIZE;

        while (running) {
            try {
                while (running && receiveData(minRequiredData)) {
                    while (buffer.remaining() >= HEADER_SIZE) {
                        // wait for a complete message header

                        // identify command
                        final int id = readUByte();
                        final int xor = readUByte();

                        // valid command id
                        if (id != (xor ^ COMMAND_XOR_MASK)) {
                            // delete only first byte from buffer, scanning for valid command
                            buffer.position(1);
                            buffer.compact();

                            LOGGER.warn("Skipping invalid data [" + id + ']');

                            continue;
                        }

                        // read length and CRC
                        final int len = readUShort();
                        final int crc = readUShort();

                        // wait for complete data
                        if (!isDataComplete(len)) {
                            // scroll the cursor back and wait for more.
                            buffer.position(0);
                            minRequiredData = len + HEADER_SIZE;
                            break;
                        }

                        minRequiredData = HEADER_SIZE;

                        // check CRC
                        if (crc != NetComm.getCRC(buffer, len)) {
                            final int oldLimit = buffer.limit();
                            buffer.limit(len + HEADER_SIZE);
                            buffer.position(HEADER_SIZE);
                            NetComm.dump("Invalid CRC ", buffer);

                            buffer.position(1);
                            buffer.limit(oldLimit);
                            buffer.compact();
                            buffer.flip();
                            continue;
                        }

                        // decode
                        try {
                            final ServerReply rpl = replyFactory.getReply(id);
                            if (rpl != null) {
                                rpl.decode(this);

                                if (networkDebug) {
                                    LOGGER.debug("REC: " + rpl.toString());
                                }

                                // put decoded command in input queue
                                queue.put(rpl);
                            } else {
                                // throw away the command that was incorrectly decoded
                                buffer.position(len + HEADER_SIZE);
                            }
                        } catch (@Nonnull final IllegalArgumentException ex) {
                            LOGGER.error("Invalid command id received " + Integer.toHexString(id));
                        }

                        buffer.compact();
                        buffer.flip();
                    }
                }
            } catch (@Nonnull final IOException e) {
                if (running) {
                    LOGGER.fatal("The connection to the server is not working anymore.", e);
                    running = false;
                    return;
                }
            } catch (@Nonnull final Exception e) {
                if (running) {
                    LOGGER.fatal("General error in the receiver", e);
                    running = false;
                    return;
                }
            }
        }
    }

    /**
     * Read data from the input stream of the socket and store it in the buffer.
     *
     * @param neededDataInBuffer The data that is needed at least before the method has to return in order to parse
     *                           the values correctly
     * @return {@code true} in case there is any data to be decoded in the buffer
     * @throws IOException In case there is something wrong with the input stream
     */
    @SuppressWarnings({"nls", "BooleanMethodNameMustStartWithQuestion"})
    private boolean receiveData(final int neededDataInBuffer) throws IOException {
        int data = buffer.remaining();

        final int appPos = buffer.limit();
        buffer.clear();
        buffer.position(appPos);

        int newData = 0;
        while (true) {
            if (inChannel.isOpen()) {
                newData = inChannel.read(buffer);
            }
            data += newData;
            if (data >= neededDataInBuffer) {
                break;
            }
            try {
                Thread.sleep(2);
            } catch (@Nonnull final InterruptedException e) {
                LOGGER.warn("Interrupted wait time for new data");
            }
        }

        buffer.flip();

        if ((newData > 0) && networkDebug) {
            buffer.position(appPos);
            NetComm.dump("rcv <= ", buffer);
            buffer.position(0);
        }

        return buffer.hasRemaining();
    }

    /**
     * Read a single byte from the buffer and handle it as unsigned byte.
     *
     * @return The byte of the buffer handled as unsigned byte.
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public short readUByte() throws IOException {
        final short data = readByte();
        if (data < 0) {
            return (short) (data + (1 << Byte.SIZE));
        }
        return data;
    }

    /**
     * Read a single byte from the buffer and handle it as signed byte.
     *
     * @return The byte from the buffer handled as signed byte
     * @throws IOException If there are more byte read then there are written in the buffer
     */
    @Override
    public byte readByte() throws IOException {
        return buffer.get();
    }

    /**
     * This function checks of the received data contains a complete command.
     *
     * @param len the amount of bytes that were received for that command
     * @return {@code true} in case the command is complete, {@code false} if not
     */
    @SuppressWarnings("nls")
    private boolean isDataComplete(final int len) {
        if (len <= buffer.remaining()) {
            timeOut = 0;
            return true;
        }

        // set timeout for data
        if (timeOut == 0) {
            timeOut = System.currentTimeMillis() + RECEIVER_TIMEOUT;
        }

        // timeout exceeded
        if (System.currentTimeMillis() > timeOut) {
            NetComm.dump("Receiver timeout. Skipping ", buffer);
            buffer.clear();
            buffer.limit(0);
        } else { // still waiting
            buffer.position(0);
        }

        return false;
    }

    /**
     * Shutdown the receiver.
     */
    public void saveShutdown() {
        LOGGER.info(getName() + ": Shutdown requested!");
        running = false;
        interrupt();
    }
}
