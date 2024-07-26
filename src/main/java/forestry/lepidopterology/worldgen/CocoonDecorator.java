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
package forestry.lepidopterology.worldgen;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.Forestry;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.core.config.Config;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.ModuleLepidopterology;
import forestry.lepidopterology.features.LepidopterologyBlocks;
import forestry.lepidopterology.tiles.TileCocoon;

public class CocoonDecorator extends Feature<NoneFeatureConfiguration> {
	public CocoonDecorator() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static boolean genCocoon(WorldGenLevel world, RandomSource rand, BlockPos pos, IButterfly butterfly) {
		if (butterfly.getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getRarity() * ModuleLepidopterology
				.getGenerateCocoonsAmount() < rand.nextFloat() * 100.0f) {
			return false;
		}

		TagKey<Biome> spawnBiomes = butterfly.getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getSpawnBiomes();

		if (world.getBiome(pos).is(spawnBiomes)) {
			for (int tries = 0; tries < 4; tries++) {
				int x = pos.getX() + rand.nextInt(16);
				int z = pos.getZ() + rand.nextInt(16);

				if (tryGenCocoon(world, x, z, butterfly)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean tryGenCocoon(WorldGenLevel level, int x, int z, IButterfly butterfly) {
		BlockPos pos = getPosForCocoon(level, x, z);

		if (pos != null) {
			if (isValidLocation(level, pos)) {
				return setCocoon(level, pos, butterfly);
			}
		}

		return false;
	}

	private static boolean setCocoon(WorldGenLevel world, BlockPos pos, IButterfly butterfly) {
		Block cocoonBlock = LepidopterologyBlocks.COCOON_SOLID.block();
		boolean placed = world.setBlock(pos, cocoonBlock.defaultBlockState(), Block.UPDATE_CLIENTS);
		if (!placed) {
			return false;
		}

		BlockState state = world.getBlockState(pos);
		if (cocoonBlock != state.getBlock()) {
			return false;
		}

		TileCocoon cocoon = TileUtil.getTile(world, pos, TileCocoon.class);
		if (cocoon != null) {
			cocoon.setCaterpillar(butterfly);
		} else {
			return false;
		}

		cocoonBlock.onPlace(state, world.getLevel(), pos, cocoonBlock.defaultBlockState(), false);
		world.getLevel().setBlocksDirty(pos, state, cocoonBlock.defaultBlockState());

		if (Config.logCocoonPlacement) {
			Forestry.LOGGER.info("Placed {} at {}", cocoonBlock, pos);
		}

		return true;
	}

	@Nullable
	private static BlockPos getPosForCocoon(WorldGenLevel world, int x, int z) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);
		BlockState state = world.getBlockState(pos);
		if (!state.is(BlockTags.LEAVES)) {
			return null;
		}

		do {
			pos.move(0, -1, 0);
			state = world.getBlockState(pos);
		} while (state.is(BlockTags.LEAVES));

		return pos;
	}

	public static boolean isValidLocation(WorldGenLevel world, BlockPos pos) {
		BlockPos abovePos = pos.above();
		BlockState aboveState = world.getBlockState(abovePos);
		if (!aboveState.is(BlockTags.LEAVES)) {
			return false;
		}

		BlockPos posBelow = pos.below();
		BlockState blockStateBelow = world.getBlockState(posBelow);
		return BlockUtil.canReplace(blockStateBelow, world, posBelow);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		ObjectArrayList<IButterflySpecies> butterflies = new ObjectArrayList<>(SpeciesUtil.BUTTERFLY_TYPE.get().getAllSpecies());

		Util.shuffle(butterflies, context.random());

		for (IButterflySpecies butterfly : butterflies) {
			if (genCocoon(context.level(), context.random(), context.origin(), butterfly.createIndividual())) {
				return true;
			}
		}

		return false;
	}
}
