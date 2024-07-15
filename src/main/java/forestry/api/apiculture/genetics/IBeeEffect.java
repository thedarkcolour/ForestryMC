/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;

public interface IBeeEffect extends IEffect {

	/**
	 * Called by apiaries to cause an effect in the world. (server)
	 *
	 * @param genome     Genome of the bee queen causing this effect
	 * @param storedData Object containing the stored effect data for the apiary/hive the bee is in.
	 * @param housing    {@link IBeeHousing} the bee currently resides in.
	 * @return storedData, may have been manipulated.
	 */
	IEffectData doEffect(IGenome genome, IEffectData storedData, IBeeHousing housing);

	/**
	 * Called on the client side to produce visual bee effects.
	 *
	 * @param genome     Genome of the bee queen causing this effect
	 * @param storedData Object containing the stored effect data for the apiary/hive the bee is in.
	 * @param housing    {@link IBeeHousing} the bee currently resides in.
	 * @return storedData, may have been manipulated.
	 */
	IEffectData doFX(IGenome genome, IEffectData storedData, IBeeHousing housing);

}
