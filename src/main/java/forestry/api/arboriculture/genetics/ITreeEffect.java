/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.apiculture.genetics.IEffect;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.IRegistryAlleleValue;

/**
 * Simple allele encapsulating a leaf effect. (Not implemented)
 */
public interface ITreeEffect extends IEffect, IRegistryAlleleValue {
	IEffectData doEffect(IGenome genome, IEffectData storedData, Level level, BlockPos pos);
}
