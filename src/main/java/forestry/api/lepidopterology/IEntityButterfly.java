/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology;

import javax.annotation.Nullable;

import net.minecraft.world.entity.PathfinderMob;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.pollen.IPollen;
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
	IPollen<?> getPollen();

	void setPollen(@Nullable IPollen<?> pollen);

	boolean canMateWith(IEntityButterfly butterfly);

	boolean canMate();
}
