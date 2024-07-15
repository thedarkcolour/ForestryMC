package forestry.api.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

/**
 * For use in {@link IValueAllele} to determine identity of a value. Primitives are built-in and do not require this.
 */
public interface INamedValue {
	/**
	 * @return The value to be returned in {@link IValueAllele#id()}
	 */
	ResourceLocation id();
}
