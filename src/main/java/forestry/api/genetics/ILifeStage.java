package forestry.api.genetics;

import net.minecraft.world.item.ItemStack;

/**
 * The replacement for the old ISpeciesType class. Denotes different forms of the same species (ex. larvae, princess, queen, drone)
 *
 * @see forestry.api.apiculture.genetics.BeeLifeStage
 * @see forestry.api.arboriculture.genetics.TreeLifeStage
 * @see forestry.api.lepidopterology.genetics.ButterflyLifeStage
 */
public interface ILifeStage {
	/**
	 * @return The lowercase name of this life stage. Used for translations.
	 */
	String getName();

	/**
	 * @return The item form of this life stage. Every life stage is associated with an item form.
	 */
	ItemStack getItemForm();

	/**
	 * @return The ordinal used for reading/writing to NBT.
	 */
	int ordinal();
}
