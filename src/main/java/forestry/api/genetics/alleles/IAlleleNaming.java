package forestry.api.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

/**
 * Used to generate names for values when registering value alleles.
 */
public interface IAlleleNaming<V> {
	/**
	 * @return The name of the allele that should contain this value.
	 * Generally, dominant alleles have a "d" suffix to differentiate them from recessive alleles.
	 * Alleles that wrap equal values, like two instances of the same string value, should return
	 * the same allele, even if the identities of the values are different.
	 */
	ResourceLocation getName(V value, boolean dominant);
}
