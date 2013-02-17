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
package illarion.bbiwi.net;

import illarion.bbiwi.net.server.*;
import illarion.common.net.AbstractReplyFactory;

/**
 * This is the reply factory implementation that stores the replies known to the BBIWI tool.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ReplyFactory extends AbstractReplyFactory {
    @Override
    protected void registerReplies() {
        register(DisconnectMsg.class);
        register(MessageMsg.class);
        register(PersonalIdMsg.class);
        register(PlayerActionMsg.class);
        register(PlayerAttributeMsg.class);
        register(PlayerLocationMsg.class);
        register(PlayerLogoutMsg.class);
        register(PlayerMsg.class);
        register(PlayerSkillMsg.class);
        register(PlayerTalkMsg.class);
    }
}
