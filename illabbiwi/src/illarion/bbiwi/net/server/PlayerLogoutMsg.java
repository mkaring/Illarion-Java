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

import illarion.bbiwi.events.PlayerLogoutEvent;
import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;
import illarion.common.types.CharacterId;
import org.bushe.swing.event.EventServiceLocator;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server to inform the tool that a character logged out.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x04)
public final class PlayerLogoutMsg extends AbstractReply {
    /**
     * The ID of the character who logged out.
     */
    private CharacterId charId;

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        charId = new CharacterId(reader);
    }

    @Override
    public boolean executeUpdate() {
        // TODO: Forward this message to the GUI
        EventServiceLocator.getSwingEventService().publish(new PlayerLogoutEvent(charId));
        return true;
    }

    @Override
    public String toString() {
        return toString(charId.toString());
    }
}
