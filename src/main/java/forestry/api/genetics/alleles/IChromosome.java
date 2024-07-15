package forestry.api.genetics.alleles;

import java.util.Set;

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
	Codec<IChromosome<?>> CODEC = IForestryApi.INSTANCE.getGeneticManager().chromosomeCodec();
	/**
	 * @return A set of alleles that this chromosome can have. Alleles not in this set are not allowed.
	 * @see {@link forestry.api.plugin.IGeneticRegistration#modifyChromosome} to add more values.
	 */
	Set<A> getValidAlleles();

	/**
	 * @return Unique ID for this chromosome.
	 */
	ResourceLocation getId();

	/**
	 * The translation key of this allele, for use in {@link Component#translatable(String)}.
	 */
	String getTranslationKey(A allele);

	default MutableComponent getDisplayName(A allele) {
		return Component.translatable(getTranslationKey(allele));
	}
}
