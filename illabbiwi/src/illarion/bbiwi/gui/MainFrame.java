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
package illarion.bbiwi.gui;

import illarion.bbiwi.BBIWI;
import illarion.bbiwi.world.Players;
import illarion.common.net.NetComm;
import illarion.common.net.Server;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This is the main frame ob the BBIWI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MainFrame extends JFrame {
    private final JXList playerList;

    @Nonnull
    private final Players players;

    public MainFrame(final NetComm networkComm, final Server usedServer) {
        super(BBIWI.APPLICATION + ' ' + BBIWI.VERSION);

        players = new Players();

        playerList = new JXList(players);
        playerList.setCellRenderer(new DefaultListRenderer(new PlayerComponentProvider()));
        getContentPane().add(new JScrollPane(playerList));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                networkComm.disconnect();
                dispose();
            }
        });

        pack();
    }
}
