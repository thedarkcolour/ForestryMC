/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.farming;

import java.util.Collection;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface IFarmListener {
	/**
	 * Called before a crop is harvested.
	 *
	 * @param crop ICrop about to be harvested.
	 * @return true to cancel further processing of this crop.
	 */
	default boolean beforeCropHarvest(ICrop crop) {
		return false;
	}

	/**
	 * Called after a crop has been harvested, but before harvested items are stowed in the farms inventory.
	 *
	 * @param harvested Collection of harvested stacks. May be manipulated. Ensure removal of stacks with 0 or less items!
	 * @param crop      Harvested {@link ICrop}
	 */
	default void afterCropHarvest(List<ItemStack> harvested, ICrop crop) {
	}

	/**
	 * Called after the stack of collected items has been returned by the farm logic, but before it is added to the farm's pending queue.
	 *
	 * @param collected Collection of collected stacks. May be manipulated. Ensure removal of stacks with 0 or less items!
	 */
	default void hasCollected(List<ItemStack> collected, IFarmLogic logic) {
	}

	/**
	 * Called after farmland has successfully been cultivated by a farm logic.
	 */
	default void hasCultivated(IFarmLogic logic, BlockPos pos, Direction direction, int extent) {
	}

	/**
	 * Called after the stack of harvested crops has been returned by the farm logic, but before it is added to the farm's pending queue.
	 */
	default void hasScheduledHarvest(Collection<ICrop> harvested, IFarmLogic logic, BlockPos pos, Direction direction, int extent) {
	}

	/**
	 * Can be used to cancel farm task on a per side/{@link IFarmLogic} basis.
	 *
	 * @return true to skip any work action on the given logic and direction for this work cycle.
	 */
	default boolean cancelTask(IFarmLogic logic, Direction direction) {
		return false;
	}
}
