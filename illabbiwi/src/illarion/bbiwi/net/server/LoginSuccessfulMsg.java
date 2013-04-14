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

import illarion.bbiwi.events.LoginSuccessfulEvent;
import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;
import org.bushe.swing.event.EventServiceLocator;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server once the login was successfully done.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x00)
public final class LoginSuccessfulMsg extends AbstractReply {
    @Nonnull
    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        // nothing to do
    }

    @Override
    public boolean executeUpdate() {
        EventServiceLocator.getSwingEventService().publish(new LoginSuccessfulEvent());
        return true;
    }
}
