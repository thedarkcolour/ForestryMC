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

import java.util.List;

import deleteme.Shuffler;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.Forestry;
import forestry.api.IForestryApi;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.core.config.Config;

public class HiveDecorator extends Feature<NoneFeatureConfiguration> {
	public HiveDecorator() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static boolean tryGenHive(WorldGenLevel world, RandomSource rand, int x, int z, Hive hive) {
		final BlockPos hivePos = hive.getPosForHive(world, x, z);

		if (hivePos == null) {
			return false;
		}

		if (!hive.canReplace(world, hivePos)) {
			return false;
		}

		Holder<Biome> biome = world.getBiome(hivePos);
		TemperatureType temperature = IForestryApi.INSTANCE.getClimateManager().getTemperature(biome);
		if (!hive.isGoodTemperature(temperature)) {
			return false;
		}

		if (!hive.isValidLocation(world, hivePos)) {
			return false;
		}

		return setHive(world, rand, hivePos, hive);
	}

	private static boolean setHive(WorldGenLevel world, RandomSource rand, BlockPos pos, Hive hive) {
		BlockState hiveState = hive.getHiveBlockState();
		Block hiveBlock = hiveState.getBlock();
		boolean placed = world.setBlock(pos, hiveState, Block.UPDATE_CLIENTS);
		if (!placed) {
			return false;
		}

		BlockState state = world.getBlockState(pos);
		Block placedBlock = state.getBlock();
		if (!(hiveBlock == placedBlock)) {
			return false;
		}

		hiveBlock.onPlace(state, world.getLevel(), pos, hiveState, false);

		if (!Config.generateBeehivesDebug) {
			hive.postGen(world, rand, pos);
		}

		if (Config.logHivePlacement) {
			//getCoordinatesAsString
			Forestry.LOGGER.info("Placed {} at {}", hive, pos.toShortString());
		}

		return true;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		BlockPos pos = context.origin();

		List<Hive> hives = IForestryApi.INSTANCE.getHiveManager().getHives();

		hives.removeIf(hive -> !(hive.getHiveDescription() == HiveDefinition.FOREST || hive.getHiveDescription() == HiveDefinition.MEADOWS));
		Shuffler.shuffle(hives, rand);

		for (int tries = 0; tries < 10; tries++) {
			Holder<Biome> biome = level.getBiome(pos);
			HumidityType humidity = IForestryApi.INSTANCE.getClimateManager().getHumidity(biome);

			for (Hive hive : hives) {
				if (hive.genChance() * Config.generateBeehivesAmount * hives.size() / 8 >= rand.nextFloat() * 0.5f) {
					if (hive.isGoodBiome(biome) && hive.isGoodHumidity(humidity)) {
						int x = pos.getX() + rand.nextInt(16);
						int z = pos.getZ() + rand.nextInt(16);

						if (tryGenHive(level, rand, x, z, hive)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
