/*
 * This file is part of the Illarion Common Library.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion Common Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Common Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Common Library.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.common.net;

import illarion.common.config.Config;
import illarion.common.util.Stoppable;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * This class will take care that the messages received from the server are executes properly.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class MessageExecutor extends Thread implements Stoppable {
    /**
     * The logger instance that takes care for the logging output of this class.
     */
    @Nonnull
    private static final Logger LOGGER = Logger.getLogger(MessageExecutor.class);

    /**
     * This queue contains all tasks that were executed already once and need to be executed a second time.
     */
    @Nonnull
    private final Queue<ServerReply> delayedQueue;

    /**
     * The queue that contains all the tasks that were received from the server and still need to be executed.
     */
    @Nonnull
    private final BlockingQueue<ServerReply> input;

    /**
     * This reply is to be repeated at the next run.
     */
    @Nullable
    private ServerReply repeatReply;

    /**
     * The running flag. The loop of this thread will keep running until this flag is set to {@code false}.
     */
    private volatile boolean running;

    /**
     * The configuration supplier that is used by the message executor.
     */
    @Nonnull
    private final Config config;

    /**
     * This value is set {@code true} in case the network is supposed to be debugged.
     */
    private boolean networkDebug;

    /**
     * Default constructor for a message executor.
     *
     * @param inputQueue the input queue of messages that need to be handled
     */
    @SuppressWarnings("nls")
    MessageExecutor(@Nonnull final Config cfg, @Nonnull final BlockingQueue<ServerReply> inputQueue) {
        super("NetComm MessageExecutor");
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        input = inputQueue;
        config = cfg;
        delayedQueue = new LinkedList<ServerReply>();
    }

    @Override
    public synchronized void start() {
        networkDebug = config.getBoolean(NetComm.CFG_DEBUG_NETWORK_KEY);
        running = true;
        super.start();
    }

    /**
     * Have the thread finishing the current message and shut the thread down after.
     */
    @Override
    public void saveShutdown() {
        LOGGER.info(getName() + ": Shutdown requested!");
        running = false;
        interrupt();
    }

    /**
     * Main loop of the Message Executor. The messages are handled as soon as they appear in the queue.
     */
    @SuppressWarnings("nls")
    @Override
    public void run() {
        while (running) {
            /*
             * First we handle the delayed stuff in case there is any and it
             * does not block from executing.
             */
            if (!delayedQueue.isEmpty() && delayedQueue.peek().processNow()) {
                final ServerReply rpl = delayedQueue.poll();
                rpl.executeUpdate();
                continue;
            }

            final ServerReply rpl;

            if (repeatReply == null) {
                try {
                    rpl = input.take();
                } catch (@Nonnull final InterruptedException e) {
                    // Got and interrupt, quit the thread right now.
                    LOGGER.warn("MessageExecutor got interrupted and will exit now!");
                    return;
                }
            } else {
                rpl = repeatReply;
            }

            /*
             * Process the updates or put them into the delayed queue.
             */
            if (rpl.processNow()) {
                if (networkDebug) {
                    LOGGER.debug("executing " + rpl.toString());
                }

                if (rpl.executeUpdate()) {
                    if (networkDebug) {
                        LOGGER.debug("finished " + rpl.toString());
                    }
                } else {
                    if (networkDebug) {
                        LOGGER.debug("repeating " + rpl.toString());
                    }

                    repeatReply = rpl;
                }
            } else {
                delayedQueue.offer(rpl);
            }
        }
    }
}