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

import net.minecraft.world.item.ItemStack;

import genetics.api.individual.ISpeciesDefinition;

import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.ISpeciesType;

public interface ITreeDefinition extends ISpeciesDefinition<ITree> {

	@Override
	ISpeciesType<ITree> getSpecies();

	ItemStack getMemberStack(TreeLifeStage treeType);
}
