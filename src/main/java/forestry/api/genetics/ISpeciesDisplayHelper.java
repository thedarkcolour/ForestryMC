package forestry.api.genetics;

import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.alleles.IAlleleSpecies;
import genetics.api.individual.IIndividual;

@OnlyIn(Dist.CLIENT)
public interface ISpeciesDisplayHelper<I extends IIndividual, S extends IAlleleSpecies> {
	/**
	 * Retrieves a stack that can and should only be used on the client side in a gui.
	 *
	 * @return A empty stack, if the species was not registered before the creation of this handler or if the species is
	 * not a species of the {@link IForestrySpeciesType}.
	 */
	ItemStack getDisplayStack(IAlleleSpecies species, ILifeStage type);

	ItemStack getDisplayStack(IAlleleSpecies species);
}
