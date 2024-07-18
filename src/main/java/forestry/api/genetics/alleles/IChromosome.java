package forestry.api.genetics.alleles;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import forestry.api.IForestryApi;

/**
 * In Forestry, a chromosome is a key in the genome that maps to different alleles.
 * Register your chromosomes with {@link forestry.api.plugin.IGeneticRegistration#registerChromosome}.
 */
public interface IChromosome<A extends IAllele> {
	// todo is this circular?
	Codec<IChromosome<?>> CODEC = IForestryApi.INSTANCE.getAlleleManager().chromosomeCodec();

	/**
	 * @return Unique ID for this chromosome.
	 */
	ResourceLocation id();

	/**
	 * The translation key of this allele, for use in {@link Component#translatable(String)}.
	 */
	default String getTranslationKey(A allele) {
		ResourceLocation alleleId = allele.id();
		// ex: allele.forestry.bee_species.meadows
		return "allele." + alleleId.getNamespace() + '.' + id().getPath() + '.' + alleleId.getPath();
	}

	default MutableComponent getDisplayName(A allele) {
		return Component.translatable(getTranslationKey(allele));
	}

	Class<?> valueClass();
}
