/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import forestry.api.genetics.IGenome;

public interface IBeeJubilance {
	/**
	 * Returns true when conditions are right to make this species Jubilant.
	 * Jubilant bees can produce their Specialty products.
	 */
	boolean isJubilant(IBeeSpecies species, IGenome genome, IBeeHousing housing);
}
