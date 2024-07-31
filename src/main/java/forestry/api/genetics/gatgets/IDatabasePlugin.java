/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics.gatgets;

import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.ISpecies;

/**
 * An ISpeciesPlugin provides methods that are used in the alyzer and database to display information about an individual.
 */
public interface IDatabasePlugin {
	List<String> getHints();

	IDatabaseTab<?>[] getTabs();

	Map<ISpecies<?>, ItemStack> getIndividualStacks();
}
