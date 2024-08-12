package forestry.api.genetics.alleles;

/**
 * Denotes a value that can be stored in a registry allele.
 */
public interface IRegistryAlleleValue {
	/**
	 * @return Whether the allele for this value is dominant or recessive.
	 */
	boolean isDominant();
}
