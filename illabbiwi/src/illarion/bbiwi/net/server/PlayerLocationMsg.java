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
import illarion.common.types.Location;
import org.bushe.swing.event.EventServiceLocator;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server to update the location of a character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x05)
public final class PlayerLocationMsg extends AbstractReply {
    /**
     * The ID of the character that is updated.
     */
    private CharacterId charId;

    /**
     * The current location of the character.
     */
    private Location location;

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        charId = new CharacterId(reader);
        location = new Location(reader);
    }

    @Override
    public boolean executeUpdate() {
        // TODO: Forward received data to BBIWI
        EventServiceLocator.getSwingEventService().publish(new GenericComEvent());
        return true;
    }

    @Override
    public String toString() {
        return toString(charId.toString() + ' ' + location);
    }
}
