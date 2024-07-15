/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import genetics.api.individual.IIndividual;

/**
 * @author Nedelosk
 * @since 5.12.16
 */
//TODO: Move to a component ?
public interface IPollinatableSpeciesType<I extends IIndividual> extends IForestrySpeciesType<I> {

	ICheckPollinatable createPollinatable(IIndividual individual);

	@Nullable
	IPollinatable tryConvertToPollinatable(@Nullable GameProfile owner, Level world, final BlockPos pos, final IIndividual pollen);

}
