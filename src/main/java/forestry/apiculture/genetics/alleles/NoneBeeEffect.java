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
package forestry.apiculture.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.genetics.IBeeEffect;

import static forestry.api.ForestryConstants.forestry;

public enum NoneBeeEffect implements IBeeEffect {
	INSTANCE;

	public static final ResourceLocation ID = forestry("bee_effect_none");

	@Override
	public ResourceLocation id(boolean dominant) {
		return ID;
	}
}
