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
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This command is used to request a update of the attribute values for one character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class RequestAttributesCmd extends AbstractCommand {
    /**
     * The character id of the character that requires the attribute update.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The name of the character that requires the skill update.
     * <p/>
     * TODO: Find out what one needs to smoke to come up with the idea to identify the character by ID and name.
     */
    @Nonnull
    private final String charName;

    /**
     * Create a new instance of this update attributes command.
     *
     * @param charId   the ID of the character to update
     * @param charName the name of the character to update
     */
    public RequestAttributesCmd(@Nonnull final CharacterId charId, @Nonnull final String charName) {
        super(0x06);
        this.charId = charId;
        this.charName = charName;
    }

    @Nonnull
    @Override
    public String toString() {
        return toString(charId + " Name: " + charName);
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        charId.encode(writer);
        writer.writeString(charName);
    }
}
