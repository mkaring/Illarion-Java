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

import illarion.bbiwi.world.Player;
import illarion.common.types.Location;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This event is fired in case the location of a character changed.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public class PlayerLocationChangedEvent {
    /**
     * The index of the player who got updated.
     */
    private final int index;

    /**
     * The player who received a location update.
     */
    @Nonnull
    private final Player player;

    /**
     * The old location of the player.
     */
    @Nonnull
    private final Location oldLocation;

    /**
     * The new location of the player.
     */
    @Nonnull
    private final Location newLocation;

    /**
     * Create a new event.
     *
     * @param index       the index of the player
     * @param player      the player that got a new location
     * @param oldLocation the old location of the player
     * @param newLocation the new location of the player
     */
    public PlayerLocationChangedEvent(final int index, @Nonnull final Player player,
                                      @Nonnull final Location oldLocation, @Nonnull final Location newLocation) {
        this.index = index;
        this.player = player;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public int getIndex() {
        return index;
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Location getOldLocation() {
        return oldLocation;
    }

    @Nonnull
    public Location getNewLocation() {
        return newLocation;
    }
}
