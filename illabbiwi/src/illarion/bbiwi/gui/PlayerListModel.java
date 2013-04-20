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

import illarion.bbiwi.BBIWI;
import illarion.bbiwi.events.NewPlayerOnListEvent;
import illarion.bbiwi.events.RemovedPlayerFromListEvent;
import illarion.bbiwi.world.Player;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * This is the list model that handles showing the character list.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerListModel extends AbstractListModel<Player> {
    public PlayerListModel() {
        AnnotationProcessor.process(this);
    }

    @Override
    public int getSize() {
        return BBIWI.getPlayers().getOnlinePlayerCount();
    }

    @Override
    public Player getElementAt(final int index) {
        return BBIWI.getPlayers().getOnlinePlayer(index);
    }

    @EventSubscriber(eventClass = NewPlayerOnListEvent.class,
            eventServiceName = EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE)
    public void onNewPlayerOnListEvent(@Nonnull final NewPlayerOnListEvent event) {
        fireIntervalAdded(this, event.getIndex(), event.getIndex());
    }

    @EventSubscriber(eventClass = RemovedPlayerFromListEvent.class,
            eventServiceName = EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE)
    public void onRemovePlayerFromListEvent(@Nonnull final RemovedPlayerFromListEvent event) {
        fireIntervalRemoved(this, event.getIndex(), event.getIndex());
    }
}
