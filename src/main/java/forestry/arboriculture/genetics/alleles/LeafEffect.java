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
package forestry.arboriculture.genetics.alleles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import genetics.api.alleles.AlleleCategorized;
import forestry.api.genetics.IGenome;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.genetics.ILeafEffect;
import forestry.api.genetics.IEffectData;

public class LeafEffect extends AlleleCategorized implements ILeafEffect {
	protected LeafEffect(String valueName, boolean isDominant) {
		super(ForestryConstants.MOD_ID, "leaves", valueName, isDominant);
	}

	@Override
	public boolean isCombinable() {
		return true;
	}

	@Override
	public IEffectData validateStorage(IEffectData storedData) {
		return storedData;
	}

	@Override
	public IEffectData doEffect(IGenome genome, IEffectData storedData, Level world, BlockPos pos) {
		return storedData;
	}
}
