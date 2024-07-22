/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

import forestry.api.apiculture.IBeeHousing;

public interface IBeeMutation extends IMutation<IBeeSpecies> {
	@Override
	IBeeSpeciesType getType();

	/**
	 * @return float representing the chance for mutation to occur. note that this is 0 - 100 based, since it was an integer previously!
	 */
	float getChance(IBeeHousing housing, IBeeSpecies allele0, IBeeSpecies allele1, IGenome genome0, IGenome genome1);
}
