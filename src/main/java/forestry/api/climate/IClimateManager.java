package forestry.api.climate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.core.HumidityType;
import forestry.api.core.ILocatable;
import forestry.api.core.TemperatureType;

/**
 * Used to keep track of climate information.
 * Get an instance from {@link forestry.api.IForestryApi}.
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
	 * @return The climate state at the given location.
	 */
	@Nullable
	ClimateState getState(ServerLevel level, BlockPos pos);

	/**
	 * @return The climate of the biome at the given position.
	 */
	ClimateState getBiomeState(Level level, BlockPos pos);

	/**
	 * @return Create a climate provider.
	 */
	IClimateProvider getDefaultClimate(Level level, BlockPos pos);

	/**
	 * @return A new instance of {@link IClimateTransformer}.
	 */
	IClimateTransformer createTransformer(IClimateHousing housing);

	/**
	 * @return A new instance of {@link IClimateListener}.
	 */
	IClimateListener createListener(ILocatable locatable);
}
