package forestry.core.climate;

import forestry.api.climate.IClimateProvider;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

public enum FakeClimateProvider implements IClimateProvider {
	INSTANCE;

	@Override
	public TemperatureType temperature() {
		return TemperatureType.NORMAL;
	}

	@Override
	public HumidityType humidity() {
		return HumidityType.NORMAL;
	}
}
