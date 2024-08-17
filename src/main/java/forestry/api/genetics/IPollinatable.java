/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import javax.annotation.Nullable;

import forestry.api.core.ForestryEvent;

/**
 * Can be implemented by tile entities, if they wish to be pollinatable.
 */
public interface IPollinatable extends ICheckPollinatable {
	/**
	 * Pollinates this entity.
	 * Implementations should fire the {@link ForestryEvent.PollinateIndividual} event.
	 *
	 * @param pollen IIndividual representing the pollen (the other individual to mate with).
	 * @param pollinator The {@link IIndividual} (for bees and butterflies) or entity (for players) that is trying to
	 *                   mate the two individuals. Can be null.
	 */
	void mateWith(IIndividual pollen, @Nullable Object pollinator);

	@Deprecated
	default void mateWith(IIndividual pollen) {
		mateWith(pollen, null);
	}
}
