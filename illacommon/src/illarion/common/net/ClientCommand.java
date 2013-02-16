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

import javax.annotation.Nonnull;

/**
 * This interface defines a command that is send by the client to the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface ClientCommand {
    /**
     * Encode the command to the network writer.
     *
     * @param sender the sender that is transferring the data
     */
    void encode(@Nonnull NetCommWriter sender);

    /**
     * Get the ID of this command.
     *
     * @return the command id
     */
    int getId();
}
