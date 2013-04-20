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
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.*;

/**
 * This is the main frame ob the BBIWI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MainFrame extends JXFrame {
    private final JXList playerList;

    public MainFrame() {
        super(BBIWI.APPLICATION + ' ' + BBIWI.VERSION);

        playerList = new JXList(new PlayerListModel());
        playerList.setCellRenderer(new DefaultListRenderer(new PlayerComponentProvider()));
        getContentPane().add(new JScrollPane(playerList));

        final Action displayPlayerWindowAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int index = playerList.getSelectedIndex();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (index >= 0) {
                            new PlayerDetailsFrame(BBIWI.getPlayers().getOnlinePlayer(index)).setVisible(true);
                        }
                    }
                });
            }
        };

        final KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        playerList.getInputMap().put(enterStroke, enterStroke);
        playerList.getActionMap().put(enterStroke, displayPlayerWindowAction);
        playerList.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final ActionEvent event = new ActionEvent(playerList, ActionEvent.ACTION_PERFORMED, "");
                    displayPlayerWindowAction.actionPerformed(event);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                BBIWI.getConnectionMonitor().reportDisconnect(0);
                dispose();
            }
        });

        pack();
    }
}
