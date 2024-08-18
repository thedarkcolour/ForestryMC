/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.genetics.IGenome;

/**
 * Implements the tree generation for a tree species.
 */
public interface ITreeGenerator {
	Feature<NoneFeatureConfiguration> getTreeFeature(ITreeGenData tree);

	boolean setLogBlock(IGenome genome, LevelAccessor level, BlockPos pos, Direction facing);

	boolean setLeaves(IGenome genome, LevelAccessor level, BlockPos pos, RandomSource rand);
}
