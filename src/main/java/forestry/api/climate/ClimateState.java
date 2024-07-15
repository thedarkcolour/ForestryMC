package forestry.api.climate;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Data object for handling a climate's temperature and humidity.
 */
public record ClimateState(TemperatureType temperature, HumidityType humidity) implements IClimatised {
	public ClimateState(float baseTemperature, float downfall) {
		this(TemperatureType.getFromValue(baseTemperature), HumidityType.getFromValue(downfall));
	}

	public ClimateState(IClimatised climatised) {
		this(climatised.temperature(), climatised.humidity());
	}

	public CompoundTag writeToNbt() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("temperature", this.temperature.ordinal());
		nbt.putInt("humidity", this.humidity.ordinal());
		return nbt;
	}

	@Nullable
	public static ClimateState readFromNbt(CompoundTag nbt) {
		if (!nbt.contains("temperature") || !nbt.contains("humidity")) {
			return null;
		} else {
			return new ClimateState(TemperatureType.VALUES.get(nbt.getInt("temperature")), HumidityType.VALUES.get(nbt.getInt("humidity")));
		}
	}
}
