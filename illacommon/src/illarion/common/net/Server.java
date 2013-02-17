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
package illarion.common.net;

import javax.annotation.Nonnull;

/**
 * The definitions of the existing servers. All data needed to connect and identify a server is stored here.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("nls")
public enum Server {
    /**
     * The game server of Illarion. Normal players should connect to this server.
     */
    RealServer("Game server", "Spielserver", "illarion.org", 3008, 122, 200),

    /**
     * The test server of Illarion. Testers and developers need a client that is allowed to connect to this server.
     */
    TestServer("Test server", "Testserver", RealServer.serverAddress, 3012, 122, 200);

    /**
     * The client version that needs to be transferred to the server so it accepts the connection and the client
     * shows that it is up to date.
     */
    private final int clientVersion;

    /**
     * The version the monitoring client needs to report in order to indicate that it is up to date.
     */
    private final int monitoringClientVersion;

    /**
     * Storage of the server host address the client needs to connect to.
     */
    @Nonnull
    private final String serverAddress;

    /**
     * The english name of the server.
     */
    @Nonnull
    private final String serverNameEnglish;

    /**
     * The german name of the server.
     */
    @Nonnull
    private final String serverNameGerman;

    /**
     * The port of the server the clients needs to connect to.
     */
    private final int serverPort;

    /**
     * Default ENUM constructor for the enumeration entries. It creates a definition of a server and stores it to the
     * enumeration constants.
     *
     * @param nameEn         the english name of the server
     * @param nameDe         the german name of the server
     * @param address        the host address of the server
     * @param port           the port the server is listening for connections
     * @param clientVersion  the version number the client needs to report for a proper connection
     * @param monitorVersion the version number the monitoring client needs to report for a proper connection
     */
    Server(@Nonnull final String nameEn, @Nonnull final String nameDe, @Nonnull final String address,
           final int port, final int clientVersion, final int monitorVersion) {
        serverNameEnglish = nameEn;
        serverNameGerman = nameDe;
        serverAddress = address;
        serverPort = port;
        this.clientVersion = clientVersion;
        monitoringClientVersion = monitorVersion;
    }

    /**
     * Get the version of the client that need to be transferred to connect to this server.
     *
     * @return the client version that need to be transferred
     */
    public int getClientVersion() {
        return clientVersion;
    }

    /**
     * Get the version of the monitoring client that need to be transferred to connect to this server.
     *
     * @return the monitoring client version that need to be transferred
     */
    public int getMonitoringClientVersion() {
        return monitoringClientVersion;
    }

    /**
     * The the server host address of the server entry.
     *
     * @return the host address of the server
     */
    public String getServerHost() {
        return serverAddress;
    }

    /**
     * Get the english name of the server that is defined with this server entry.
     *
     * @return the english name of the server
     */
    @Nonnull
    public String getServerNameEnglish() {
        return serverNameEnglish;
    }

    /**
     * Get the german name of the server that is defined with this server entry.
     *
     * @return the german name of the server
     */
    @Nonnull
    public String getServerNameGerman() {
        return serverNameGerman;
    }

    /**
     * Get the port the server listens of the server entry.
     *
     * @return the port that is listened by the server
     */
    public int getServerPort() {
        return serverPort;
    }
}
