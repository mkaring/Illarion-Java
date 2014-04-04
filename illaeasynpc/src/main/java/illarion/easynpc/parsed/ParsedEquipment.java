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
package illarion.easynpc.parsed;

import illarion.easynpc.data.EquipmentSlots;
import illarion.easynpc.data.Items;
import illarion.easynpc.writer.LuaWriter;
import illarion.easynpc.writer.SQLBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;

/**
 * This class stores the parsed equipment data that contains information about what the NPC wears.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ParsedEquipment implements ParsedData {
    /**
     * The format string for the LUA version of this data type.
     */
    @SuppressWarnings("nls")
    private static final String LUA_FORMAT = "mainNPC:setEquipment(%1$s, %2$s);";

    /**
     * The item that is supposed to be placed in this slot.
     */
    private final Items item;

    /**
     * The equipment slot the item is supposed to be placed in.
     */
    private final EquipmentSlots slot;

    /**
     * Create a instance of the parsed equipment.
     *
     * @param itemSlot the slot the item is placed in
     * @param slotItem the item that is placed in the slot
     */
    public ParsedEquipment(final EquipmentSlots itemSlot, final Items slotItem) {
        slot = itemSlot;
        item = slotItem;
    }

    /**
     * The equipment data does not effect the query.
     */
    @Override
    public void buildSQL(@Nonnull final SQLBuilder builder) {
        // nothing to add to the query
    }

    /**
     * Check if the current stage is effected by the values stores in this
     * construct.
     */
    @Override
    public boolean effectsLuaWritingStage(@Nonnull final LuaWriter.WritingStage stage) {
        return stage == LuaWriter.WritingStage.Clothes;
    }

    /**
     * The equipment of the character is a part of the base NPC. There are no
     * additional modules needed.
     */
    @Nullable
    @Override
    public String[] getRequiredModules() {
        return null;
    }

    /**
     * Write the LUA representation of this data to the LUA NPC script.
     */
    @Override
    public void writeLua(
            @Nonnull final Writer target, @Nonnull final LuaWriter.WritingStage stage) throws IOException {
        if (!effectsLuaWritingStage(stage)) {
            return;
        }

        target.write(String.format(LUA_FORMAT, Integer.toString(slot.getLuaId()), Integer.toString(item.getItemId())));
        target.write(LuaWriter.NL);
    }
}
