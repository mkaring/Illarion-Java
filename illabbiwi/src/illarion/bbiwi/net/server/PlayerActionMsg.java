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

import illarion.bbiwi.events.GenericComEvent;
import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;
import illarion.common.types.CharacterId;
import org.bushe.swing.event.EventServiceLocator;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server in case a character performs some special action.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x08)
public final class PlayerActionMsg extends AbstractReply {
    /**
     * The ID of the character that performs the action.
     */
    private CharacterId charId;

    /**
     * The name of this character.
     */
    private String name;

    /**
     * The text related to the action.
     */
    private String actionMessage;

    /**
     * The type of the performed action
     */
    private int actionType;

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        charId = new CharacterId(reader);
        name = reader.readString();
        actionType = reader.readUByte();
        actionMessage = reader.readString();
    }

    @Override
    public boolean executeUpdate() {
        // TODO: Forward received data to BBIWI
        EventServiceLocator.getSwingEventService().publish(new GenericComEvent());
        return true;
    }

    @Override
    public String toString() {
        return toString(charId + " Name: " + name + " Message: " + actionMessage);
    }
}
