package forestry.api.genetics.alleles;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;

/**
 * Used to generate names for values when registering value alleles.
 */
public interface IAlleleNaming<V> {
	IAlleleNaming<Vec3i> VEC3I_NAMING = (value, dominant) -> {
		StringBuilder builder = new StringBuilder();
		builder.append(value.getX());
		builder.append('_');
		builder.append(value.getY());
		builder.append('_');
		builder.append(value.getZ());
		if (dominant) {
			builder.append('d');
		}
		return ForestryConstants.forestry(builder.toString());
	};

	/**
	 * @return The name of the allele that should contain this value.
	 * Generally, dominant alleles have a "d" suffix to differentiate them from recessive alleles.
	 * Alleles that wrap equal values, like two instances of the same string value, should return
	 * the same allele, even if the identities of the values are different.
	 */
	ResourceLocation getName(V value, boolean dominant);
}
