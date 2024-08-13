package forestry.api.climate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Used to keep track of climate information.
 * In a future version of Forestry, this will also be used to account for climate modifiers.
 * Get an instance from {@link forestry.api.IForestryApi#getClimateManager}.
 */
public interface IClimateManager {
	/**
	 * @return The temperature of the given biome.
	 */
	TemperatureType getTemperature(Holder<Biome> biome);

	/**
	 * @return The temperature of the given biome.
	 */
	TemperatureType getTemperature(ResourceKey<Biome> biome);

	/**
	 * @return The humidity of the given biome.
	 */
	HumidityType getHumidity(Holder<Biome> holder);

	/**
	 * @return The humidity of the given biome.
	 */
	HumidityType getHumidity(ResourceKey<Biome> holder);

	/**
	 * @return The climate state at the given location with modifications applied. TODO implement IClimateModifier
	 */
	ClimateState getState(ServerLevel level, BlockPos pos);

	/**
	 * @return The unmodified climate of the biome at the given position.
	 */
	ClimateState getBiomeState(LevelReader level, BlockPos pos);

	/**
	 * @return Create a climate provider.
	 */
	IClimateProvider getDefaultClimate(LevelReader level, BlockPos pos);
}
