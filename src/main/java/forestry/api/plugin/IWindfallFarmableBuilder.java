package forestry.api.plugin;

import net.minecraft.world.item.Item;

import forestry.api.farming.IFarmable;

/**
 * Allows customization of an {@link IFarmable} that has windfall items.
 * Windfall items are items that drop from a farmable but aren't directly harvested by the farm (ex. decaying leaves).
 * Primarily used when adding modded windfall items is necessary. For example, the Arboretum has IFarmable instances
 * for all vanilla tree species, but it does not collect modded leaf drops like the items from Delightful or Twig.
 * This interface exposes the list of windfall items passed to the final IFarmable through {@link IFarmTypeBuilder#modifyWindfallFarmable}.
 */
public interface IWindfallFarmableBuilder {
	/**
	 * Adds a windfall item to this farmable.
	 *
	 * @param windfall The windfall item to add.
	 */
	IWindfallFarmableBuilder addWindfall(Item windfall);

	/**
	 * Adds multiple windfall items to this farmable.
	 *
	 * @param windfall The collection of windfall items to add.
	 */
	IWindfallFarmableBuilder addWindfall(Iterable<? extends Item> windfall);
}
