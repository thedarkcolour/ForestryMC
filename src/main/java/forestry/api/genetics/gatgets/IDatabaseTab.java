package forestry.api.genetics.gatgets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.core.gui.elements.DatabaseElement;

/**
 * A tab of the database screen that shows some information about a {@link IIndividual}.
 */
public interface IDatabaseTab<I extends IIndividual> {
	/**
	 * Creates the gui elements that are displayed if this tab is selected in the database.
	 *
	 * @param container  A helper to create the gui elements.
	 * @param individual The individual that is currently in the database selected.
	 * @param stage
	 */
	void createElements(DatabaseElement container, I individual, ILifeStage stage, ItemStack stack);

	ItemStack getIconStack();

	/**
	 * Can be used to give the tab a custom tooltip.
	 */
	default Component getTooltip(I individual) {
		return Component.empty();
	}

	default DatabaseMode getMode() {
		return DatabaseMode.ACTIVE;
	}
}
