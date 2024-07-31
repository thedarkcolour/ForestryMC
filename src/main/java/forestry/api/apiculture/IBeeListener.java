/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import forestry.api.genetics.IIndividual;

public interface IBeeListener {
	/**
	 * Called when the bees wear out the housing's equipment.
	 *
	 * @param amount Integer indicating the amount worn out.
	 */
	default void wearOutEquipment(int amount) {
	}

	/**
	 * Called after the children have been spawned, and before the new princess is spawned.
	 */
	default void onQueenDeath() {
	}

	/**
	 * Called when the bees have retrieved some pollen.
	 *
	 * @return true if this bee listener handled the pollen.
	 */
	default boolean onPollenRetrieved(IIndividual pollen) {
		return false;
	}
}
