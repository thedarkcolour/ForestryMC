/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import forestry.api.climate.IClimateProvider;

public interface IMutationCondition {
	/**
	 * Used to modify the chance of a mutation based on certain conditions being met.
	 * Most conditions will either return the current chance or {@code 0.0f} if the condition is not met.
	 *
	 * @param level         The world.
	 * @param pos           The position where this mutation is taking place.
	 * @param mutation      The mutation.
	 * @param firstGenome   The genome of one parent in the mutation. Order of genomes does not necessarily match {@code mutation}.
	 * @param secondGenome  The genome of the other parent in the mutation. Order of genomes does not necessarily match {@code mutation}.
	 * @param climate       The climate in which this mutation is taking place.
	 * @param currentChance The current chance. Starts out as the base chance of the mutation, but may be modified by other {@link IMutationCondition}.
	 * @return The new mutation chance. Usually {@code currentChance} if the condition is met, {@code 0.0f} if it is not.
	 */
	float modifyChance(Level level, BlockPos pos, IMutation<?> mutation, IGenome firstGenome, IGenome secondGenome, IClimateProvider climate, float currentChance);

	/**
	 * A localized description of the mutation condition. (i.e. "A temperature of HOT is required.")
	 */
	Component getDescription();
}
