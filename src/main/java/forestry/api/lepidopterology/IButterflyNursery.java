/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology;

import javax.annotation.Nullable;

import forestry.api.climate.IClimateProvider;
import forestry.api.core.ILocationProvider;
import forestry.api.genetics.IIndividual;
import forestry.api.lepidopterology.genetics.IButterfly;

/**
 * A butterfly nursery is a place, usually a leaf block, where caterpillars laid by mated butterflies mature into cocoons.
 */
public interface IButterflyNursery extends ILocationProvider, IClimateProvider {
	@Nullable
	IButterfly getCaterpillar();

	/**
	 * @return The butterfly who created this nursery.
	 */
	@Nullable
	IIndividual getNanny();

	void setCaterpillar(@Nullable IButterfly caterpillar);

	boolean canNurse(IButterfly caterpillar);
}
