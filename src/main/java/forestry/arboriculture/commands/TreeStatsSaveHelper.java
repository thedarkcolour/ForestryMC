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
package forestry.arboriculture.commands;

import java.util.Collection;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpecies;
import forestry.core.commands.IStatsSaveHelper;
import forestry.core.utils.SpeciesUtil;

public class TreeStatsSaveHelper implements IStatsSaveHelper {
	@Override
	public String getTranslationKey() {
		return "for.chat.command.forestry.tree.save.stats";
	}

	@Override
	public void addExtraInfo(Collection<Component> statistics, IBreedingTracker breedingTracker) {
	}

	@Override
	public List<? extends ISpecies<?>> getSpecies() {
		return SpeciesUtil.getAllTreeSpecies();
	}

	@Override
	public String getFileSuffix() {
		return "trees";
	}

	@Override
	public IBreedingTracker getBreedingTracker(Level world, GameProfile gameProfile) {
		return SpeciesUtil.TREE_TYPE.get().getBreedingTracker(world, gameProfile);
	}

}
