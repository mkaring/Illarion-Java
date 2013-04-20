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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This event is fired in case a new player appears on the online list.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public class NewPlayerOnListEvent {
    /**
     * The index where the player is added to the list.
     */
    private final int index;

    /**
     * The new player itself.
     */
    @Nonnull
    private final Player player;

    /**
     * Create a new event.
     *
     * @param index  the index of the player on the list
     * @param player the new player
     */
    public NewPlayerOnListEvent(final int index, @Nonnull final Player player) {
        this.index = index;
        this.player = player;
    }

    /**
     * Get the index of the new player on the list of online players.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the player that appears on the list.
     *
     * @return the new player
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }
}
