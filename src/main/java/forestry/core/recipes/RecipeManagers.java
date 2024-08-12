/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.core.recipes;

// todo implement recipe caching, similar to Ex Deorum's RecipeUtil.java
public class RecipeManagers {
	// Instead of keeping around a list of objects that listen to recipe reloads, which is prone to memory leaks,
	// objects that cache recipe data should keep track of the last recipe reload number to know when data has changed.
	private static int recipeReloads = 0;

	public static int getRecipeReloads() {
		return recipeReloads;
	}

	public static void invalidateCaches() {
		recipeReloads++;
	}
}
