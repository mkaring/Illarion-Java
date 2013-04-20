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
package illarion.bbiwi.gui;

import illarion.bbiwi.world.Player;
import org.jdesktop.swingx.JXFrame;

import javax.annotation.Nonnull;

/**
 * This frame contains details regarding a single player.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerDetailsFrame extends JXFrame {
    /**
     * The player that is shown in this frame.
     */
    private final Player player;

    /**
     * Create a new player details frame and set the player that is supposed to be displayed in this frame.
     *
     * @param player the player to display
     */
    public PlayerDetailsFrame(@Nonnull final Player player) {
        super(player.getName());
        this.player = player;
    }
}
