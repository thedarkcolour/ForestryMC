/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import net.minecraft.world.level.Level;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

import forestry.api.lepidopterology.IButterflyNursery;

public interface IButterflyMutation extends IMutation {
	float getChance(Level world, IButterflyNursery housing, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1);
}
