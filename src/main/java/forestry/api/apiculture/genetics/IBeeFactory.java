/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import net.minecraft.resources.ResourceLocation;

public interface IBeeFactory {

	/**
	 * Creates a new bee species.
	 * See IAlleleBeeSpeciesBuilder and IAlleleSpeciesBuilder for adding additional properties to the returned species.
	 *
	 * @return a new bee species allele.
	 */
	IAlleleBeeSpeciesBuilder createSpecies(ResourceLocation id, String speciesIdentifier);

}
