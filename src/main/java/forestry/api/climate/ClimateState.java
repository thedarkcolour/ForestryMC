package forestry.api.climate;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Data object for handling a climate's temperature and humidity.
 */
public record ClimateState(TemperatureType temperature, HumidityType humidity) implements IClimateProvider {
}
