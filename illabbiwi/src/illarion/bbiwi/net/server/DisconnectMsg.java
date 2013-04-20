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

import illarion.bbiwi.BBIWI;
import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server to inform the client that it got disconnected.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0xCC)
public final class DisconnectMsg extends AbstractReply {
    /**
     * The list of the reasons for the logout from the server. This are the keys for the translation.
     */
    @SuppressWarnings("nls")
    private static final String[] REASONS = {null, "old_client",
            "already_logged_in", "wrong_pw", "server_shutdown", "kicked", null,
            "no_place", "not_found", null, "unstable", "no_account",
            "no_skillpack", "corruput_inventory"};

    /**
     * The ID of the logout reason.
     */
    private short reason;

    /**
     * Get the data of this disconnect message as string.
     *
     * @return the string that contains the values that were decoded for this
     *         message
     */
    @Nonnull
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return toString("Reason: " + Integer.toHexString(reason));
    }

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        reason = reader.readUByte();
    }

    /**
     * Execute the disconnect message and send the decoded data to the rest of
     * the client.
     *
     * @return true if the execution is done, false if it shall be called again
     */
    @SuppressWarnings("nls")
    @Override
    public boolean executeUpdate() {
        BBIWI.getConnectionMonitor().reportDisconnect(reason);
        return true;
    }
}
