package forestry.api.genetics.alleles;

/**
 * A registry allele refers to a value registered to some {@link IRegistryChromosome}.
 *
 * @param <V> The type of value contained in this allele.
 */
public interface IRegistryAllele<V extends IRegistryAlleleValue> extends IValueAllele<V> {
	/**
	 * @return The value contained in this registry allele.
	 * @throws IllegalStateException If the value for this allele has not yet been registered to its registry chromosome.
	 */
	@Override
	V value();

	/**
	 * @return The registry chromosome responsible for managing allele values.
	 */
	IRegistryChromosome<V> chromosome();
}
