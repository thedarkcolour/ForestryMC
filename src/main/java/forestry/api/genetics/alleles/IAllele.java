package forestry.api.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import forestry.api.IForestryApi;

/**
 * Alleles represent named values of a {@link IChromosome} in a genome.
 * Create new alleles using {@link IAlleleManager}. Alleles are compared by reference equality.
 * Alleles can be created at any time before all species types have been registered.
 * Registered alleles must be usable in the karyotype of at least one species type or an exception will be thrown.
 */
public sealed interface IAllele permits IBooleanAllele, IFloatAllele, IIntegerAllele, IValueAllele {
	Codec<IAllele> CODEC = IForestryApi.INSTANCE.getAlleleManager().alleleCodec();

	/**
	 * Returns the unique ID of this allele. All alleles share the same registry, so prefixes like "i" or "tree_"
	 * are used to prevent conflicts between different types of alleles.
	 * Dominant alleles must be named differently than recessive alleles of the same value. Usually a "d" suffix is good enough.
	 *
	 * @return Unique ID of this allele.
	 */
	ResourceLocation alleleId();

	/**
	 * @return {@code true} if the allele is dominant, {@code false} otherwise.
	 */
	boolean dominant();

	/**
	 * Helper method to ignore Java generic type restrictions.
	 * Manually casting should be preferred whenever possible, only if absolutely necessary (ex. requires capture of ?)
	 */
	@SuppressWarnings("unchecked")
	default <A extends IAllele> A cast() {
		return (A) this;
	}
}
