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
package forestry.core.utils;

import java.util.Calendar;

import net.minecraft.network.chat.Component;

/**
 * Fed up with Date and Calendar and their shenanigans
 */
public record DayMonth(int day, int month) {
	public static DayMonth now() {
		Calendar calendar = Calendar.getInstance();
		return new DayMonth(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
	}

	public boolean between(DayMonth start, DayMonth end) {
		if (equals(start) || equals(end)) {
			return true;
		}
		if (start.month > end.month) {
			return after(start) || before(end);
		}
		return after(start) && before(end);
	}

	public boolean before(DayMonth other) {
		if (other.month > this.month) {
			return true;
		}

		if (other.month < this.month) {
			return false;
		}

		return this.day < other.day;
	}

	public boolean after(DayMonth other) {
		if (other.month < this.month) {
			return true;
		}

		if (other.month > this.month) {
			return false;
		}

		return this.day > other.day;
	}

	public Component getDisplayName() {
		return Component.translatable("forestry.date.month." + this.month, this.day);
	}
}
