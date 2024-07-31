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
import forestry.api.genetics.IMutationCondition;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.IGenome;

public class MutationConditionDaytime implements IMutationCondition {
	private final boolean daytime;

	public MutationConditionDaytime(boolean daytime) {
		this.daytime = daytime;
	}

	@Override
	public float getChance(Level level, BlockPos pos, ISpecies<?> allele0, ISpecies<?> allele1, IGenome genome0, IGenome genome1, IClimateProvider climate) {
		if (level.isDay() == this.daytime) {
			return 1;
		}
		return 0;
	}

	@Override
	public Component getDescription() {
		if (this.daytime) {
			return Component.translatable("for.mutation.condition.daytime.day");
		} else {
			return Component.translatable("for.mutation.condition.daytime.night");
		}
	}
}
