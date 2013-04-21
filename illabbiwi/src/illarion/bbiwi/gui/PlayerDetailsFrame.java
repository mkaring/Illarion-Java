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

import illarion.bbiwi.events.PlayerAttributeChangedEvent;
import illarion.bbiwi.world.Player;
import illarion.common.data.CharacterAttribute;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * This frame contains details regarding a single player.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerDetailsFrame extends JXFrame {
    /**
     * The player that is shown in this frame.
     */
    @Nonnull
    private final Player player;

    @Nonnull
    private final JXLabel hitPointsLabel;

    @Nonnull
    private final JXLabel manaPointsLabel;

    @Nonnull
    private final JXLabel foodPointsLabel;

    /**
     * Create a new player details frame and set the player that is supposed to be displayed in this frame.
     *
     * @param player the player to display
     */
    public PlayerDetailsFrame(@Nonnull final Player player) {
        super(player.getName());
        this.player = player;

        final Container content = getContentPane();
        content.setLayout(new FlowLayout(FlowLayout.LEADING));

        final JXPanel hitPointsPanel = new JXPanel(new BorderLayout());
        hitPointsPanel.add(new JXLabel("HitPoints:"), BorderLayout.WEST);
        hitPointsLabel = new JXLabel(Integer.toString(player.getAttribute(CharacterAttribute.HitPoints)));
        hitPointsPanel.add(hitPointsLabel, BorderLayout.CENTER);
        content.add(hitPointsPanel);

        final JXPanel manaPointsPanel = new JXPanel(new BorderLayout());
        manaPointsPanel.add(new JXLabel("Mana:"), BorderLayout.WEST);
        manaPointsLabel = new JXLabel(Integer.toString(player.getAttribute(CharacterAttribute.ManaPoints)));
        manaPointsPanel.add(manaPointsLabel, BorderLayout.CENTER);
        content.add(manaPointsPanel);

        final JXPanel foodPointsPanel = new JXPanel(new BorderLayout());
        foodPointsPanel.add(new JXLabel("Foodpoints:"), BorderLayout.WEST);
        foodPointsLabel = new JXLabel(Integer.toString(player.getAttribute(CharacterAttribute.FoodPoints)));
        foodPointsPanel.add(foodPointsLabel, BorderLayout.CENTER);
        content.add(foodPointsPanel);

        pack();

        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = PlayerAttributeChangedEvent.class,
            eventServiceName = EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE)
    public void onPlayerAttributeChangedEvent(@Nonnull final PlayerAttributeChangedEvent event) {
        if (event.getPlayer().equals(player)) {
            switch (event.getAttribute()) {
                case HitPoints:
                    hitPointsLabel.setText(Integer.toString(event.getNewValue()));
                    break;
                case ManaPoints:
                    manaPointsLabel.setText(Integer.toString(event.getNewValue()));
                    break;
                case FoodPoints:
                    foodPointsLabel.setText(Integer.toString(event.getNewValue()));
                    break;
            }
        }
    }
}
