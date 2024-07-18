package forestry.api.genetics.alleles;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.Codec;

import forestry.api.genetics.IGenome;
import forestry.core.genetics.Genome;

/**
 * A karyotype is the set of all chromosomes that make up a species's genome.
 * It also defines what alleles are possible values for each chromosome in the genome.
 * The karyotype also creates a {@link Codec} for genomes of members of this species.
 */
public interface IKaryotype {
	/**
	 * @return All chromosomes types of this IKaryotype.
	 */
	ImmutableList<IChromosome<?>> getChromosomes();

	/**
	 * Checks if this karyotype contains the given type.
	 */
	boolean contains(IChromosome<?> chromosome);

	/**
	 * @return The chromosome that determines this individual's species.
	 */
	ISpeciesChromosome<?> getSpeciesChromosome();

	/**
	 * @return The number of chromosomes in this karyotype.
	 */
	int size();

	/**
	 * Used to get a genome for an individual when the species is not known.
	 * If you do know the species, use {@link forestry.api.genetics.ISpecies#getDefaultGenome} instead.
	 *
	 * @return The default genome for this species type.
	 */
	IGenome getDefaultGenome();

	<A extends IAllele> boolean isAlleleValid(IChromosome<A> chromosome, A allele);

	Codec<Genome> genomeCodec();
}
