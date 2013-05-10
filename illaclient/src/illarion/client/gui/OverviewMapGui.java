/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.gui;

import illarion.common.types.Location;

import javax.annotation.Nonnull;

/**
 * This interface is used to control the mini map.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface OverviewMapGui {
    /**
     * This interface defines a pointer on the map that points towards a specified location.
     */
    interface Pointer {
        /**
         * Get target of this pointer.
         *
         * @param loc the location this pointer is supposed to point to
         */
        void setTarget(@Nonnull Location loc);
    }

    /**
     * Create a new pointer instance.
     *
     * @return the new pointer
     */
    Pointer createPointer();

    /**
     * Cleanup this pointer. Once this is called the instance is not usable anymore
     *
     * @param pointer the pointer to release
     */
    void releasePointer(@Nonnull Pointer pointer);

    /**
     * Add the pointer to the mini map.
     *
     * @param pointer the pointer to be added
     */
    void addPointer(@Nonnull Pointer pointer);

    /**
     * Remove the pointer from the mini map.
     *
     * @param pointer the pointer to remove
     */
    void removePointer(@Nonnull Pointer pointer);

    /**
     * Show the world map window.
     */
    void showWorldMap();

    /**
     * Hide the world map window.
     */
    void hideWorldMap();
}
