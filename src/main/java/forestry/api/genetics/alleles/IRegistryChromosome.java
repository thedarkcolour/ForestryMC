package forestry.api.genetics.alleles;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

/**
 * A chromosome that is backed by a registry of alleles that needs to be populated after karyotypes are created.
 * In a karyotype, it is not possible to restrict what alleles can be used for a registry chromosome.
 * Create an instance using {@link IAlleleManager#registryChromosome(ResourceLocation, Class)}.
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

	/**
	 * @return A collections of all values in this chromosome.
	 */
	Collection<V> values();

	/**
	 * Gets the allele ID of a value in this chromosome and returns it.
	 *
	 * @param value The value.
	 * @return The ID of the allele holding this value.
	 */
	ResourceLocation getId(V value);

	/**
	 * Used to fill the internal registry of this chromosome.
	 *
	 * @param registry The immutable map of all registry values.
	 */
	void populate(ImmutableMap<ResourceLocation, V> registry);

	boolean isPopulated();
}
