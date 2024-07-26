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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.arboriculture.IArboristTracker;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpecies;
import forestry.core.genetics.BreedingTracker;
import forestry.core.utils.SpeciesUtil;

public class ArboristTracker extends BreedingTracker implements IArboristTracker {
	public ArboristTracker() {
		super();
	}

	public ArboristTracker(CompoundTag tag) {
		super(tag);
	}

	@Override
	protected IBreedingTracker getBreedingTracker(Player player) {
		return SpeciesUtil.TREE_TYPE.get().getBreedingTracker(player.level, player.getGameProfile());
	}

	@Override
	protected ResourceLocation getSpeciesId() {
		return ForestrySpeciesTypes.TREE;
	}

	@Override
	public void registerPickup(ISpecies<?> individual) {
	}
}
