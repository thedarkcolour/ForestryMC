package forestry.api.genetics.alleles;

/**
 * An allele that wraps a value that isn't one of the primitive types. Values contained in these alleles are NEVER null.
 *
 * @param <V> The type of value that this allele contains.
 * @see IIntegerChromosome
 * @see IFloatChromosome
 * @see IBooleanChromosome
 */
public non-sealed interface IValueAllele<V> extends IAllele {
	/**
	 * @return The non-null value that this allele contains.
	 */
	V value();
}
