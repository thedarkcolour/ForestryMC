/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture.genetics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBeeMutation;
import forestry.api.apiculture.genetics.IBeeMutationBuilder;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.core.genetics.mutations.Mutation;

public class BeeMutation extends Mutation implements IBeeMutation {
	public BeeMutation(IBeeSpecies bee0, IBeeSpecies bee1, IAllele[] result, int chance) {
		super(bee0, bee1, result, chance);
	}

	@Override
	public IBeeMutation build() {
		return this;
	}

	@Override
	public IBeeSpeciesType getType() {
		return BeeManager.beeRoot;
	}

	@Override
	public float getChance(IBeeHousing housing, IBeeSpecies allele0, IBeeSpecies allele1, IGenome genome0, IGenome genome1) {

	}

}
