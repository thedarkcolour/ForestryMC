/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.plugin;

import java.time.Month;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutationCondition;

/**
 * Set custom mutation requirements
 */
public interface IMutationBuilder {
	/**
	 * Require a specific temperature for this mutation to occur
	 */
	IMutationBuilder restrictTemperature(TemperatureType temperature);

	/**
	 * Require a range of temperatures for this mutation to occur.
	 */
	IMutationBuilder restrictTemperature(TemperatureType minTemperature, TemperatureType maxTemperature);

	/**
	 * Require a specific humidity for this mutation to occur.
	 */
	IMutationBuilder restrictHumidity(HumidityType humidity);

	/**
	 * Require a range of humidities for this mutation to occur.
	 */
	IMutationBuilder restrictHumidity(HumidityType minHumidity, HumidityType maxHumidity);

	/**
	 * Restrict this mutation to certain types of biomes.
	 */
	IMutationBuilder restrictBiomeType(TagKey<Biome> types);

	/**
	 * Restrict the days of the year that this mutation can occur.
	 */
	default IMutationBuilder restrictDateRange(Month startMonth, int startDay, Month endMonth, int endDay) {
		return restrictDateRange(startMonth.getValue(), startDay, endMonth.getValue(), endDay);
	}

	/**
	 * Restrict the days of the year that this mutation can occur.
	 */
	IMutationBuilder restrictDateRange(int startMonth, int startDay, int endMonth, int endDay);

	/**
	 * Restrict the time of day that this mutation can occur
	 */
	IMutationBuilder requireDay();

	IMutationBuilder requireNight();

	/**
	 * Require a specific resource to be under the location of the mutation
	 */
	IMutationBuilder requireResource(BlockState... acceptedBlockStates);

	/**
	 * Require some other custom mutation condition.
	 */
	IMutationBuilder addMutationCondition(IMutationCondition condition);

	/**
	 * Override the chance set in {@link forestry.api.plugin.IMutationsRegistration#add}.
	 */
	IMutationBuilder setChance(int chance);
}
