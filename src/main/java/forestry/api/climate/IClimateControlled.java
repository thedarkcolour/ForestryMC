/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climate;

/**
 * Used to
 */
public interface IClimateControlled {
	void addTemperatureChange(byte steps);

	void addHumidityChange(byte steps);
}
