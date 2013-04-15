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

import illarion.common.data.CharacterAttribute;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;

/**
 * This is the event that is published on the event bus in case a update of the player attributes is received.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerAttributeEvent implements PlayerEvent {
    /**
     * The ID of the character effected by this event.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The attribute that is effected by this event.
     */
    @Nonnull
    private final CharacterAttribute attribute;

    /**
     * The new value of the attribute.
     */
    private final int value;

    /**
     * Create a new player attribute update event.
     *
     * @param charId    the ID of the effected character
     * @param attribute the attribute that is updated
     * @param value     the new value of the attribute
     */
    public PlayerAttributeEvent(@Nonnull final CharacterId charId, @Nonnull final CharacterAttribute attribute, final int value) {
        this.charId = charId;
        this.attribute = attribute;
        this.value = value;
    }

    @Nonnull
    @Override
    public CharacterId getCharId() {
        return charId;
    }

    /**
     * Get the attribute that is updated by this event.
     *
     * @return the updated attribute
     */
    @Nonnull
    public CharacterAttribute getAttribute() {
        return attribute;
    }

    /**
     * Get the new value of the attribute.
     *
     * @return the new attribute value
     */
    public int getValue() {
        return value;
    }
}
