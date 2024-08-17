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
package forestry.arboriculture.genetics;

import forestry.api.arboriculture.IArboristTracker;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.ISpecies;
import forestry.core.genetics.BreedingTracker;

public class ArboristTracker extends BreedingTracker implements IArboristTracker {
	public ArboristTracker() {
		super(ForestrySpeciesTypes.TREE);
	}

	@Override
	public void registerPickup(ISpecies<?> species) {
	}
}
