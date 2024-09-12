package forestry.api.climate;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Data object for storing a climate's temperature and humidity. Does not change with world updates.
 */
public record ClimateState(TemperatureType temperature, HumidityType humidity) implements IClimateProvider {
}
