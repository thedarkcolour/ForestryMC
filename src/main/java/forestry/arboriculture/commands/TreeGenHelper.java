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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.IGenome;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.SpeciesUtil;
import forestry.core.worldgen.FeatureBase;

public class TreeGenHelper {
	public static Feature<NoneFeatureConfiguration> getWorldGen(ResourceLocation treeName, Player player, BlockPos pos) {
		IGenome treeGenome = getTreeGenome(treeName);
		ITree tree = SpeciesUtil.TREE_TYPE.get().getTree(treeGenome);
		return tree.getTreeGenerator((ServerLevel) player.level, pos, true);
	}

	public static <FC extends FeatureConfiguration> boolean generateTree(Feature<FC> feature, ChunkGenerator generator, Level world, BlockPos pos, FC config) {
		if (pos.getY() > 0 && world.isEmptyBlock(pos.below())) {
			pos = BlockUtil.getNextSolidDownPos(world, pos);
		} else {
			pos = BlockUtil.getNextReplaceableUpPos(world, pos);
		}
		if (pos == null) {
			return false;
		}

		BlockState blockState = world.getBlockState(pos);
		if (BlockUtil.canPlaceTree(blockState, world, pos)) {
			if (feature instanceof FeatureBase) {
				return ((FeatureBase) feature).place(world, world.random, pos, true);
			} else {
				return feature.place(new FeaturePlaceContext<>(Optional.empty(), (ServerLevel) world, generator, world.random, pos, config));
			}
		}
		return false;
	}

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

	private static IGenome getTreeGenome(ResourceLocation speciesName) {
		return SpeciesUtil.getTreeSpecies(speciesName).getDefaultGenome();
	}
}
