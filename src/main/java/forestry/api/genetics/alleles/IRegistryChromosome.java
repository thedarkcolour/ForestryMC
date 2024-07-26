package forestry.api.genetics.alleles;

import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

/**
 * A chromosome which has values registered to it at appropriate times instead of statically.
 * Each registry chromosome maintains a registry of the values its alleles can represent.
 */
public interface IRegistryChromosome<V extends IRegistryAlleleValue> extends IValueChromosome<V> {
	/**
	 * Returns whether a certain allele is allowed for this chromosome. Overrides {@link IKaryotype#isAlleleValid}.
	 *
	 * @return {@code true} if the given allele is valid in this chromosome. Used by the karyotype.
	 */
	boolean isValidAllele(IAllele allele);

	/**
	 * @return The value with the given ID from the registry this chromosome represents.
	 * @throws IllegalStateException If the registry is not yet populated.
	 */
	V get(ResourceLocation id);

	Collection<V> values();

	ResourceLocation getId(V value);
}
