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
package illarion.bbiwi.net.client;

import illarion.common.net.NetCommWriter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This command is used to send a broadcast to all players in the game.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class BroadcastMsg extends AbstractCommand {
    /**
     * The message that is broadcast.
     */
    @Nonnull
    private final String message;

    /**
     * Standard constructor for the broadcast message.
     *
     * @param message the message that is broadcast
     */
    public BroadcastMsg(@Nonnull final String message) {
        super(0x02);
        this.message = message;
    }

    @Nonnull
    @Override
    public String toString() {
        return toString('"' + message + '"');
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        writer.writeString(message);
    }
}
