/*
 * This file is part of the Illarion Common Library.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion Common Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Common Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Common Library.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.common.net;

import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * The default factory implementation for commands that are send from the client to the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractReplyFactory implements ServerReplyFactory {
    /**
     * The logger that takes care for the logging output of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractReplyFactory.class);

    /**
     * This map stores the message classes along with the IDs of the command encoded in them.
     */
    @Nonnull
    private final Map<Integer, Class<? extends ServerReply>> replyMap;

    /**
     * The default constructor of the factory. This registers all commands.
     */
    protected AbstractReplyFactory() {
        replyMap = new HashMap<Integer, Class<? extends ServerReply>>();

        registerReplies();
    }

    /**
     * Once this function is executed all the replies known to this factory need to be registered.
     */
    protected abstract void registerReplies();

    /**
     * Get a replay instance. This class will check if there is any reply fitting the ID registered and create a new
     * instance of it.
     *
     * @param id the ID of the reply
     * @return the newly created reply instance
     */
    @Override
    @Nullable
    public ServerReply getReply(final int id) {
        final Class<? extends ServerReply> replyClass = replyMap.get(id);

        if (replyClass == null) {
            LOGGER.error("Illegal reply requested. ID: 0x" + Integer.toHexString(id));
            return null;
        }

        try {
            return replyClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("Failed to create instance of reply class!", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Access to reply class constructor was denied.", e);
        }
        return null;
    }

    /**
     * Register a class as replay message class. Those classes need to implement the {@link ServerReply} interface
     * and they require the contain the {@link ReplyMessage} annotation.
     *
     * @param clazz the class to register as reply.
     */
    protected void register(@Nonnull final Class<? extends ServerReply> clazz) {
        final ReplyMessage messageData = clazz.getAnnotation(ReplyMessage.class);

        if (messageData == null) {
            LOGGER.error("Illegal class supplied to register! No annotation: " + clazz.getName());
            return;
        }

        if (replyMap.containsKey(messageData.replyId())) {
            LOGGER.error("Class with duplicated key: " + clazz.getName());
            return;
        }

        replyMap.put(messageData.replyId(), clazz);
    }
}
