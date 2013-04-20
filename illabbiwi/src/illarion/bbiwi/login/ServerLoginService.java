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
import illarion.bbiwi.gui.MainFrame;
import illarion.bbiwi.net.ReplyFactory;
import illarion.bbiwi.net.client.KeepAliveCmd;
import illarion.bbiwi.net.client.LoginCmd;
import illarion.common.config.Config;
import illarion.common.net.NetComm;
import illarion.common.net.Server;
import org.jdesktop.swingx.auth.LoginService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

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

        @Nullable Server usedServer = null;
        for (@Nonnull final Server checkServer : Server.values()) {
            if (checkServer.getServerNameEnglish().equals(server)) {
                usedServer = checkServer;
                break;
            }
        }
        if (usedServer == null) {
            return false;
        }

        if (!netComm.connect(usedServer.getServerHost(), usedServer.getServerPort(), new ReplyFactory(), null)) {
            return false;
        }

        BBIWI.getConnectionMonitor().startLogin(netComm);

        netComm.sendCommand(new LoginCmd(name, String.valueOf(password), usedServer.getMonitoringClientVersion()));
        netComm.setupKeepAlive(10000, new KeepAliveCmd());

        BBIWI.getConnectionMonitor().waitUntilLoginDone(4000);

        if (BBIWI.getConnectionMonitor().isLoginSuccessful()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final MainFrame frame = new MainFrame();
                    BBIWI.setMainGui(frame);
                    frame.setVisible(true);
                }
            });
            return true;
        }
        netComm.disconnect();
        return false;
    }
}
