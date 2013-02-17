/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.net;

import illarion.client.net.server.*;
import illarion.common.net.AbstractReplyFactory;

import javax.annotation.Nonnull;

/**
 * The Factory for commands the server sends to the client. This factory creates the required message objects on
 * demand.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("OverlyCoupledClass")
public final class ReplyFactory extends AbstractReplyFactory {
    /**
     * The singleton instance of this factory.
     */
    private static final ReplyFactory INSTANCE = new ReplyFactory();

    /**
     * The default constructor of the factory.
     */
    private ReplyFactory() {
        super();
    }

    /**
     * Get the singleton instance of this class.
     *
     * @return the singleton instance of this class
     */
    @Nonnull
    public static ReplyFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected void registerReplies() {
        register(AppearanceMsg.class);
        register(AttackMsg.class);
        register(AttributeMsg.class);
        register(BookMsg.class);
        register(ChangeItemMsg.class);
        register(CharacterAnimationMsg.class);
        register(CloseShowcaseMsg.class);
        register(CloseDialogMsg.class);
        register(DateTimeMsg.class);
        register(DialogCraftingMsg.class);
        register(DialogCraftingUpdateMsg.class);
        register(DialogInputMsg.class);
        register(DialogMerchantMsg.class);
        register(DialogMessageMsg.class);
        register(DialogSelectionMsg.class);
        register(DisconnectMsg.class);
        register(GraphicEffectMsg.class);
        register(InformMsg.class);
        register(IntroduceMsg.class);
        register(InventoryMsg.class);
        register(ItemUpdateMsg.class);
        register(LocationMsg.class);
        register(LookAtCharMsg.class);
        register(LookAtDialogItemMsg.class);
        register(LookAtInvMsg.class);
        register(LookAtMapItemMsg.class);
        register(LookAtShowcaseMsg.class);
        register(LookAtTileMsg.class);
        register(MagicFlagMsg.class);
        register(MapCompleteMsg.class);
        register(MapStripeMsg.class);
        register(MoveMsg.class);
        register(MusicMsg.class);
        register(PlayerIdMsg.class);
        register(PutItemMsg.class);
        register(QuestMsg.class);
        register(RemoveCharMsg.class);
        register(RemoveItemMsg.class);
        register(SayMsg.class);
        register(ShoutMsg.class);
        register(WhisperMsg.class);
        register(ShowcaseMsg.class);
        register(SkillMsg.class);
        register(SoundEffectMsg.class);
        register(TargetLostMsg.class);
        register(TurnCharMsg.class);
        register(WeatherMsg.class);
    }
}
