package forestry.api.genetics.alleles;

/**
 * A registry allele refers to a value registered to some {@link IRegistryChromosome}.
 *
 * @param <V> The type of value contained in this allele.
 */
public interface IRegistryAllele<V extends IRegistryAlleleValue> extends IValueAllele<V> {
	/**
	 * @return The value contained in this registry allele, or {@code null} if it has not been registered yet.
	 */
	@Override
	V value();

	/**
	 * @return The registry chromosome responsible for managing allele values.
	 */
	IRegistryChromosome<V> chromosome();
}
