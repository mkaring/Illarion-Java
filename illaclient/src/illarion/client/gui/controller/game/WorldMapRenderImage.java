/*
 * This file is part of the Illarion Game Engine.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion Game Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Game Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Game Engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.gui.controller.game;

import illarion.common.util.FastMath;
import org.illarion.engine.graphic.Color;
import org.illarion.engine.graphic.Graphics;
import org.illarion.engine.graphic.WorldMap;
import org.illarion.engine.nifty.IgeRenderImage;

import javax.annotation.Nonnull;

/**
 * This implementation of the render image is used in special to render the world map of the game.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class WorldMapRenderImage implements IgeRenderImage {
    /**
     * The world map that supplies the data to this render image.
     */
    private final WorldMap map;

    /**
     * The height and width of the map in the unscaled version.
     */
    private final int unscaledSize;

    /**
     * Create a new render engine that shows the mini map.
     *
     * @param map the world map that supplied the data
     */
    public WorldMapRenderImage(@Nonnull final WorldMap map) {
        this.map = map;
        unscaledSize = FastMath.sqrt(FastMath.sqr(WorldMap.WORLD_MAP_HEIGHT) + FastMath.sqr(WorldMap.WORLD_MAP_WIDTH));
    }

    @Override
    public void renderImage(@Nonnull final Graphics g, final int x, final int y, final int width, final int height,
                            @Nonnull final Color color, final float imageScale) {
        renderImage(g, x, y, width, height, 0, 0, getWidth(), getHeight(), color, imageScale, unscaledSize / 2,
                unscaledSize / 2);
    }

    @Override
    public void renderImage(@Nonnull final Graphics g, final int x, final int y, final int w, final int h, final int srcX,
                            final int srcY, final int srcW, final int srcH, @Nonnull final Color color, final float scale,
                            final int centerX, final int centerY) {
        final int unscaledWidth = Math.round(((float) w / (float) unscaledSize) * WorldMap.WORLD_MAP_WIDTH);
        final int unscaledHeight = Math.round(((float) h / (float) unscaledSize) * WorldMap.WORLD_MAP_HEIGHT);
        final int scaledWidth = Math.round(unscaledWidth * scale);
        final int scaledHeight = Math.round(unscaledHeight * scale);

        final int realSrcW = Math.round(((float) srcW / (float) unscaledSize) * (float) WorldMap.WORLD_MAP_WIDTH);
        final int realSrcH = Math.round(((float) srcH / (float) unscaledSize) * (float) WorldMap.WORLD_MAP_HEIGHT);

        final int fixedX = Math.round(x + ((unscaledWidth - scaledWidth) * ((float) centerX / (float) unscaledWidth))
                + ((w - scaledWidth) / 2.f));
        final int fixedY = Math.round(y + ((unscaledHeight - scaledHeight) * ((float) centerY / (float)
                unscaledHeight)) + ((h - scaledHeight) / 2.f));

        g.drawTexture(map.getWorldMap(), fixedX, fixedY,
                scaledWidth, scaledHeight, srcX,
                srcY, realSrcW, realSrcH, centerX - fixedX, centerY - fixedY, -45.f, color);
    }

    @Override
    public int getWidth() {
        return unscaledSize;
    }

    @Override
    public int getHeight() {
        return unscaledSize;
    }

    @Override
    public void dispose() {
        // nothing to do
    }
}
