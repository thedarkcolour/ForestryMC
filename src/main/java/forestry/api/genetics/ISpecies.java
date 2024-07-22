package forestry.api.genetics;

import java.util.Map;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;

public interface ISpecies<I extends IIndividual> {
	String getTranslationKey();

	default MutableComponent getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	/**
	 * @return The default genome for this species.
	 */
	IGenome getDefaultGenome();

	/**
	 * @return The unique identifier of this species. This is DIFFERENT than the id of its allele.
	 */
	ResourceLocation id();

	/**
	 * @return The scientific name of this species, including the genus and species.
	 */
	String getBinomial();

	/**
	 * @return The type of species this is.
	 */
	ISpeciesType<? extends ISpecies<I>> getType();

	/**
	 * @return Whether this species is "secret" (Ex. Easter egg bee or extremely rare tree/butterfly)
	 */
	boolean isSecret();

	int getComplexity();

	default ItemStack createStack(I individual, ILifeStage stage) {
		return getType().createStack(individual, stage);
	}

	/**
	 * Creates an individual of this species using the default genome and the added alleles.
	 *
	 * @param alleles A map of alleles to set on this individual upon creation. Any missing chromosomes use default alleles.
	 * @return An individual along with any specified alleles.
	 */
	I createIndividual(Map<IChromosome<?>, IAllele> alleles);

	default I createIndividual() {
		return createIndividual(Map.of());
	}

	default IKaryotype getKaryotype() {
		return getDefaultGenome().getKaryotype();
	}
}
