package genetics.api.root;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;

public interface IDisplayHelper<I extends IIndividual> {

	String getLocalizedShortName(IChromosome type);

	String getTranslationKeyShort(IChromosome type);

	String getLocalizedName(IChromosome type);

	String getTranslationKey(IChromosome type);

	/**
	 * Retrieves a stack that can and should only be used on the client side in a gui.
	 *
	 * @return A empty stack, if the species was not registered before the creation of this handler or if the species is
	 * not a species of the {@link ISpeciesType}.
	 */
	ItemStack getDisplayStack(IAlleleSpecies species, ILifeStage type);

	ItemStack getDisplayStack(IAlleleSpecies species);
}
