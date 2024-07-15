package genetics.api.root;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.ISpeciesType;

import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IIndividual;

public interface IIndividualRootHelper {
	/**
	 * Retrieve a matching {@link IRootDefinition} for the given itemstack.
	 *
	 * @param stack An itemstack possibly containing NBT data which can be converted by a species root.
	 * @return {@link IRootDefinition} if found, empty otherwise.
	 */
	@Nullable
	<R extends ISpeciesType<?>> R getSpeciesRoot(ItemStack stack);

	@Nullable
	<R extends ISpeciesType<?>> R getSpeciesRoot(ItemStack stack, Class<? extends R> rootClass);

	/**
	 * Retrieve a matching {@link IRootDefinition} for the given {@link IIndividual}-class.
	 *
	 * @param individualClass Class extending {@link IIndividual}.
	 * @return {@link IRootDefinition} if found, null otherwise.
	 */
	<R extends ISpeciesType<I>, I extends IIndividual> R getSpeciesRoot(Class<I> individualClass);

	<R extends ISpeciesType> R getSpeciesRoot(Class<? extends IIndividual> individualClass, Class<? extends R> rootClass);

	/**
	 * Retrieve a matching {@link IRootDefinition} for the given {@link IIndividual}
	 */
	<R extends ISpeciesType<?>> R getSpeciesRoot(IIndividual individual);

	<R extends ISpeciesType> R getSpeciesRoot(IIndividual individual, Class<? extends R> rootClass);

	boolean isIndividual(ItemStack stack);

	@Nullable
	IIndividual getIndividual(ItemStack stack);

	IAlleleTemplateBuilder createTemplate(String uid);
}
