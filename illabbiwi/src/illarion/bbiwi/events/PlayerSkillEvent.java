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
package illarion.bbiwi.events;

import illarion.common.data.Skill;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;

/**
 * This is the event that is published on the event bus in case a update of the player skills is received.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerSkillEvent implements PlayerEvent {
    /**
     * The ID of the character effected by this event.
     */
    @Nonnull
    private final CharacterId charId;

    /**
     * The skill that is effected by this event.
     */
    @Nonnull
    private final Skill skill;

    /**
     * The new value of the skill.
     */
    private final int value;

    /**
     * The new minor skill value.
     */
    private final int minor;

    /**
     * Create a new player skill update event.
     *
     * @param charId the ID of the effected character
     * @param skill  the skill that is updated
     * @param value  the new value of the skill
     * @param minor  the new minor value of the skill
     */
    public PlayerSkillEvent(@Nonnull final CharacterId charId, @Nonnull final Skill skill, final int value, final int minor) {
        this.charId = charId;
        this.skill = skill;
        this.value = value;
        this.minor = minor;
    }

    @Nonnull
    @Override
    public CharacterId getCharId() {
        return charId;
    }

    /**
     * Get the skill that is updated by this event.
     *
     * @return the updated skill
     */
    @Nonnull
    public Skill getSkill() {
        return skill;
    }

    /**
     * Get the new value of the skill.
     *
     * @return the new skill value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the new minor skill value.
     *
     * @return the new minor value of the skill
     */
    public int getMinor() {
        return minor;
    }
}
