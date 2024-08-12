/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.farming;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * IFarmable describes a farmable resource, including its sapling/premature and mature versions, its germling/seeds,
 * and the harvested resources resulting from harvesting and collecting from windfall.
 */
public interface IFarmable {
	/**
	 * @return true if the block at the given location is a "sapling" for this type, i.e. a non-harvestable immature version of the crop.
	 */
	boolean isSaplingAt(Level level, BlockPos pos, BlockState state);

	/**
	 * @return {@link ICrop} if the block at the given location is a harvestable and mature crop, null otherwise.
	 */
	@Nullable
	ICrop getCropAt(Level level, BlockPos pos, BlockState state);

	/**
	 * @return true if the item is a valid germling (plantable sapling, seed, etc.) for this type.
	 */
	boolean isGermling(ItemStack stack);

	/**
	 * Used by JEI to display the germlings of this farmable.
	 *
	 * @param accumulator Accepts new item stacks for germlings.
	 */
	default void addGermlings(Consumer<ItemStack> accumulator) {
	}

	/**
	 * Used by JEI to display the products of this farmable.
	 *
	 * @param accumulator Accepts new item stacks for products.
	 */
	default void addProducts(Consumer<ItemStack> accumulator) {
	}

	/**
	 * @return true if the item is something that can drop from this type without actually being harvested as a crop. (Apples or sapling from decaying leaves.)
	 */
	boolean isWindfall(ItemStack stack);

	/**
	 * Plants a sapling by manipulating the world. The {@link IFarmLogic} should have verified the given location as valid. Called by the {@link IFarmHousing}
	 * which handles resources.
	 *
	 * @return {@code true} if a sapling was planted, false otherwise.
	 */
	boolean plantSaplingAt(Player player, ItemStack germling, Level level, BlockPos pos);

}
