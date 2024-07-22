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
package forestry.apiculture.genetics;

import java.util.List;

import net.minecraft.world.entity.Mob;

import forestry.api.apiculture.IBeeJubilance;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.genetics.IGenome;

import forestry.api.apiculture.IBeeHousing;

/**
 * Hermits will not produce if there are any other living creatures nearby.
 */
public enum HermitBeeJubilance implements IBeeJubilance {
	INSTANCE;

	@Override
	public boolean isJubilant(IBeeSpecies species, IGenome genome, IBeeHousing housing) {
		List<Mob> list = IBeeEffect.getEntitiesInRange(genome, housing, Mob.class);
		return list.isEmpty();
	}
}
