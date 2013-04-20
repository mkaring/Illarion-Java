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
import illarion.common.data.CharacterAttribute;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * This event is fired in case the attribute of a player is changed.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public class PlayerAttributeChangedEvent {
    /**
     * The index of the player who got updated.
     */
    private final int index;

    /**
     * The player who received a attribute update.
     */
    @Nonnull
    private final Player player;

    /**
     * The attribute that got changed.
     */
    @Nonnull
    private final CharacterAttribute attribute;

    /**
     * The old value of the attribute.
     */
    private final int oldValue;

    /**
     * The new value of the attribute.
     */
    private final int newValue;

    /**
     * Create a new event.
     *
     * @param index     the index of the player that got updated
     * @param player    the player that got updated
     * @param attribute the attribute that got changed
     * @param oldValue  the old value of the attribute
     * @param newValue  the new value of the attribute
     */
    public PlayerAttributeChangedEvent(final int index, @Nonnull final Player player,
                                       @Nonnull final CharacterAttribute attribute, final int oldValue,
                                       final int newValue) {
        this.index = index;
        this.player = player;
        this.attribute = attribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    public int getIndex() {
        return index;
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public CharacterAttribute getAttribute() {
        return attribute;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }
}
