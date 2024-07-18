package forestry.api.genetics;

import com.google.common.collect.ImmutableList;

import net.minecraft.network.chat.MutableComponent;

import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IFloatChromosome;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;

/**
 * Holds the {@link AllelePair}s which comprise the traits of a given individual.
 */
public interface IGenome {

	/**
	 * @return A array with all chromosomes of this genome.
	 */
	ImmutableList<AllelePair<?>> getAllelePairs();

	/**
	 * @return The active allele of the chromosome with the given type.
	 */
	<A extends IAllele> A getActiveAllele(IChromosome<A> chromosome);

	<V> IValueAllele<V> getActiveAllele(IValueChromosome<V> chromosome);

	<A extends IAllele> A getActiveAllele(IChromosome<A> chromosome, Class<? extends A> alleleClass);

	<A extends IAllele> A getActiveAllele(IChromosome<A> chromosome, Class<? extends A> alleleClass, A fallback);

	/**
	 * @return The value of the active allele of the chromosome with the given type.
	 */
	<V> V getActiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass);

	/**
	 * @return The value of the active allele of the chromosome with the given type.
	 */
	<V> V getActiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass, V fallback);

	default float getActiveValue(IFloatChromosome chromosome) {
		return getActiveAllele(chromosome).value();
	}

	default boolean getActiveValue(IBooleanChromosome chromosome) {
		return getActiveAllele(chromosome).value();
	}

	default int getActiveValue(IIntegerChromosome chromosome) {
		return getActiveAllele(chromosome).value();
	}

	default <V> V getActiveValue(IValueChromosome<V> chromosome) {
		return getActiveAllele(chromosome).value();
	}

	/**
	 * @return The active allele of the chromosome with the given type.
	 */
	<A extends IAllele> A getInactiveAllele(IChromosome<A> chromosome);

	<V> IValueAllele<V> getInactiveAllele(IValueChromosome<V> chromosome);

	<A extends IAllele> A getInactiveAllele(IChromosome<A> chromosome, Class<? extends A> alleleClass);

	<A extends IAllele> A getInactiveAllele(IChromosome<A> chromosome, Class<? extends A> alleleClass, A fallback);

	/**
	 * @return The value of the active allele of the chromosome with the given type.
	 */
	<V> V getInactiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass);

	/**
	 * @return The value of the active allele of the chromosome with the given type.
	 */
	<V> V getInactiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass, V fallback);

	default float getInactiveValue(IFloatChromosome chromosome) {
		return getInactiveAllele(chromosome).value();
	}

	default boolean getInactiveValue(IBooleanChromosome chromosome) {
		return getInactiveAllele(chromosome).value();
	}

	default int getInactiveValue(IIntegerChromosome chromosome) {
		return getInactiveAllele(chromosome).value();
	}

	default <V> V getInactiveValue(IValueChromosome<V> chromosome) {
		return getInactiveAllele(chromosome).value();
	}

	/**
	 * @return The chromosome with the given type.
	 */
	<A extends IAllele> AllelePair<A> getChromosome(IChromosome<A> chromosomeType);

	@Override
	boolean equals(Object other);

	/**
	 * @return The karyotype of this genome. It defines the positions of the chromosomes in the array and the length
	 * of it.
	 */
	IKaryotype getKaryotype();

	int size();

	default MutableComponent getActiveName(IFloatChromosome chromosome) {
		return chromosome.getDisplayName(getActiveAllele(chromosome));
	}

	default MutableComponent getActiveName(IBooleanChromosome chromosome) {
		return chromosome.getDisplayName(getActiveAllele(chromosome));
	}

	default MutableComponent getActiveName(IIntegerChromosome chromosome) {
		return chromosome.getDisplayName(getActiveAllele(chromosome));
	}

	default <V> MutableComponent getActiveName(IValueChromosome<V> chromosome) {
		return chromosome.getDisplayName(getActiveAllele(chromosome));
	}

	default MutableComponent getInactiveName(IFloatChromosome chromosome) {
		return chromosome.getDisplayName(getInactiveAllele(chromosome));
	}

	default MutableComponent getInactiveName(IBooleanChromosome chromosome) {
		return chromosome.getDisplayName(getInactiveAllele(chromosome));
	}

	default MutableComponent getInactiveName(IIntegerChromosome chromosome) {
		return chromosome.getDisplayName(getInactiveAllele(chromosome));
	}

	default <V> MutableComponent getInactiveName(IValueChromosome<V> chromosome) {
		return chromosome.getDisplayName(getInactiveAllele(chromosome));
	}
}
