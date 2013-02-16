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
import illarion.common.types.Location;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This command is used to warp a player to a new location.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class WarpCmd extends AbstractCommand {
    /**
     * The character id of the character that is supposed to be warped.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The name of the character that is supposed to be warped.
     * <p/>
     * TODO: Find out what one needs to smoke to come up with the idea to identify the character by ID and name.
     */
    @Nonnull
    private final String charName;

    /**
     * The location the character is supposed to be warped to.
     */
    @Nonnull
    private final Location targetLocation;

    /**
     * Create a new instance of this warp command.
     *
     * @param charId   the ID of the character to receive the message
     * @param charName the name of the character to receive the message
     * @param location the location the character is warped to
     */
    public WarpCmd(@Nonnull final CharacterId charId, @Nonnull final String charName,
                   @Nonnull final Location location) {
        super(0x09);
        this.charId = charId;
        this.charName = charName;
        targetLocation = new Location(location);
    }

    @Nonnull
    @Override
    public String toString() {
        return toString(charId + " Name: " + charName + " Target" + targetLocation);
    }

    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        charId.encode(writer);
        writer.writeString(charName);
        writer.writeLocation(targetLocation);
    }
}
