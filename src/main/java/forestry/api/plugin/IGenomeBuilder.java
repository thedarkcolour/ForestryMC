package forestry.api.plugin;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;
import forestry.api.genetics.alleles.IChromosome;

/**
 * Used to create a genome.
 */
public interface IGenomeBuilder {
	/**
	 * Shortcut for setting dominant true/false alleles.
	 */
	default void set(IBooleanChromosome chromosome, boolean defaultAllele) {
		set(chromosome, defaultAllele ? ForestryAlleles.TRUE : ForestryAlleles.FALSE);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	default void setUnchecked(IChromosome chromosome, AllelePair allele) {
		this.setActive(chromosome, allele.active());
		this.setInactive(chromosome, allele.inactive());
	}

	/**
	 * Sets both the active and inactive allele for the chromosome in this genome.
	 *
	 * @param chromosome The chromosome to set.
	 * @param allele     The allele for this chromosome.
	 * @throws IllegalArgumentException If the chromosome is not contained in this genome, or if the
	 *                                  given allele is not valid for the given chromosome.
	 */
	<A extends IAllele> void set(IChromosome<A> chromosome, A allele);

	/**
	 * Sets the active allele for the chromosome in this genome.
	 * Active alleles have the most influence over the actual traits of an individual, also called the phenotype.
	 *
	 * @param chromosome The chromosome to set.
	 * @param allele     The active allele for this chromosome.
	 * @throws IllegalArgumentException If the chromosome is not contained in this genome, or if the
	 *                                  given allele is not valid for the given chromosome.
	 */
	<A extends IAllele> void setActive(IChromosome<A> chromosome, A allele);


	/**
	 * Sets the inactive (non-expressed) allele for the chromosome in this genome.
	 * Inactive alleles have little to no influence on an individual, but are still important for inheritance/breeding.
	 *
	 * @param chromosome The chromosome to set.
	 * @param allele     The active allele for this chromosome.
	 * @throws IllegalArgumentException If the chromosome is not contained in this genome, or if the
	 *                                  given allele is not valid for the given chromosome.
	 */
	<A extends IAllele> void setInactive(IChromosome<A> chromosome, A allele);

	IGenome build();

	boolean isEmpty();
}
