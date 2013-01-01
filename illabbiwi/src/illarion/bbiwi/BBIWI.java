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
import illarion.common.util.DirectoryManager;
import illarion.common.util.JavaLogToLog4J;
import illarion.common.util.StdOutToLog4J;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.swingx.JXLoginPane;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

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
public final class BBIWI {
    public static final String APPLICATION = "BBIWI Tool";
    public static final String VERSION = "2.0";

    /**
     * The logging instance of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BBIWI.class);

    /**
     * Launch function.
     *
     * @param args launch arguments
     */
    public static void main(final String[] args) {
        try {
            initLogfiles();
        } catch (final IOException e) {
            System.err.println("Failed to setup logging system!");
            e.printStackTrace(System.err);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                try {
                    SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
                } catch (final Exception e) {
                    // nothing
                }

                final List<String> serverList = new ArrayList<String>();
                serverList.add("Illarion Server");
                serverList.add("Test Server");
                final JXLoginPane loginPane = new JXLoginPane(null, null, null, serverList);
                final JXLoginPane.JXLoginDialog loginDialog = new JXLoginPane.JXLoginDialog((Frame) null, loginPane);
                loginDialog.setVisible(true);

                System.out.println("Startup done.");
            }
        });
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
