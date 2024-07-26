package forestry.api.genetics;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

/**
 * The replacement for the old ISpeciesType class. Denotes different forms of the same species (ex. larvae, princess, queen, drone)
 *
 * @see forestry.api.apiculture.genetics.BeeLifeStage
 * @see forestry.api.arboriculture.genetics.TreeLifeStage
 * @see forestry.api.lepidopterology.genetics.ButterflyLifeStage
 */
public interface ILifeStage extends StringRepresentable {
	/**
	 * @return The simple name of this life stage (ex. {@code "drone"}), used in commands and item naming.
	 */
	@Override
	String getSerializedName();

	/**
	 * @return The item form of this life stage. Every life stage is associated with an item form.
	 */
	Item getItemForm();

	/**
	 * @return The ordinal used for reading/writing to NBT.
	 */
	int ordinal();
}
