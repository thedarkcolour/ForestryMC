package forestry.api.plugin;

import java.util.function.Consumer;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Customize properties shared by all species types. Implement this class for your species type registration.
 */
public interface ISpeciesBuilder<B extends ISpeciesBuilder<B>> {
	/**
	 * Overrides whether the allele of this species is dominant. Usually set first by a "registerSpecies" method parameter.
	 */
	B setDominant(boolean dominant);

	/**
	 * Customize the default alleles for each chromosome in the karyotype..
	 * Called after {@link ITaxonBuilder#setDefaultChromosome} which in turn is called after {@link ISpeciesTypeBuilder#setKaryotype}.
	 */
	B setGenome(Consumer<IGenomeBuilder> karyotype);

	/**
	 * Define mutations that mutate into this species.
	 */
	B addMutations(Consumer<IMutationsRegistration> mutations);

	/**
	 * Specify whether item forms of this species will have an enchantment glint.
	 * Usually for rare/milestone species like Steadfast or Industrious.
	 * For trees and butterflies, only applies to their item forms, not their block/entity forms.
	 */
	B setGlint(boolean glint);

	/**
	 * Sets the ideal temperature preference of this species.
	 * All species types in base Forestry use climate information.
	 * If a species type from an addon doesn't, this method is does nothing.
	 */
	B setTemperature(TemperatureType temperature);

	/**
	 * Sets the ideal humidity preference of this species.
	 * All species types in base Forestry use climate information.
	 * If a species type from an addon doesn't, this method is does nothing.
	 */
	B setHumidity(HumidityType humidity);

	/**
	 * Overrides the default complexity of this species, a number between 1 and 10 used by the Escritoire
	 * to determine how many cells the memory game has when analyzing a specimen of this species.
	 * By default, complexity is determined by the number of breeding steps required to breed this species.
	 */
	B setComplexity(int complexity);
}
