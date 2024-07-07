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
package forestry.core.genetics;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GenericRatings {
	public static MutableComponent rateActivityTime(boolean neverSleeps, boolean naturalNocturnal) {
		MutableComponent active = naturalNocturnal ? Component.translatable("for.gui.nocturnal") : Component.translatable("for.gui.diurnal");
		if (neverSleeps) {
			active.append(", ").append(naturalNocturnal ? Component.translatable("for.gui.diurnal") : Component.translatable("for.gui.nocturnal"));
		}

		return active;
	}
}
