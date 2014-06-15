/*
 * This file is part of the Illarion project.
 *
 * Copyright © 2014 - Illarion e.V.
 *
 * Illarion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Illarion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package illarion.client.input;

import illarion.client.IllaClient;
import illarion.client.world.World;
import illarion.common.gui.AbstractMultiActionHelper;
import org.bushe.swing.event.EventBus;
import org.illarion.engine.input.*;

import javax.annotation.Nonnull;

/**
 * This class is used to receive and forward all user input.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class InputReceiver implements InputListener {
    private static final int MOVE_KEY = 1;

    /**
     * This is the multi click helper that is used to detect single and multiple clicks on the game map.
     *
     * @author Martin Karing &lt;nitram@illarion.org&gt;
     */
    private static final class ButtonMultiClickHelper extends AbstractMultiActionHelper {
        /**
         * The x coordinate of the last reported click.
         */
        private int x;

        /**
         * The Y coordinate of the last reported click.
         */
        private int y;

        /**
         * The key that is used.
         */
        private Button key;

        /**
         * The constructor that sets the used timeout interval to the system default double click interval.
         */
        private ButtonMultiClickHelper() {
            super(IllaClient.getCfg().getInteger("doubleClickInterval"), 2);
        }

        /**
         * Update the data that is needed to report the state of the last click properly.
         *
         * @param mouseKey the key that was clicked
         * @param posX the x coordinate where the click happened
         * @param posY the y coordinate where the click happened
         */
        public void setInputData(@Nonnull Button mouseKey, int posX, int posY) {
            x = posX;
            y = posY;
            key = mouseKey;
        }

        @Override
        public void executeAction(int count) {
            switch (count) {
                case 1:
                    EventBus.publish(new ClickOnMapEvent(key, x, y));
                    break;
                case 2:
                    World.getMapDisplay().getGameScene().publishEvent(new DoubleClickOnMapEvent(key, x, y));
                    break;
            }
        }
    }

    /**
     * This class is used as helper for the point at events.
     */
    private static final class PointAtHelper extends AbstractMultiActionHelper {
        /**
         * The x coordinate of the last reported click.
         */
        private int x;

        /**
         * The Y coordinate of the last reported click.
         */
        private int y;

        /**
         * Default constructor.
         */
        private PointAtHelper() {
            super(50);
        }

        /**
         * Update the data that is needed to report the state of the last move properly.
         *
         * @param posX the x coordinate where the click happened
         * @param posY the y coordinate where the click happened
         */
        public void setInputData(int posX, int posY) {
            x = posX;
            y = posY;
        }

        @Override
        public void executeAction(int count) {
            EventBus.publish(new PointOnMapEvent(x, y));
        }
    }

    /**
     * The topic that is in general used to publish input events.
     */
    public static final String EB_TOPIC = "InputEvent";

    /**
     * The key mapper stores the keep-action assignments of the client.
     */
    @Nonnull
    private final KeyMapper keyMapper;

    /**
     * The instance of the button multi-click helper that is used in this instance of the input receiver.
     */
    private final ButtonMultiClickHelper buttonMultiClickHelper = new ButtonMultiClickHelper();

    /**
     * The instance of the point at helper used by this instance of the input receiver.
     */
    private final PointAtHelper pointAtHelper = new PointAtHelper();

    /**
     * The input engine.
     */
    private final Input input;

    /**
     * In case this value is set {@code true} this receiver is active and working. If not will discard all events
     * received.
     */
    private boolean enabled;

    /**
     * Create a new instance of the input receiver.
     *
     * @param input the input system this class is receiving its data from
     */
    public InputReceiver(Input input) {
        this.input = input;
        enabled = false;
        keyMapper = new KeyMapper(input);
    }

    /**
     * Set the enabled flag of this input receiver.
     *
     * @param value the new enabled flag
     */
    public void setEnabled(boolean value) {
        enabled = value;
    }

    @Override
    public void keyDown(@Nonnull Key key) {
        if (enabled) {
            keyMapper.handleKeyPressedInput(key);
        }
    }

    @Override
    public void keyUp(@Nonnull Key key) {
        if (enabled) {
            keyMapper.handleKeyReleasedInput(key);
        }
    }

    @Override
    public void keyTyped(char character) {
        // nothing
    }

    @Override
    public void buttonDown(int mouseX, int mouseY, @Nonnull Button button) {
        if (enabled) {
            buttonMultiClickHelper.setInputData(button, mouseX, mouseY);
            buttonMultiClickHelper.pulse();
        }
    }

    @Override
    public void buttonUp(int mouseX, int mouseY, @Nonnull Button button) {
        if (enabled) {
            World.getPlayer().getMovementHandler().getTargetMouseMovementHandler().disengage(true);
            World.getPlayer().getMovementHandler().getFollowMouseHandler().disengage(true);
            input.disableForwarding(ForwardingTarget.Mouse);
        }
    }

    @Override
    public void buttonClicked(int mouseX, int mouseY, @Nonnull Button button, int count) {
        // nothing
    }

    @Override
    public void mouseMoved(int mouseX, int mouseY) {
        if (enabled) {
            pointAtHelper.setInputData(mouseX, mouseY);
            pointAtHelper.pulse();
            EventBus.publish(new MoveOnMapEvent(mouseX, mouseY));
        }
    }

    @Override
    public void mouseDragged(
            @Nonnull Button button, int fromX, int fromY, int toX, int toY) {
        if (enabled) {
            EventBus.publish(new DragOnMapEvent(fromX, fromY, toX, toY, button));
        }
    }

    @Override
    public void mouseWheelMoved(int mouseX, int mouseY, int delta) {
        // nothing
    }
}
