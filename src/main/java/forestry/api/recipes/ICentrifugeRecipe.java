/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.recipes;

import java.util.List;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import forestry.api.core.Product;

public interface ICentrifugeRecipe extends IForestryRecipe {
	/**
	 * The item for this recipe to match against.
	 **/
	Ingredient getInput();

	/**
	 * The time it takes to process one item. Default is 20.
	 **/
	int getProcessingTime();

	/**
	 * Returns the randomized products from processing one input item.
	 **/
	List<ItemStack> getProducts(RandomSource random);

	/**
	 * Returns a list of all possible products and their estimated probabilities (0.0 to 1.0],
	 * to help mods that display recipes
	 **/
	List<Product> getAllProducts();
}
