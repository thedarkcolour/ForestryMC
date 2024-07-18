package forestry.api.plugin;

import java.util.function.Consumer;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;
import forestry.api.genetics.alleles.IChromosome;

/**
 * Builder for a taxon that allows defining subtaxa and adding species to a taxon.
 */
public interface ITaxonBuilder {
	/**
	 * Defines a taxon with this taxon as the its parent.
	 * Do not use with species names, species are set by the {@link ISpeciesBuilder}.
	 *
	 * @param name The name of the taxon. Must be unique.
	 * @throws UnsupportedOperationException If this taxon is a genus. Species names are set by the species builder.
	 */
	void defineSubTaxon(String name);

	/**
	 * Defines and configures a taxon with this taxon as its parent.
	 *
	 * @param name   The name of the taxon. Must be unique.
	 * @param action A consumer that adds additional information to the taxon after it is created.
	 */
	void defineSubTaxon(String name, Consumer<ITaxonBuilder> action);

	/**
	 * Sets the default allele for the given chromosome for all members of this taxon.
	 *
	 * @param chromosome The chromosome to set.
	 * @param value      The default allele of the chromosome.
	 *
	 * @throws UnsupportedOperationException If a member of this taxon does not have this chromosome.
	 */
	default <A extends IAllele> void setDefaultChromosome(IChromosome<A> chromosome, A value) {
		setDefaultChromosome(chromosome, value, true);
	}

	/**
	 * Sets the default allele for the given chromosome for all members of this taxon.
	 *
	 * @param chromosome The chromosome to set.
	 * @param value The default allele of this chromosome.
	 * @param required If true, throws an exception if a member of the taxon does not have this chromosome.
	 *
	 * @throws UnsupportedOperationException If required is {@code true} and a member of this taxon does not have this chromosome.
	 */
	<A extends IAllele> void setDefaultChromosome(IChromosome<A> chromosome, A value, boolean required);
}
