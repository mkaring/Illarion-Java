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
package illarion.client.gui.controller.game;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import illarion.client.graphics.AnimationUtility;
import illarion.client.gui.OverviewMapGui;
import illarion.client.resources.MiscImageFactory;
import illarion.client.world.World;
import illarion.common.types.Location;
import illarion.common.util.FastMath;
import org.illarion.engine.GameContainer;
import org.illarion.engine.graphic.Color;
import org.illarion.engine.graphic.Graphics;
import org.illarion.engine.graphic.Sprite;
import org.illarion.engine.nifty.IgeRenderImage;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class is the GUI control of the mini map and the world map. It takes care of all the required interaction with
 * the mini map and the world map GUI, however it does not draw the overview map itself. It just displays the already
 * drawn map on the GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class OverviewMapHandler implements OverviewMapGui, ScreenController, UpdatableHandler {
    /**
     * This is the implementation of the pointers. This class is the image that is displayed on the GUI in order to
     * show the arrow on the mini map. It takes care for rendering it with the proper rotation applied.
     */
    private static final class MapArrowPointer implements IgeRenderImage, Pointer {
        /**
         * The sprite that contains the arrow. This is displayed in case the point is outside the area of the mini map.
         */
        @Nonnull
        private final Sprite arrowSprite;

        /**
         * The sprite that contains the point. This is displayed in case the target location is on the area of the
         * mini map.
         */
        @Nonnull
        private final Sprite pointSprite;

        /**
         * The location the arrow is supposed to point to.
         */
        @Nonnull
        private final Location targetLocation;

        /**
         * The Nifty-GUI element this pointer is assigned to.
         */
        @Nonnull
        private final Element parentElement;

        /**
         * The current angle of the arrow in (1/10)°
         */
        private int currentAngle;

        /**
         * The target angle of the arrow in (1/10)°
         */
        private int targetAngle;

        /**
         * The current delta value of the X coordinates.
         */
        private int currentDeltaX;

        /**
         * The current delta value of the Y coordinates.
         */
        private int currentDeltaY;

        /**
         * Create a new arrow pointer.
         */
        MapArrowPointer(@Nonnull final Element parentElement) {
            arrowSprite = MiscImageFactory.getInstance().getTemplate(MiscImageFactory.MINI_MAP_ARROW).getSprite();
            pointSprite = MiscImageFactory.getInstance().getTemplate(MiscImageFactory.MINI_MAP_POINT).getSprite();
            targetLocation = new Location();
            this.parentElement = parentElement;
        }

        @Override
        public int getWidth() {
            return arrowSprite.getWidth();
        }

        @Override
        public int getHeight() {
            return arrowSprite.getHeight();
        }

        @Override
        public void dispose() {
            // nothing to do
        }

        private float getAngle() {
            final double angle = Math.toDegrees(Math.atan2(currentDeltaY, currentDeltaX));

            return (float) angle + 45.f;
        }

        @Override
        public void renderImage(@Nonnull final Graphics g, final int x, final int y, final int width, final int height,
                                @Nonnull final Color color, final float imageScale) {
            renderImage(g, x, y, width, height, 0, 0, arrowSprite.getWidth(), arrowSprite.getHeight(), color,
                    imageScale, arrowSprite.getWidth() / 2, arrowSprite.getHeight() / 2);
        }

        @Override
        public void renderImage(@Nonnull final Graphics g, final int x, final int y, final int w, final int h, final int srcX,
                                final int srcY, final int srcW, final int srcH, @Nonnull final Color color, final float scale,
                                final int centerX, final int centerY) {
            final int scaledWidth = Math.round(w * scale);
            final int scaledHeight = Math.round(h * scale);

            final int fixedX = x + Math.round((w - scaledWidth) * ((float) centerX / (float) w));
            final int fixedY = y + Math.round((h - scaledHeight) * ((float) centerY / (float) h));

            if (isOnMapArea()) {
                final int offsetX = (FastMath.sqrt(FastMath.sqr(currentDeltaX) / 2) * -FastMath.sign(currentDeltaX)) +
                        (FastMath.sqrt(FastMath.sqr(currentDeltaY) / 2) * -FastMath.sign(currentDeltaY));
                final int offsetY = (FastMath.sqrt(FastMath.sqr(currentDeltaX) / 2) * FastMath.sign(currentDeltaX)) +
                        (FastMath.sqrt(FastMath.sqr(currentDeltaY) / 2) * -FastMath.sign(currentDeltaY));

                g.drawTexture(pointSprite.getFrame(0), fixedX - offsetX, fixedY - offsetY,
                        scaledWidth, scaledHeight, srcX, srcY, srcW, srcH, centerX - fixedX, centerY - fixedY,
                        0.f, color);
            } else {
                final float angle = (float) currentAngle / 10.f;

                final int spriteOffsetX = arrowSprite.getOffsetX();
                final int spriteOffsetY = arrowSprite.getOffsetY();

                final float radianAngle = FastMath.toRadians(angle);
                final float sinAngle = FastMath.sin(radianAngle);
                final float cosAngle = FastMath.cos(radianAngle);

                final int rotationOffsetX = Math.round((spriteOffsetX * cosAngle) + (spriteOffsetY * sinAngle));
                final int rotationOffsetY = Math.round((spriteOffsetX * sinAngle) + (spriteOffsetY * cosAngle));

                g.drawTexture(arrowSprite.getFrame(0), fixedX + rotationOffsetX, fixedY - rotationOffsetY,
                        scaledWidth, scaledHeight, srcX, srcY, srcW, srcH, centerX - fixedX, centerY - fixedY,
                        angle, color);
            }
        }

        /**
         * This functions returns if this pointer is on the mini map right now or outside of it.
         *
         * @return {@code true} in case the pointer is on the map
         */
        private boolean isOnMapArea() {
            return FastMath.sqrt(FastMath.sqr(currentDeltaX) + FastMath.sqr(currentDeltaY)) < 71;
        }

        /**
         * Update the angle of arrow.
         *
         * @param delta the time since the last update
         */
        void update(final int delta) {
            final int dX = targetLocation.getScX() - World.getPlayer().getLocation().getScX();
            final int dY = targetLocation.getScY() - World.getPlayer().getLocation().getScY();

            if ((currentDeltaX != dX) || (currentDeltaY != dY)) {
                currentDeltaX = dX;
                currentDeltaY = dY;

                targetAngle = ((Math.round(getAngle() * 10.f) % 3600) + 3600) % 3600;
            }

            animateAngle(delta);
        }

        /**
         * Perform the animation of this arrow.
         *
         * @param delta the time since the last update
         */
        private void animateAngle(final int delta) {
            if (targetAngle == currentAngle) {
                return;
            }

            final int angleDiff = targetAngle - currentAngle;
            if (Math.abs(angleDiff) <= 1800) {
                currentAngle += AnimationUtility.approach(0, angleDiff, -1800, 1800, delta);
            } else if (angleDiff > 0) {
                currentAngle += AnimationUtility.approach(0, angleDiff - 3600, -3600, 0, delta);
            } else {
                currentAngle += AnimationUtility.approach(0, angleDiff + 3600, 0, 3600, delta);
            }
            currentAngle = ((currentAngle % 3600) + 3600) % 3600;
        }

        @Override
        public void setTarget(@Nonnull final Location loc) {
            targetLocation.set(loc);
        }

        /**
         * Get the parent Nifty-GUI element.
         *
         * @return the parent element
         */
        @Nonnull
        public Element getParentElement() {
            return parentElement;
        }
    }

    /**
     * The instance of Nifty used to control the elements on the screen.
     */
    private Nifty nifty;

    /**
     * This is the instance of the screen that is active and showing the game.
     */
    private Screen screen;

    /**
     * The main element of the mini map.
     */
    private Element miniMapPanel;

    /**
     * The window that shows the world map.
     */
    private Window worldMapWindow;

    /**
     * Get the panel that shows the world map.
     */
    private Element worldMapPanel;

    /**
     * The buffer that stores the currently not used instances of the mini map arrows.
     */
    @Nonnull
    private final Queue<MapArrowPointer> buffer;

    /**
     * The list of pointers that are currently active and need to be updated.
     */
    @Nonnull
    private final List<MapArrowPointer> activePointers;

    /**
     * Create a new game mini map handler.
     */
    public OverviewMapHandler() {
        buffer = new LinkedList<MapArrowPointer>();
        activePointers = new LinkedList<MapArrowPointer>();
    }

    @Override
    public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
        this.nifty = nifty;
        this.screen = screen;

        miniMapPanel = screen.findElementById("miniMapPanel");

        miniMapPanel.findElementById("miniMapImage").getRenderer(ImageRenderer.class).setImage(
                new NiftyImage(nifty.getRenderEngine(), World.getMap().getMinimap().getMiniMap()));

        worldMapWindow = screen.findNiftyControl("worldMapWindow", Window.class);
        worldMapPanel = worldMapWindow.getElement().findElementById("#mapContainer");
        worldMapPanel.findElementById("#worldMapImage").getRenderer(ImageRenderer.class).setImage(
                new NiftyImage(nifty.getRenderEngine(), World.getMap().getMinimap().getWorldMap()));
    }

    @Override
    public Pointer createPointer() {
        if (buffer.isEmpty()) {
            final ImageBuilder builder = new ImageBuilder();
            builder.visible(false);
            builder.align(ElementBuilder.Align.Center);
            builder.valign(ElementBuilder.VAlign.Center);
            final Element image = builder.build(nifty, screen, miniMapPanel);
            final MapArrowPointer pointer = new MapArrowPointer(image);
            image.getRenderer(ImageRenderer.class).setImage(new NiftyImage(nifty.getRenderEngine(), pointer));
            image.setConstraintHeight(SizeValue.px(pointer.getHeight()));
            image.setConstraintWidth(SizeValue.px(pointer.getWidth()));
            miniMapPanel.layoutElements();
            return pointer;
        }
        return buffer.poll();
    }

    @Override
    public void releasePointer(@Nonnull final Pointer pointer) {
        if (pointer instanceof MapArrowPointer) {
            final MapArrowPointer arrowPointer = (MapArrowPointer) pointer;
            activePointers.remove(arrowPointer);
            if (arrowPointer.getParentElement().isVisible()) {
                arrowPointer.getParentElement().hide(new EndNotify() {
                    @Override
                    public void perform() {
                        buffer.offer(arrowPointer);
                    }
                });
            } else {
                buffer.offer(arrowPointer);
            }
        }
    }

    @Override
    public void addPointer(@Nonnull final Pointer pointer) {
        if (pointer instanceof MapArrowPointer) {
            final MapArrowPointer arrowPointer = (MapArrowPointer) pointer;
            arrowPointer.getParentElement().show(new EndNotify() {
                @Override
                public void perform() {
                    if (!activePointers.contains(arrowPointer)) {
                        activePointers.add(arrowPointer);
                    }
                }
            });
        }
    }

    @Override
    public void removePointer(@Nonnull final Pointer pointer) {
        if (pointer instanceof MapArrowPointer) {
            final MapArrowPointer arrowPointer = (MapArrowPointer) pointer;
            activePointers.remove(arrowPointer);
            arrowPointer.getParentElement().hide();
        }
    }

    @Override
    public void showWorldMap() {
        if (worldMapWindow.getElement().isVisible()) {
            worldMapWindow.moveToFront();
        } else {
            worldMapWindow.getElement().show(new EndNotify() {
                @Override
                public void perform() {
                    worldMapWindow.moveToFront();
                }
            });
        }
    }

    @Override
    public void hideWorldMap() {
        if (worldMapWindow.getElement().isVisible()) {
            worldMapWindow.closeWindow();
        }
    }

    @Override
    public void onStartScreen() {
        buffer.clear();
    }

    @Override
    public void onEndScreen() {
        buffer.clear();
    }

    @Override
    public void update(final GameContainer container, final int delta) {
        for (@Nonnull final MapArrowPointer pointer : activePointers) {
            pointer.update(delta);
        }
    }
}
