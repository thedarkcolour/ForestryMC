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
package forestry.lepidopterology.genetics;

import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.ISpecies;
import forestry.api.lepidopterology.ILepidopteristTracker;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.genetics.BreedingTracker;

public class LepidopteristTracker extends BreedingTracker implements ILepidopteristTracker {
	public LepidopteristTracker() {
		super(ForestrySpeciesTypes.BUTTERFLY);
	}

	@Override
	public void registerCatch(IButterfly butterfly) {
		registerSpecies(butterfly.getSpecies());
		registerSpecies(butterfly.getInactiveSpecies());
	}

	@Override
	public void registerPickup(ISpecies<?> species) {
		registerSpecies(species);
	}
}
