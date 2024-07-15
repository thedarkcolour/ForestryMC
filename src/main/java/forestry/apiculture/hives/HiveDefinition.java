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
package forestry.apiculture.hives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.ForestryTags;
import forestry.api.IForestryApi;
import forestry.api.apiculture.hives.HiveType;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveGen;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.apiculture.features.ApicultureBlocks;
import forestry.apiculture.genetics.BeeDefinition;

// todo this should be data driven
public enum HiveDefinition implements IHiveDefinition {
	FOREST(HiveType.FOREST, 3.0f, BeeDefinition.FOREST, IForestryApi.INSTANCE.getHiveManager().createTreeGen()) {
		@Override
		public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
			postGenFlowers(world, rand, pos, flowerStates);
		}
	},
	MEADOWS(HiveType.MEADOWS, 1.0f, BeeDefinition.MEADOWS, IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.DIRT, Blocks.GRASS_BLOCK)) {
		@Override
		public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
			postGenFlowers(world, rand, pos, flowerStates);
		}
	},
	DESERT(HiveType.DESERT, 1.0f, BeeDefinition.MODEST, IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.SANDSTONE)) {
		@Override
		public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
			postGenFlowers(world, rand, pos, cactusStates);
		}
	},
	JUNGLE(HiveType.JUNGLE, 6.0f, BeeDefinition.TROPICAL, IForestryApi.INSTANCE.getHiveManager().createTreeGen()),
	END(HiveType.END, 2.0f, BeeDefinition.ENDED, IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.END_STONE, Blocks.END_STONE_BRICKS)) {
		@Override
		public boolean isGoodBiome(Holder<Biome> biome) {
			return biome.is(ForestryTags.Biomes.THE_END_CATEGORY);
		}
	},
	SNOW(HiveType.SNOW, 2.0f, BeeDefinition.WINTRY, IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.DIRT, Blocks.SNOW, Blocks.GRASS_BLOCK)) {
		@Override
		public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
			BlockPos posAbove = pos.above();
			if (world.isEmptyBlock(posAbove)) {
				world.setBlock(posAbove, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);
			}

			postGenFlowers(world, rand, pos, flowerStates);
		}
	},
	SWAMP(HiveType.SWAMP, 2.0f, BeeDefinition.MARSHY, IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.DIRT, Blocks.GRASS_BLOCK)) {
		@Override
		public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
			postGenFlowers(world, rand, pos, mushroomStates);
		}
	};

	private static final IHiveGen FLOWER_GROUND = IForestryApi.INSTANCE.getHiveManager().createGroundGen(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SNOW, Blocks.SAND, Blocks.SANDSTONE);
	private static final List<BlockState> flowerStates = new ArrayList<>();
	private static final List<BlockState> mushroomStates = new ArrayList<>();
	private static final List<BlockState> cactusStates = Collections.singletonList(Blocks.CACTUS.defaultBlockState());

	static {
		flowerStates.addAll(Blocks.POPPY.getStateDefinition().getPossibleStates());
		flowerStates.addAll(Blocks.DANDELION.getStateDefinition().getPossibleStates());
		mushroomStates.add(Blocks.RED_MUSHROOM.defaultBlockState());
		mushroomStates.add(Blocks.BROWN_MUSHROOM.defaultBlockState());
	}

	private final BlockState blockState;
	private final float genChance;
	private final IGenome beeGenome;
	private final IHiveGen hiveGen;
	private final HiveType hiveType;

	HiveDefinition(HiveType hiveType, float genChance, BeeDefinition beeTemplate, IHiveGen hiveGen) {
		this.blockState = ApicultureBlocks.BEEHIVE.get(hiveType).defaultState();
		this.genChance = genChance;
		this.beeGenome = beeTemplate.getGenome();
		this.hiveGen = hiveGen;
		this.hiveType = hiveType;
	}

	@Override
	public IHiveGen getHiveGen() {
		return hiveGen;
	}

	@Override
	public BlockState getBlockState() {
		return blockState;
	}

	@Override
	public boolean isGoodBiome(Holder<Biome> biome) {
		return !biome.is(ForestryTags.Biomes.NETHER_CATEGORY);
	}

	@Override
	public boolean isGoodHumidity(HumidityType humidity) {
		HumidityType idealHumidity = beeGenome.getActiveAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES).getHumidity();
		ToleranceType humidityTolerance = beeGenome.getActiveValue(BeeChromosomes.HUMIDITY_TOLERANCE);
		return ClimateHelper.isWithinLimits(humidity, idealHumidity, humidityTolerance);
	}

	@Override
	public boolean isGoodTemperature(TemperatureType temperature) {
		TemperatureType idealTemperature = beeGenome.getActiveAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES).getTemperature();
		ToleranceType temperatureTolerance = beeGenome.getActiveValue(BeeChromosomes.TEMPERATURE_TOLERANCE);
		return ClimateHelper.isWithinLimits(temperature, idealTemperature, temperatureTolerance);
	}

	@Override
	public float getGenChance() {
		return genChance;
	}

	@Override
	public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
	}

	protected static void postGenFlowers(WorldGenLevel world, RandomSource rand, BlockPos hivePos, List<BlockState> flowerStates) {
		int plantedCount = 0;
		for (int i = 0; i < 10; i++) {
			int xOffset = rand.nextInt(8) - 4;
			int zOffset = rand.nextInt(8) - 4;
			BlockPos blockPos = hivePos.offset(xOffset, 0, zOffset);
			if ((xOffset == 0 && zOffset == 0) || !world.hasChunkAt(blockPos)) {
				continue;
			}

			blockPos = FLOWER_GROUND.getPosForHive(world, blockPos.getX(), blockPos.getZ());
			if (blockPos == null) {
				continue;
			}

			BlockState state = flowerStates.get(rand.nextInt(flowerStates.size()));
			Block block = state.getBlock();
			if (!block.defaultBlockState().canSurvive(world, blockPos)) {
				continue;
			}

			world.setBlock(blockPos, state, Block.UPDATE_CLIENTS);
			plantedCount++;

			if (plantedCount >= 3) {
				break;
			}
		}
	}
}
