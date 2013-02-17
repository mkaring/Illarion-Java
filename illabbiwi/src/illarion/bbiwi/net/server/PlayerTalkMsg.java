/*
 * This file is part of the Illarion BBIWI.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion BBIWI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion BBIWI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion BBIWI.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.bbiwi.net.server;

import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server in case a character is talking.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x03)
public final class PlayerTalkMsg extends AbstractReply {
    /**
     * This constant for the {@link #messageType} means that the text is spoken.
     */
    private static final int MSG_TYPE_SAY = 0;

    /**
     * This constant for the {@link #messageType} means that the text is whispered.
     */
    private static final int MSG_TYPE_WHISPER = 1;

    /**
     * This constant for the {@link #messageType} means that the text is shouted.
     */
    private static final int MSG_TYPE_SHOUT = 2;
    /**
     * The ID of the character that is updated.
     */
    private CharacterId charId;

    /**
     * The name of this character.
     */
    private String name;

    /**
     * The current location of the character.
     */
    private String message;

    /**
     * The kind of message that is spoken.
     */
    private int messageType;

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        charId = new CharacterId(reader);
        name = reader.readString();
        message = reader.readString();
        messageType = reader.readUByte();
    }

    @Override
    public boolean executeUpdate() {
        // TODO: Forward received data to BBIWI
        return true;
    }

    @Override
    public String toString() {
        return toString(charId + " Name: " + name + " Message: " + message);
    }
}
