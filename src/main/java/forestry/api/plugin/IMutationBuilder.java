/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.plugin;

import com.google.common.collect.ImmutableMap;

import java.time.Month;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.IMutationCondition;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;

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

	<A extends IAllele> IMutationBuilder addSpecialAllele(IChromosome<A> chromosome, A allele);

	/**
	 * Override the chance set in {@link forestry.api.plugin.IMutationsRegistration#add}.
	 *
	 * @param chance The chance of this mutation occurring between 0 and 1, inclusive.
	 * @throws IllegalArgumentException If chance is not in [0, 1].
	 */
	IMutationBuilder setChance(float chance);

	/**
	 * Builds the mutation. Used internally.
	 *
	 * @param speciesType   Species type of the mutation. Do not call {@link ISpeciesType#getSpecies}.
	 * @param speciesLookup The species by ID lookup, since species type might not have the registry yet.
	 * @return The completed Mutation object with immutable data.
	 */
	<S extends ISpecies<?>> IMutation<S> build(ISpeciesType<S, ?> speciesType, ImmutableMap<ResourceLocation, S> speciesLookup);
}
