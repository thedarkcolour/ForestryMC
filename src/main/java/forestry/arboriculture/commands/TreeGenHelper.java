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
package forestry.arboriculture.commands;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.core.utils.BlockUtil;
import forestry.core.worldgen.FeatureBase;

public class TreeGenHelper {
	public static boolean generateTree(ITreeSpecies tree, WorldGenLevel world, BlockPos pos) {
		Feature<NoneFeatureConfiguration> gen = tree.getGenerator().getTreeFeature(tree);

		BlockState blockState = world.getBlockState(pos);
		if (BlockUtil.canPlaceTree(blockState, world, pos)) {
			if (gen instanceof FeatureBase) {
				return ((FeatureBase) gen).place(world, world.getRandom(), pos, true);
			} else {
				return gen.place(new FeaturePlaceContext<>(Optional.empty(), world, ((ServerChunkCache) world.getChunkSource()).getGenerator(), world.getRandom(), pos, FeatureConfiguration.NONE));
			}
		}
		return false;
	}
}
