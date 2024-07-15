/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import java.util.Locale;
import java.util.Set;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import forestry.api.core.ForestryError;
import forestry.api.core.HumidityType;
import forestry.api.core.IError;
import forestry.api.core.TemperatureType;
import forestry.api.core.ToleranceType;

public class ClimateHelper {
	public static void addClimateErrorStates(TemperatureType temperature, HumidityType humidity,
			TemperatureType baseTemp, ToleranceType tolTemp,
			HumidityType baseHumid, ToleranceType tolHumid, Set<IError> errorStates) {

		if (!isWithinLimits(temperature, baseTemp, tolTemp)) {
			if (baseTemp.ordinal() > temperature.ordinal()) {
				errorStates.add(ForestryError.TOO_COLD);
			} else {
				errorStates.add(ForestryError.TOO_HOT);
			}
		}

		if (!isWithinLimits(humidity, baseHumid, tolHumid)) {
			if (baseHumid.ordinal() > humidity.ordinal()) {
				errorStates.add(ForestryError.TOO_ARID);
			} else {
				errorStates.add(ForestryError.TOO_HUMID);
			}
		}
	}

	public static int getColor(TemperatureType temperature) {
		return switch (temperature) {
			case ICY -> 0xe6e6fa;
			case COLD -> 0x31698a;
			case NORMAL -> 0xf0e9cc;
			case WARM -> 0xcd9b1d;
			case HOT -> 0xdf512e;
			case HELLISH -> 0x9c433e;
		};
	}

	public static boolean isWithinLimits(TemperatureType temperature, HumidityType humidity, TemperatureType idealTemp, ToleranceType temperatureTolerance, HumidityType idealHumidity, ToleranceType humidityTolerance) {
		return isWithinLimits(temperature, idealTemp, temperatureTolerance) && isWithinLimits(humidity, idealHumidity, humidityTolerance);
	}

	public static boolean isWithinLimits(TemperatureType temperature, TemperatureType idealTemp, ToleranceType tolerance) {
		TemperatureType max = idealTemp.up(tolerance.up);
		TemperatureType min = idealTemp.down(tolerance.down);
		return temperature.isWarmerOrEqual(min) && temperature.isCoolerOrEqual(max);
	}

	public static boolean isWithinLimits(HumidityType humidity, HumidityType idealHumidity, ToleranceType tolerance) {
		HumidityType max = idealHumidity.up(tolerance.up);
		HumidityType min = idealHumidity.down(tolerance.down);
		return humidity.isWetterOrEqual(min) && humidity.isDrierOrEqual(max);
	}

	public static MutableComponent toDisplay(TemperatureType temperature) {
		return Component.translatable("for.gui." + temperature.toString().toLowerCase(Locale.ENGLISH));
	}

	public static MutableComponent toDisplay(HumidityType humidity) {
		return Component.translatable("for.gui." + humidity.toString().toLowerCase(Locale.ENGLISH));
	}
}