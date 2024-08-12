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

public class ToolTip extends TextCollection {
	private final long delay;
	private long mouseOverStart;

	public ToolTip() {
		this.delay = 0;
	}

	public ToolTip(int delay) {
		this.delay = delay;
	}

	public void onTick(boolean mouseOver) {
		if (delay == 0) {
			return;
		}
		if (mouseOver) {
			if (this.mouseOverStart == 0) {
				this.mouseOverStart = System.currentTimeMillis();
			}
		} else {
			this.mouseOverStart = 0;
		}
	}

	public boolean isReady() {
		return this.delay == 0 || this.mouseOverStart != 0 && System.currentTimeMillis() - this.mouseOverStart >= this.delay;
	}

	public void refresh() {
	}
}
