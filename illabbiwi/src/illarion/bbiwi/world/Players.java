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
import java.util.HashMap;
import java.util.Map;

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
    private final Map<CharacterId, Player> playerMap;

    /**
     * Create a new instance of the players list.
     */
    public Players() {
        playerMap = new HashMap<CharacterId, Player>();

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
        return playerMap.get(id);
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
            playerMap.put(event.getCharId(), player);
        } else {
            player.setName(event.getName());
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
        }
    }
}
