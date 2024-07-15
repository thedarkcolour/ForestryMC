package forestry.core.climate;

import javax.annotation.Nullable;
import java.util.HashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateListener;
import forestry.api.climate.IClimateProvider;
import forestry.api.climate.IClimateTransformer;
import forestry.api.core.HumidityType;
import forestry.api.core.ILocatable;
import forestry.api.core.TemperatureType;
import forestry.api.climate.IClimateManager;
import forestry.core.DefaultClimateProvider;

import org.jetbrains.annotations.ApiStatus;

public class ForestryClimateManager implements IClimateManager {
	static final String TEMPERATURE_NBT_KEY = "TEMP";
	static final String HUMIDITY_NBT_KEY = "HUMID";
	static final String MUTABLE_NBT_KEY = "MUTABLE";

	private final HashMap<ResourceKey<Biome>, TemperatureType> temperatures = new HashMap<>();
	private final HashMap<ResourceKey<Biome>, HumidityType> humidities = new HashMap<>();

	@Override
	public TemperatureType getTemperature(Holder<Biome> biome) {
		// avoid Optional creation if possible
		return getTemperature(biome instanceof Holder.Reference<Biome> reference ? reference.key() : biome.unwrapKey().get());
	}

	@Override
	public TemperatureType getTemperature(ResourceKey<Biome> biome) {
		return this.temperatures.get(biome);
	}

	@Override
	public HumidityType getHumidity(Holder<Biome> biome) {
		// avoid Optional creation if possible
		return getHumidity(biome instanceof Holder.Reference<Biome> reference ? reference.key() : biome.unwrapKey().get());
	}

	@Override
	public HumidityType getHumidity(ResourceKey<Biome> biome) {
		return this.humidities.get(biome);
	}

	@Override
	public IClimateProvider getDefaultClimate(Level world, BlockPos pos) {
		return new DefaultClimateProvider(world, pos);
	}

	@Nullable
	@Override
	public ClimateState getState(ServerLevel level, BlockPos pos) {
		return WorldClimateHolder.get(level).getAdjustedState(pos);
	}

	@Override
	public ClimateState getBiomeState(Level level, BlockPos coordinates) {
		Biome biome = level.getBiome(coordinates).value();
		return new ClimateState(biome.getTemperature(coordinates), biome.getDownfall());
	}

	@ApiStatus.Internal
	public void onBiomesReloaded(Registry<Biome> registry) {
		this.temperatures.clear();
		this.humidities.clear();

		// check biome tags. if no temperature/humidity tags are found, then calculate based on temperature/downfall
		registry.holders().forEach(holder -> {
			boolean hasTemperatureTag = false;
			boolean hasHumidityTag = false;

			for (TemperatureType temperature : TemperatureType.VALUES) {
				if (holder.is(temperature.tag)) {
					this.temperatures.put(holder.key(), temperature);
					hasTemperatureTag = true;
					break;
				}
			}
			for (HumidityType humidity : HumidityType.VALUES) {
				if (holder.is(humidity.tag)) {
					this.humidities.put(holder.key(), humidity);
					hasHumidityTag = true;
					break;
				}
			}
			if (!hasTemperatureTag) {
				this.temperatures.put(holder.key(), TemperatureType.getFromValue(holder.value().getBaseTemperature()));
			}
			if (!hasHumidityTag) {
				this.humidities.put(holder.key(), HumidityType.getFromValue(holder.value().getDownfall()));
			}
		});
	}

	@Override
	public IClimateTransformer createTransformer(IClimateHousing housing) {
		return new ClimateTransformer(housing);
	}

	@Override
	public IClimateListener createListener(ILocatable locatable) {
		return new ClimateListener(locatable);
	}
}
