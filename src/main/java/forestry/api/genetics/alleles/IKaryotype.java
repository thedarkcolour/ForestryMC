package forestry.api.genetics.alleles;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

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

	<A extends IAllele> boolean isChromosomeValid(IChromosome<A> chromosome);

	<A extends IAllele> A getDefaultAllele(IChromosome<A> chromosome);

	/**
	 * @return The default species for this species type.
	 */
	ResourceLocation getDefaultSpecies();

	<A extends IAllele> boolean isAlleleValid(IChromosome<A> chromosome, A allele);

	Codec<Genome> getGenomeCodec();

	ImmutableMap<IChromosome<?>, ? extends IAllele> getDefaultAlleles();
}
