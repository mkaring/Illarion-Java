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

import javax.swing.*;
import java.awt.*;

/**
 * This panel holds the information entry of a single character.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerListPanel extends JPanel {
    private final JLabel nameLabel;
    private final JLabel hitPointsLabel;
    private final JLabel locationLabel;

    public PlayerListPanel() {
        super(new BorderLayout());
        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getSize2D() + 2.f));
        hitPointsLabel = new JLabel();
        locationLabel = new JLabel();

        add(nameLabel, BorderLayout.CENTER);

        final JPanel secondLine = new JPanel(new GridLayout(1, 2));
        secondLine.setOpaque(false);
        add(secondLine, BorderLayout.SOUTH);

        {
            final JPanel hitPointPanel = new JPanel(new BorderLayout());
            secondLine.add(hitPointPanel);
            hitPointPanel.setOpaque(false);
            hitPointPanel.add(new JLabel("HP:"), BorderLayout.WEST);
            hitPointPanel.add(hitPointsLabel, BorderLayout.CENTER);
        }

        final JPanel locationPanel = new JPanel(new BorderLayout());
        secondLine.add(locationPanel);
        locationPanel.setOpaque(false);
        locationPanel.add(new JLabel("Location:"), BorderLayout.WEST);
        locationPanel.add(locationLabel, BorderLayout.CENTER);
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getHitPointsLabel() {
        return hitPointsLabel;
    }

    public JLabel getLocationLabel() {
        return locationLabel;
    }
}
