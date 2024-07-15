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

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ITaxon;

import forestry.api.apiculture.genetics.IAlleleBeeSpeciesBuilder;
import forestry.api.apiculture.genetics.IBeeFactory;
import forestry.apiculture.genetics.alleles.AlleleBeeSpecies;

public class BeeFactory implements IBeeFactory {
	@Override
	public IAlleleBeeSpeciesBuilder createSpecies(ResourceLocation id, String speciesIdentifier) {
		return new AlleleBeeSpecies.Builder(id, speciesIdentifier);
	}

}
