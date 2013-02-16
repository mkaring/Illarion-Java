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
import java.io.IOException;

/**
 * This interface defines a reply that is send by the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface ServerReply {
    /**
     * Execute the update.
     *
     * @return {@code true} in case the update was properly executed, else the execution will be repeated
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean executeUpdate();

    /**
     * Check if its okay to execute the update now.
     *
     * @return {@code true} in case the reply should be executed now
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean processNow();

    /**
     * Decode the reply from the incoming buffer.
     *
     * @param receiver the receiver that received the message from the server
     * @throws IOException in case there is a error while reading the reply
     */
    void decode(@Nonnull NetCommReader receiver) throws IOException;
}
