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
 * This command is used to create a connection to the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class LoginCmd extends AbstractCommand {
    /**
     * The username that is supposed to be used to login.
     */
    @Nonnull
    private final String username;

    /**
     * The password that is used to login.
     */
    @Nonnull
    private final String password;

    /**
     * The client version that is send to the server to identify the client type.
     */
    private final int version;

    /**
     * Create a new login command.
     *
     * @param username the name of the character used to login
     * @param password the password used to login
     * @param version  the version send to the server
     */
    public LoginCmd(@Nonnull final String username, @Nonnull final String password, final int version) {
        super(0x0D);
        this.username = username;
        this.password = password;
        this.version = version;
    }

    @Nonnull
    @Override
    public String toString() {
        return toString("Name: " + username + " Version: " + version);
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        writer.writeUByte((short) version); // client version
        writer.writeString(username);
        writer.writeString(password);
    }
}
