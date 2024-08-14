/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

import forestry.arboriculture.ForestryWoodType;
import forestry.arboriculture.VanillaWoodType;

/**
 * Provides easy access to Forestry and Vanilla wood items.
 * Forestry wood blocks have the same block state properties as vanilla ones.
 * Note that all doors are fireproof (even vanilla).
 *
 * @see WoodBlockKind
 * @see ForestryWoodType
 * @see VanillaWoodType
 */
public interface IWoodAccess {
	ItemStack getStack(IWoodType woodType, WoodBlockKind kind, boolean fireproof);

	BlockState getBlock(IWoodType woodType, WoodBlockKind kind, boolean fireproof);

	List<IWoodType> getRegisteredWoodTypes();

	// todo this should probably take a Supplier<BlockState>
	void register(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof, BlockState blockState, Supplier<Item> itemStack);
}
