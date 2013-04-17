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

import illarion.bbiwi.world.Player;
import illarion.common.data.CharacterAttribute;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayerComponentProvider extends ComponentProvider<PlayerListPanel> {
    @Override
    protected void format(final CellContext context) {
        // nothing
    }

    @Override
    protected void configureState(final CellContext context) {
        rendererComponent.setComponentOrientation(context.getComponent().getComponentOrientation());

        final Player selectedPlayer = (Player) context.getValue();

        rendererComponent.getNameLabel().setText(selectedPlayer.getName());
        rendererComponent.getHitPointsLabel().setText(Integer.toString(selectedPlayer.getAttribute(CharacterAttribute.HitPoints)));
        rendererComponent.getLocationLabel().setText(selectedPlayer.getLocation().getScX() + ", " +
                selectedPlayer.getLocation().getScY() + ", " + selectedPlayer.getLocation().getScZ());
    }

    @Override
    protected PlayerListPanel createRendererComponent() {
        return new PlayerListPanel();
    }
}
