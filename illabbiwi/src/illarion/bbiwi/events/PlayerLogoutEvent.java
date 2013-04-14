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

import javax.annotation.Nonnull;

/**
 * This is the event that is fired once a player is logging out. It does not implement the {@link PlayerEvent}
 * interface because its meant for the players list handler.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerLogoutEvent implements PlayerListEvent {
    /**
     * The ID of the character that logged out.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * Create a new player logout event.
     *
     * @param charId the ID of the character
     */
    public PlayerLogoutEvent(@Nonnull final CharacterId charId) {
        this.charId = charId;
    }

    /**
     * Get the ID of the character.
     *
     * @return the ID of the character
     */
    @Nonnull
    public CharacterId getCharId() {
        return charId;
    }
}
