/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology;

import javax.annotation.Nullable;

import net.minecraft.world.entity.PathfinderMob;

import genetics.api.individual.IIndividual;

import forestry.api.lepidopterology.genetics.IButterfly;

public interface IEntityButterfly {
	void changeExhaustion(int change);

	int getExhaustion();

	IButterfly getButterfly();

	/**
	 * @return The entity as an EntityCreature to save casting.
	 */
	PathfinderMob getEntity();

	@Nullable
	IIndividual getPollen();

	void setPollen(@Nullable IIndividual pollen);

	boolean canMateWith(IEntityButterfly butterfly);

	boolean canMate();
}
