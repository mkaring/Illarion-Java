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
 * This command is used to send a control command to the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class ServerCmd extends AbstractCommand {
    /**
     * This is the command to import the maps on the server.
     */
    @Nonnull
    public static final String CMD_IMPORT_MAPS = "importmaps";

    /**
     * This is the command to kick all players from the server.
     */
    @Nonnull
    public static final String CMD_KICK_ALL = "kickall";

    /**
     * This is the command to set the login flag to false. Players are not allowed to login once this is executed.
     */
    @Nonnull
    public static final String CMD_LOGIN_FALSE = "setloginfalse";

    /**
     * This is the command to set the login flag to true. Players are allowed to login once this flag is set.
     */
    @Nonnull
    public static final String CMD_LOGIN_TRUE = "setlogintrue";

    /**
     * This is the command to kill all monsters on the server.
     */
    @Nonnull
    public static final String CMD_NUKE = "nuke";

    /**
     * This is the command to reload the scripts and the database on the server.
     */
    @Nonnull
    public static final String CMD_RELOAD = "reload";
    /**
     * The command that is send to the server.
     */
    @Nonnull
    private final String command;

    /**
     * Create a new server command and set the command that is supposed to be send.
     *
     * @param command the command to send to the server
     */
    public ServerCmd(@Nonnull final String command) {
        super(0x08);
        this.command = command;
    }

    @Nonnull
    @Override
    public String toString() {
        return toString('"' + command + '"');
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        writer.writeString(command);
    }
}
