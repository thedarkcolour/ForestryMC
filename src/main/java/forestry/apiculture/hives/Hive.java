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

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.apiculture.hives.IHive;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

public final class Hive implements IHive {
	private final IHiveDefinition hiveDescription;
	private final List<IHiveDrop> drops;

	public Hive(IHiveDefinition definition, List<IHiveDrop> drops) {
		this.hiveDescription = definition;
		this.drops = drops;
	}

	@Override
	public IHiveDefinition getHiveDescription() {
		return this.hiveDescription;
	}

	@Override
	public BlockState getHiveBlockState() {
		return this.hiveDescription.getBlockState();
	}

	@Override
	public List<IHiveDrop> getDrops() {
		return this.drops;
	}

	@Override
	public float genChance() {
		return this.hiveDescription.getGenChance();
	}

	@Override
	public void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos) {
		this.hiveDescription.postGen(world, rand, pos);
	}

	@Override
	public boolean isGoodBiome(Holder<Biome> biome) {
		return this.hiveDescription.isGoodBiome(biome);
	}

	@Override
	public boolean isGoodHumidity(HumidityType humidity) {
		return this.hiveDescription.isGoodHumidity(humidity);
	}

	@Override
	public boolean isGoodTemperature(TemperatureType temperature) {
		return this.hiveDescription.isGoodTemperature(temperature);
	}

	@Override
	public boolean isValidLocation(WorldGenLevel world, BlockPos pos) {
		return this.hiveDescription.getHiveGen().isValidLocation(world, pos);
	}

	@Override
	public boolean canReplace(WorldGenLevel world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return this.hiveDescription.getHiveGen().canReplace(blockState, world, pos);
	}

	@Override
	@Nullable
	public BlockPos getPosForHive(WorldGenLevel world, int x, int z) {
		return this.hiveDescription.getHiveGen().getPosForHive(world, x, z);
	}

	public void addDrops(List<IHiveDrop> drops) {
		this.drops.addAll(drops);
	}

	@Override
	public String toString() {
		return hiveDescription + " hive";
	}
}
