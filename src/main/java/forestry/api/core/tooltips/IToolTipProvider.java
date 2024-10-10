/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.api.core.tooltips;

import javax.annotation.Nullable;

public interface IToolTipProvider {
	@Nullable
	ToolTip getToolTip(int mouseX, int mouseY);

	// Not fully implemented
	default boolean isToolTipVisible() {
		return true;
	}

	boolean isHovering(double mouseX, double mouseY);

	default boolean isRelativeToGui() {
		return true;
	}
}
