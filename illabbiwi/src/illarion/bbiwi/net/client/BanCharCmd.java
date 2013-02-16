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
package illarion.bbiwi.net.client;

import illarion.common.net.NetCommWriter;
import illarion.common.types.CharacterId;

import javax.annotation.Nonnull;

/**
 * This command is used to issue a ban against a character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class BanCharCmd extends AbstractCommand {
    /**
     * The character id of the character that is supposed to be banned.
     */
    private final CharacterId charId;

    /**
     * The name of the character that is supposed to be banned.
     * <p/>
     * TODO: Find out what one needs to smoke to come up with the idea to identify the character by ID and name.
     */
    private final String charName;

    /**
     * The time in seconds this character is supposed to remain banned.
     */
    private final long time;

    /**
     * Create a new instance of this ban command.
     *
     * @param charId   the ID of the character to ban
     * @param charName the name of the character to ban
     * @param time     the time in seconds to ban this character
     */
    public BanCharCmd(@Nonnull final CharacterId charId, @Nonnull final String charName, final long time) {
        super(0x04);
        this.charId = charId;
        this.charName = charName;
        this.time = time;
    }

    /**
     * Get the information about this object as a string.
     *
     * @return the data of the command as string
     */
    @Nonnull
    @Override
    public String toString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Encode data for transfer to server. Only for send commands.
     *
     * @param writer the byte buffer the values are added to from index 0 on
     */
    @Override
    public void encode(@Nonnull final NetCommWriter writer) {
        charId.encode(writer);
        writer.writeString(charName);
        writer.writeUInt(time);
    }
}
