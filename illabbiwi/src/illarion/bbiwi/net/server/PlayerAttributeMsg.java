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

import illarion.common.net.NetCommReader;
import illarion.common.net.ReplyMessage;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * This message is send by the server to update a attribute of a single character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@ReplyMessage(replyId = 0x06)
public final class PlayerAttributeMsg extends AbstractReply {
    /**
     * The ID of the character that receives this update.
     */
    private CharacterId charId;

    /**
     * The name of the attribute that is updated.
     */
    private String attribute;

    /**
     * The new value of the attribute.
     */
    private int value;

    @Override
    public void decode(@Nonnull final NetCommReader reader) throws IOException {
        charId = new CharacterId(reader);
        attribute = reader.readString();
        value = reader.readUShort();
    }

    @Override
    public boolean executeUpdate() {
        // TODO: Send Update to BBIWI
        return true;
    }

    @Override
    public String toString() {
        return toString(charId + " Attribute: " + attribute + " New Value: " + value);
    }
}
