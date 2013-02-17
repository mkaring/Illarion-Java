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
package illarion.bbiwi.login;

import illarion.bbiwi.BBIWI;
import illarion.bbiwi.events.CommunicationEvent;
import illarion.bbiwi.events.DisconnectEvent;
import illarion.bbiwi.net.ReplyFactory;
import illarion.bbiwi.net.client.KeepAliveCmd;
import illarion.bbiwi.net.client.LoginCmd;
import illarion.common.config.Config;
import illarion.common.net.NetComm;
import illarion.common.net.Server;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.jdesktop.swingx.auth.LoginService;

/**
 * This is the login service that takes care for the connection to the server.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ServerLoginService extends LoginService {
    /**
     * The configuration system that is used by this login service.
     */
    private final Config config;

    /**
     * Create a new instance of the login service.
     *
     * @param config the configuration system
     */
    public ServerLoginService(final Config config) {
        this.config = config;
    }

    @Override
    public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
        final NetComm netComm = new NetComm(config);

        final Server usedServer;
        if (BBIWI.TEST_SERVER.equals(server)) {
            usedServer = Server.TestServer;
        } else {
            usedServer = Server.RealServer;
        }

        if (!netComm.connect(usedServer.getServerHost(), usedServer.getServerPort(), new ReplyFactory(), null)) {
            return false;
        }

        waitingForNetwork = true;
        loginSuccess = false;

        EventBus.subscribeStrongly(CommunicationEvent.class, new EventSubscriber<CommunicationEvent>() {
            @Override
            public void onEvent(final CommunicationEvent event) {
                loginSuccess = !(event instanceof DisconnectEvent);
                waitingForNetwork = false;
                EventBus.unsubscribe(CommunicationEvent.class, this);
            }
        });


        netComm.sendCommand(new LoginCmd(name, String.valueOf(password), usedServer.getMonitoringClientVersion()));
        netComm.setupKeepAlive(10000, new KeepAliveCmd());

        while (waitingForNetwork) {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException ex) {
                netComm.disconnect();
            }
        }

        if (loginSuccess) {
            return true;
        }
        netComm.disconnect();
        return false;
    }

    private boolean waitingForNetwork;
    private boolean loginSuccess;
}
