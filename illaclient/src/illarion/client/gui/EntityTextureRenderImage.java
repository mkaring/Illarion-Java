/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2012 - Illarion e.V.
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

import illarion.client.resources.data.ItemTemplate;
import org.illarion.engine.nifty.IgeTextureRenderImage;

/**
 * This implementation of a texture render image is used to show a image that is usually used in the game graphics,
 * inside the elements of the GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class EntityTextureRenderImage extends IgeTextureRenderImage {
    /**
     * Create this render image that refers to a specified entity.
     *
     * @param entity the entity the image refers to
     */
    public EntityTextureRenderImage(final ItemTemplate entity) {
        super(entity.getGuiTexture());
    }
}
