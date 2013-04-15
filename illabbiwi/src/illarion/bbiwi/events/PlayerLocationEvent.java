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
package illarion.bbiwi.events;

import illarion.common.types.CharacterId;
import illarion.common.types.Location;

import javax.annotation.Nonnull;

/**
 * This is the event that is published on the event bus in case a update of the player location is received.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerLocationEvent implements PlayerEvent {
    /**
     * The ID of the character effected by this event.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The new location of the player.
     */
    @Nonnull
    private final Location location;

    /**
     * Create a new player location update event.
     *
     * @param charId   the ID of the effected character
     * @param location the new location of the player
     */
    public PlayerLocationEvent(@Nonnull final CharacterId charId, @Nonnull final Location location) {
        this.charId = charId;
        this.location = location;
    }

    @Nonnull
    @Override
    public CharacterId getCharId() {
        return charId;
    }

    /**
     * Get the new player location.
     *
     * @return the new location
     */
    @Nonnull
    public Location getLocation() {
        return location;
    }
}
