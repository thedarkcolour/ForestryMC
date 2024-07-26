package forestry.api.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import forestry.api.IForestryApi;

/**
 * Alleles represent named values of a {@link IChromosome} in a genome.
 * Create new alleles using {@link IAlleleManager}. Alleles are compared by ==, not .equals().
 */
public sealed interface IAllele permits IBooleanAllele, IFloatAllele, IIntegerAllele, IValueAllele {
	Codec<IAllele> CODEC = IForestryApi.INSTANCE.getAlleleManager().alleleCodec();

	/**
	 * @return Unique ID of this allele. Dominant alleles usually have the "d" suffix. Usually prefixed with type to avoid conflicts.
	 */
	ResourceLocation alleleId();

	/**
	 * @return true if the allele is dominant, false otherwise.
	 */
	boolean dominant();

	@SuppressWarnings("unchecked")
	default <A extends IAllele> A cast() {
		return (A) this;
	}
}
