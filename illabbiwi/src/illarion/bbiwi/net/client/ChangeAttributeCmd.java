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

import illarion.common.data.CharacterAttribute;
import illarion.common.net.NetCommWriter;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This command is used to change the attribute of a single character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class ChangeAttributeCmd extends AbstractCommand {
    /**
     * The character id of the character that is supposed to be banned.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The name of the character that is supposed to be banned.
     * <p/>
     * TODO: Find out what one needs to smoke to come up with the idea to identify the character by ID and name.
     */
    @Nonnull
    private final String charName;

    /**
     * The attribute that is supposed to be changed.
     */
    @Nonnull
    private final CharacterAttribute attribute;

    /**
     * The new value of the attribute.
     */
    private final int value;

    /**
     * Create a new attribute command.
     *
     * @param charId    the ID of the character that is supposed to be changed
     * @param charName  the name of the character that is supposed to be changed
     * @param attribute the attribute that is supposed to be changed
     * @param value     the new value of the attribute
     */
    public ChangeAttributeCmd(@Nonnull final CharacterId charId, @Nonnull final String charName,
                              @Nonnull final CharacterAttribute attribute, final int value) {
        super(0x06);
        this.charId = charId;
        this.charName = charName;
        this.attribute = attribute;
        this.value = value;
    }

    @Nonnull
    @Override
    public String toString() {
        return toString(charId + " Name: " + charName + " Attribute: " + attribute.name() + " Value: " + value);
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        charId.encode(writer);
        writer.writeString(charName);
        writer.writeString(attribute.getServerName());
        writer.writeUShort(value);
    }
}
