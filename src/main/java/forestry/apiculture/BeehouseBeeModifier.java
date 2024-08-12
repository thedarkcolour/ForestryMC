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
package forestry.apiculture;

import javax.annotation.Nullable;

import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

// no mutations/ignoble decay, 300% aging and flowering, 25% production
public class BeehouseBeeModifier implements IBeeModifier {
	@Override
	public float modifyProductionSpeed(IGenome genome, float currentSpeed) {
		return 0.25f * currentSpeed;
	}

	@Override
	public float modifyMutationChance(IGenome genome, IGenome mate, IMutation<IBeeSpecies> mutation, float currentChance) {
		return 0.0f;
	}

	@Override
	public float modifyAging(IGenome genome, @Nullable IGenome mate, float currentAging) {
		return 3.0f * currentAging;
	}

	@Override
	public float modifyPollination(IGenome genome, float currentPollination) {
		return 3.0f * currentPollination;
	}

	@Override
	public float modifyGeneticDecay(IGenome genome, float currentDecay) {
		return 0.0f;
	}
}
