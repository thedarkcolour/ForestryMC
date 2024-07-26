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

import java.util.Arrays;
import java.util.Locale;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.climate.IClimateProvider;
import forestry.api.genetics.IMutationCondition;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.IGenome;

// todo separate classes for single biome and tag
public class MutationConditionBiome implements IMutationCondition {
	private final TagKey<Biome> validBiomes;

	public MutationConditionBiome(TagKey<Biome> validBiomes) {
		this.validBiomes = validBiomes;
	}

	@Override
	public float getChance(Level level, BlockPos pos, ISpecies<?> allele0, ISpecies<?> allele1, IGenome genome0, IGenome genome1, IClimateProvider climate) {
		return level.getBiome(pos).is(this.validBiomes) ? 1f : 0f;
	}

	@Override
	public Component getDescription() {
		if (validBiomeTypes.size() > 1) {
			String biomeTypes = Arrays.toString(validBiomeTypes.toArray()).toLowerCase(Locale.ENGLISH);
			return Component.translatable("for.mutation.condition.biome.multiple", biomeTypes);
		} else {
			BiomeCategory firstCategory = validBiomeTypes.iterator().next();
			String biomeType = firstCategory.toString().toLowerCase(Locale.ENGLISH);
			return Component.translatable("for.mutation.condition.biome.single", biomeType);
		}
	}
}
