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

import forestry.api.climate.IClimateProvider;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.IGenome;

import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutationCondition;

public class MutationConditionTemperature implements IMutationCondition {
	private final TemperatureType minTemperature;
	private final TemperatureType maxTemperature;

	public MutationConditionTemperature(TemperatureType minTemperature, TemperatureType maxTemperature) {
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
	}

	@Override
	public float getChance(Level level, BlockPos pos, ISpecies<?> allele0, ISpecies<?> allele1, IGenome genome0, IGenome genome1, IClimateProvider climate) {
		TemperatureType biomeTemperature = climate.temperature();

		if (biomeTemperature.ordinal() < minTemperature.ordinal() || biomeTemperature.ordinal() > maxTemperature.ordinal()) {
			return 0;
		}
		return 1;
	}

	@Override
	public Component getDescription() {
		Component minString = ClimateHelper.toDisplay(minTemperature);

		if (minTemperature != maxTemperature) {
			Component maxString = ClimateHelper.toDisplay(maxTemperature);
			return Component.translatable("for.mutation.condition.temperature.range", minString, maxString);
		} else {
			return Component.translatable("for.mutation.condition.temperature.single", minString);
		}
	}
}
