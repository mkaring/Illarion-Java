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

import javax.annotation.Nullable;

/**
 * This interface defines the factory that supplies the receiver with the reply objects.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface ServerReplyFactory {
    /**
     * Get a reply.
     *
     * @param id the ID of the reply
     * @return the reply object or {@code null} in case this reply is undefined
     */
    @Nullable
    ServerReply getReply(int id);
}
