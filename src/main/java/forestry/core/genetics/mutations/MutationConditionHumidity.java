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
package forestry.core.genetics.mutations;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import forestry.api.climate.ClimateState;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;

import forestry.api.core.HumidityType;
import forestry.api.genetics.IMutationCondition;

public class MutationConditionHumidity implements IMutationCondition {
	private final HumidityType minHumidity;
	private final HumidityType maxHumidity;

	public MutationConditionHumidity(HumidityType minHumidity, HumidityType maxHumidity) {
		this.minHumidity = minHumidity;
		this.maxHumidity = maxHumidity;
	}

	@Override
	public float getChance(Level level, BlockPos pos, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1, ClimateState climate) {
		HumidityType biomeHumidity = climate.humidity();

		if (biomeHumidity.ordinal() < minHumidity.ordinal() || biomeHumidity.ordinal() > maxHumidity.ordinal()) {
			return 0;
		}
		return 1;
	}

	@Override
	public Component getDescription() {
		Component minHumidityString = ClimateHelper.toDisplay(minHumidity);

		if (minHumidity != maxHumidity) {
			Component maxHumidityString = ClimateHelper.toDisplay(maxHumidity);
			return Component.translatable("for.mutation.condition.humidity.range", minHumidityString, maxHumidityString);
		} else {
			return Component.translatable("for.mutation.condition.humidity.single", minHumidityString);
		}
	}
}
