package forestry.api.apiculture.hives;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

public interface IHive {
	IHiveDefinition getHiveDescription();

	BlockState getHiveBlockState();

	List<IHiveDrop> getDrops();

	float genChance();

	void postGen(WorldGenLevel world, RandomSource rand, BlockPos pos);

	boolean isGoodBiome(Holder<Biome> biome);

	boolean isGoodHumidity(HumidityType humidity);

	boolean isGoodTemperature(TemperatureType temperature);

	boolean isValidLocation(WorldGenLevel world, BlockPos pos);

	boolean canReplace(WorldGenLevel world, BlockPos pos);

	@Nullable
	BlockPos getPosForHive(WorldGenLevel world, int x, int z);

	@Override
	String toString();
}
