package forestry.api.plugin;

import java.awt.Color;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeJubilance;
import forestry.api.genetics.Product;

/**
 * Builder used to register new bee species and configure already existing ones.
 * Use {@link IApicultureRegistration#registerSpecies} to obtain instances of this class.
 */
public interface IBeeSpeciesBuilder extends ISpeciesBuilder<IBeeSpeciesBuilder> {
	/**
	 * Adds a product to this bee species.
	 *
	 * @param stack  A supplier that creates a new instance of the result.
	 * @param chance A float between 0 and 1. The chance that this product is produced during a single work cycle.
	 */
	IBeeSpeciesBuilder addProduct(Supplier<ItemStack> stack, float chance);

	/**
	 * Adds a specialty to the bee species, a product only produced when the bee is in a jubilant state.
	 *
	 * @param stack  A supplier that creates a new instance of the result.
	 * @param chance A float between 0 and 1. The chance that this product is produced during a single work cycle.
	 */
	IBeeSpeciesBuilder addSpecialty(Supplier<ItemStack> stack, float chance);

	/**
	 * Sets the color of the bee's body. The default is yellow, {@code #ffdc16}, used by most bees.
	 */
	IBeeSpeciesBuilder setBody(Color color);

	/**
	 * Sets the color of the bee's stripes. The default is {@link Color#BLACK}.
	 */
	IBeeSpeciesBuilder setStripes(Color color);

	/**
	 * Overrides the default bee outlines set in {@link IApicultureRegistration#registerSpecies}.
	 */
	IBeeSpeciesBuilder setOutline(Color color);

	/**
	 * Specify the jubilance conditions for this bee species. The default returns true if the bee's ideal temperature and humidity are met.
	 * When {@link IBeeJubilance#isJubilant} returns true, a bee can produce its specialty products.
	 */
	IBeeSpeciesBuilder setJubilance(IBeeJubilance jubilance);

	/**
	 * Specify whether this bee species is nocturnal.
	 */
	IBeeSpeciesBuilder setNocturnal(boolean nocturnal);

	List<Product> buildProducts();

	List<Product> buildSpecialties();

	int getBody();

	int getStripes();

	int getOutline();

	IBeeJubilance getJubilance();

	boolean isNocturnal();
}
