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
package illarion.bbiwi;

import illarion.bbiwi.crash.DefaultCrashHandler;
import illarion.bbiwi.gui.MainFrame;
import illarion.bbiwi.login.PasswordStorage;
import illarion.bbiwi.login.ServerLoginService;
import illarion.bbiwi.login.UserNameStorage;
import illarion.bbiwi.net.ConnectionMonitor;
import illarion.bbiwi.world.Players;
import illarion.common.config.Config;
import illarion.common.config.ConfigSystem;
import illarion.common.net.NetComm;
import illarion.common.net.Server;
import illarion.common.util.DirectoryManager;
import illarion.common.util.JavaLogToLog4J;
import illarion.common.util.StdOutToLog4J;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.SwingEventService;
import org.jdesktop.swingx.JXLoginPane;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * This is the main launch class for the BBIWI utility.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("ClassNamingConvention")
public final class BBIWI {
    /**
     * The title of this application.
     */
    public static final String APPLICATION = "BBIWI Tool";

    /**
     * The server of this tool.
     */
    public static final String VERSION = "2.0";

    /**
     * The logging instance of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BBIWI.class);

    /**
     * The configuration system used by the BBIWI.
     */
    private static ConfigSystem configSystem;

    /**
     * The list of players.
     */
    @Nullable
    private static Players players;

    /**
     * Get the connection monitor that keeps track of the connection status.
     */
    @Nonnull
    private static ConnectionMonitor connectionMonitor;

    /**
     * Initialize the configuration system.
     *
     * @throws IOException in case creating the configuration system failed
     */
    private static void initConfig() throws IOException {
        @Nullable final File userDirectory = DirectoryManager.getInstance().getUserDirectory();
        if (userDirectory == null) {
            throw new IOException("No user directory found.");
        }
        configSystem = new ConfigSystem(new File(userDirectory, "bbiwi.xcfgz"));
        configSystem.setDefault(NetComm.CFG_DEBUG_PROTOCOL_KEY, true);
        configSystem.setDefault(NetComm.CFG_DEBUG_NETWORK_KEY, false);
    }

    /**
     * Get the active instance of the configuration system.
     *
     * @return the instance of the configuration system
     */
    public static Config getConfig() {
        return configSystem;
    }

    /**
     * Launch function.
     *
     * @param args launch arguments
     */
    public static void main(final String[] args) {
        try {
            EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS, new SwingEventService());
        } catch (EventServiceExistsException e) {
            System.err.println("Failed to setup event system!");
        }

        try {
            initLogfiles();
            initConfig();
        } catch (final IOException e) {
            System.err.println("Failed to setup logging system!");
            e.printStackTrace(System.err);
        }

        players = new Players();
        connectionMonitor = new ConnectionMonitor();

        SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                try {
                    SubstanceLookAndFeel.setSkin(new OfficeBlack2007Skin());
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    System.setProperty("awt.useSystemAAFontSettings", "on");
                    System.setProperty("swing.aatext", "true");
                    /*for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }*/
                } catch (final Exception e) {
                    // nothing
                }

                final List<String> serverList = new ArrayList<String>();
                for (@Nonnull final Server server : Server.values()) {
                    serverList.add(server.getServerNameEnglish());
                }
                final JXLoginPane loginPane = new JXLoginPane(new ServerLoginService(configSystem),
                        new PasswordStorage(configSystem), new UserNameStorage(configSystem), serverList);
                final JXLoginPane.JXLoginDialog loginDialog = new JXLoginPane.JXLoginDialog((Frame) null, loginPane);
                loginDialog.setTitle(loginDialog.getTitle() + " - " + APPLICATION + ' ' + VERSION);
                loginDialog.setVisible(true);

                System.out.println("Startup done.");

                configSystem.save();
            }
        });
    }

    @Nonnull
    public static ConnectionMonitor getConnectionMonitor() {
        return connectionMonitor;
    }

    /**
     * Basic initialization of the log files and the debug settings.
     */
    @SuppressWarnings("nls")
    private static void initLogfiles() throws IOException {
        final Properties tempProps = new Properties();
        tempProps.load(getResource("logging.properties"));
        tempProps.put("log4j.appender.IllaLogfileAppender.file", getFile("error.log"));
        tempProps.put("log4j.appender.ChatAppender.file", getFile("illarion.log"));
        tempProps.put("log4j.reset", "true");
        new PropertyConfigurator().doConfigure(tempProps, LOGGER.getLoggerRepository());

        Thread.setDefaultUncaughtExceptionHandler(DefaultCrashHandler.getInstance());

        System.out.println("Startup done.");
        LOGGER.info(getVersionText() + " started.");
        LOGGER.info("VM: " + System.getProperty("java.version"));
        LOGGER.info("OS: " + System.getProperty("os.name") + ' ' + System.getProperty("os.version") +
                ' ' + System.getProperty("os.arch"));

        java.util.logging.Logger.getAnonymousLogger().getParent().setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("javolution").setLevel(Level.SEVERE);
        JavaLogToLog4J.setup();
        StdOutToLog4J.setup();
    }

    @Nullable
    private static MainFrame mainGui;

    @SuppressWarnings("NullableProblems")
    public static void setMainGui(@Nonnull final MainFrame frame) {
        mainGui = frame;
    }

    public static void disconnect(final int reason) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (reason > 0) {
                    JOptionPane.showMessageDialog(mainGui, "Disconnected from server.", "Disconnected", JOptionPane.ERROR_MESSAGE);
                }
                if (mainGui != null) {
                    mainGui.dispose();
                }
            }
        });
    }

    @Nonnull
    public static Players getPlayers() {
        if (players == null) {
            throw new IllegalStateException("Requested player list before it was created");
        }
        return players;
    }

    /**
     * Get the full path to a file. This includes the default path that was set up and the name of the file this
     * function gets.
     *
     * @param name the name of the file that shall be append to the folder
     * @return the full path to a file
     */
    public static String getFile(final String name) {
        return new File(DirectoryManager.getInstance().getUserDirectory(), name).getAbsolutePath();
    }

    /**
     * Load a resource as stream via the basic class loader.
     *
     * @param path the path to the object that shall be loaded
     * @return the data stream of the object
     */
    public static InputStream getResource(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Get a text that identifies the version of this BBIWI tool.
     *
     * @return the version text of this BBIWI tool
     */
    public static String getVersionText() {
        return APPLICATION + ' ' + VERSION;
    }
}
