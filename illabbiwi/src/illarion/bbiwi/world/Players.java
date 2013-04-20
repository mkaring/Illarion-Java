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

import illarion.bbiwi.events.NewPlayerOnListEvent;
import illarion.bbiwi.events.RemovedPlayerFromListEvent;
import illarion.common.types.CharacterId;
import org.apache.log4j.Logger;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class maintains the list of all players on the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class Players {
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
     * @return the player
     */
    @Nonnull
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
        return new Player(this, id);
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
     * Add a player to the list of online players.
     *
     * @param player the player that is now online
     */
    public void addPlayerToOnlineList(@Nonnull final Player player) {
        offlinePlayers.remove(player);

        final int insertIndex = Collections.binarySearch(players, player, playerComparator);
        if (insertIndex < 0) {
            players.add(-insertIndex - 1, player);
            publish(new NewPlayerOnListEvent(-insertIndex - 1, player));
        }
    }

    /**
     * Get the index of the player.
     *
     * @param player the player
     * @return the index of the player in case the player is present in the online list, else any value below {@code 0}
     */
    public int getPlayerIndex(@Nonnull final Player player) {
        return Collections.binarySearch(players, player, playerComparator);
    }

    /**
     * Add a player to the list of offline players.
     *
     * @param player the player to move to the list of offline players
     */
    public void addPlayerToOfflineList(@Nonnull final Player player) {
        final int playerIndex = Collections.binarySearch(players, player, playerComparator);
        if (playerIndex >= 0) {
            players.remove(playerIndex);
            publish(new RemovedPlayerFromListEvent(playerIndex, player));
        }
        if (!offlinePlayers.contains(player)) {
            offlinePlayers.add(player);
        }
    }

    private static void publish(@Nonnull final Object event) {
        EventServiceLocator.getSwingEventService().publish(event);
    }

    /**
     * Get the amount of players.
     *
     * @return the amount of players
     */
    public int getOnlinePlayerCount() {
        return players.size();
    }
}
