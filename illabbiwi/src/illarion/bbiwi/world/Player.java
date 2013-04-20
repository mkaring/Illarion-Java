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

import illarion.bbiwi.events.PlayerAttributeChangedEvent;
import illarion.bbiwi.events.PlayerLocationChangedEvent;
import illarion.bbiwi.events.PlayerSkillChangedEvent;
import illarion.common.data.CharacterAttribute;
import illarion.common.data.Skill;
import illarion.common.types.CharacterId;
import illarion.common.types.Location;
import org.bushe.swing.event.EventServiceLocator;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a single player on the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class Player {
    /**
     * The character ID of the player.
     */
    @Nonnull
    private final CharacterId characterId;

    /**
     * The name of the player.
     */
    @Nonnull
    private String name;

    /**
     * The location of the player.
     */
    @Nonnull
    private final Location location;

    /**
     * The attributes of the player character.
     */
    @Nonnull
    private final Map<CharacterAttribute, Integer> attributes;

    /**
     * The skills of the player character.
     */
    @Nonnull
    private final Map<Skill, Integer> skills;

    /**
     * This is {@code true} as long as the player is online.
     */
    private boolean online;

    /**
     * The parent player list.
     */
    @Nonnull
    private final Players playerList;

    /**
     * Create a new player character.
     *
     * @param parentList the player list this player object belongs to
     * @param id         the ID of the character
     */
    public Player(@Nonnull final Players parentList, @Nonnull final CharacterId id) {
        characterId = id;
        location = new Location();
        attributes = new EnumMap<CharacterAttribute, Integer>(CharacterAttribute.class);
        skills = new HashMap<Skill, Integer>();

        playerList = parentList;
    }

    /**
     * Update the location a character is located at.
     *
     * @param loc the new location
     */
    public void setLocation(@Nonnull final Location loc) {
        final Location tempLocation = new Location(location);
        location.set(loc);

        if (!isOnline()) {
            return;
        }

        publish(new PlayerLocationChangedEvent(getIndex(), this, tempLocation, new Location(loc)));
    }

    /**
     * Set the value of the online flag.
     *
     * @param on the value of the online flag
     */
    public void setOnline(final boolean on) {
        online = on;
        if (online) {
            playerList.addPlayerToOnlineList(this);
        } else {
            playerList.addPlayerToOfflineList(this);
        }
    }

    /**
     * Set the name of the character.
     *
     * @param newName the name of the character
     */
    public void setName(@Nonnull final String newName) {
        name = newName;
    }

    public void setSkill(@Nonnull final Skill skill, final int value) {
        final int oldValue = getSkill(skill);

        skills.put(skill, value);

        if (!isOnline()) {
            return;
        }

        publish(new PlayerSkillChangedEvent(getIndex(), this, skill, oldValue, value));
    }

    private int getSkill(final Skill skill) {
        if (skills.containsKey(skill)) {
            return skills.get(skill);
        }
        return 0;
    }

    public void setAttribute(@Nonnull final CharacterAttribute attribute, final int value) {
        final int oldValue = getAttribute(attribute);
        attributes.put(attribute, value);

        if (!isOnline()) {
            return;
        }

        publish(new PlayerAttributeChangedEvent(getIndex(), this, attribute, oldValue, value));
    }

    private static void publish(@Nonnull final Object event) {
        EventServiceLocator.getSwingEventService().publish(event);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public int getIndex() {
        return playerList.getPlayerIndex(this);
    }

    @Nonnull
    public CharacterId getCharacterId() {
        return characterId;
    }

    public boolean isOnline() {
        return online;
    }

    public int getAttribute(@Nonnull final CharacterAttribute attribute) {
        if (attributes.containsKey(attribute)) {
            return attributes.get(attribute);
        }
        return 0;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }
}
