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
package illarion.bbiwi.world;

import illarion.bbiwi.events.PlayerLoginEvent;
import illarion.bbiwi.events.PlayerLogoutEvent;
import illarion.common.types.CharacterId;
import org.apache.log4j.Logger;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class maintains the list of all players on the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class Players extends AbstractListModel<String> {
    /**
     * The logger that takes care for the logging output of this class.
     */
    @Nonnull
    private static final Logger LOGGER = Logger.getLogger(Players.class);

    /**
     * This map stores the references to all the players that are currently logged in.
     */
    @Nonnull
    private final List<Player> players;

    /**
     * This map stores the references to the players that were online but are offline now.
     */
    @Nonnull
    private final List<Player> offlinePlayers;

    /**
     * The comparator used to sort the player list.
     */
    @Nonnull
    private final Comparator<Player> playerComparator;

    /**
     * Create a new instance of the players list.
     */
    public Players() {
        players = new ArrayList<Player>();
        offlinePlayers = new ArrayList<Player>();
        playerComparator = new Comparator<Player>() {
            @Override
            public int compare(final Player player1, final Player player2) {
                return player1.getName().compareTo(player2.getName());
            }
        };

        AnnotationProcessor.process(this);
    }

    /**
     * Get a player.
     *
     * @param id the ID of the player
     * @return the player or {@code null} in case the player is not known
     */
    @Nullable
    public Player getPlayer(@Nonnull final CharacterId id) {
        for (@Nonnull final Player player : players) {
            if (player.getCharacterId().equals(id)) {
                return player;
            }
        }
        for (@Nonnull final Player player : offlinePlayers) {
            if (player.getCharacterId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Get a who is online player.
     *
     * @param index the index of the player
     * @return the player
     */
    @Nonnull
    public Player getOnlinePlayer(final int index) {
        int cnt = 0;
        for (@Nonnull final Player player : players) {
            if (index == cnt) {
                return player;
            }
            cnt++;
        }
        throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    /**
     * Handle player login events.
     *
     * @param event the login event
     */
    @EventSubscriber(eventClass = PlayerLoginEvent.class,
            eventServiceName = EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE)
    public void onPlayerLoginEvent(@Nonnull final PlayerLoginEvent event) {
        Player player = getPlayer(event.getCharId());
        if (player == null) {
            player = new Player(event.getCharId(), event.getName());
            final int insertIndex = Collections.binarySearch(players, player, playerComparator);
            if (insertIndex < 0) {
                players.add(-insertIndex - 1, player);
                fireIntervalAdded(this, -insertIndex - 1, -insertIndex - 1);
            } else {
                players.set(insertIndex, player);
                fireContentsChanged(this, insertIndex, insertIndex);
            }
        } else {
            player.setName(event.getName());
            if (offlinePlayers.remove(player)) {
                players.add(player);
                fireIntervalAdded(this, 0, players.size());
            }
        }
        player.setLocation(event.getLocation());
        player.setOnline(true);
    }

    /**
     * Handle player logout events.
     *
     * @param event the logout event
     */
    @EventSubscriber(eventClass = PlayerLogoutEvent.class,
            eventServiceName = EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE)
    public void onPlayerLogoutEvent(@Nonnull final PlayerLogoutEvent event) {
        final Player player = getPlayer(event.getCharId());
        if (player == null) {
            LOGGER.warn("Logout received for a character that was not even logged in.");
        } else {
            player.setOnline(false);

            final int removeIndex = players.indexOf(player);
            if (removeIndex >= 0) {
                players.remove(removeIndex);
                offlinePlayers.add(player);
                fireIntervalRemoved(this, removeIndex, removeIndex);
            }
        }
    }

    /**
     * Get the amount of players.
     *
     * @return the amount of players
     */
    public int getOnlinePlayerCount() {
        return players.size();
    }

    @Override
    public int getSize() {
        return getOnlinePlayerCount();
    }

    @Override
    public String getElementAt(final int index) {
        return getOnlinePlayer(index).getName();
    }
}
