package forestry.api.genetics.alleles;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import forestry.api.IForestryApi;

/**
 * In Forestry, a chromosome is a key in the genome that maps to different alleles.
 */
public interface IChromosome<A extends IAllele> {
	/**
	 * @return Unique ID for this chromosome.
	 */
	ResourceLocation id();

	/**
	 * The translation key of this allele, for use in {@link Component#translatable(String)}.
	 */
	String getTranslationKey(A allele);

	default MutableComponent getDisplayName(A allele) {
		return Component.translatable(getTranslationKey(allele));
	}

	Class<?> valueClass();
}
