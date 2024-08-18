/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.genetics.IGenome;

public interface ITreeGenData {
	int getGirth(IGenome genome);

	float getHeightModifier(IGenome genome);

	/**
	 * @return Position that this tree can grow, or {@code null} if it cannot grow. May be different from pos if there are multiple saplings.
	 */
	@Nullable
	BlockPos getGrowthPos(IGenome genome, LevelAccessor level, BlockPos pos, int expectedGirth, int expectedHeight);

	boolean setLeaves(IGenome genome, LevelAccessor level, BlockPos pos, RandomSource random, boolean convertBlockEntity);

	boolean setLogBlock(IGenome genome, LevelAccessor level, BlockPos pos, Direction facing);

	boolean allowsFruitBlocks(IGenome genome);

	boolean trySpawnFruitBlock(LevelAccessor level, RandomSource rand, BlockPos pos);

	IGenome getDefaultGenome();
}
