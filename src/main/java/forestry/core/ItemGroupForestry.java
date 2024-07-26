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
package forestry.core;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.core.ItemGroups;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.arboriculture.genetics.Tree;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.core.features.CoreItems;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.genetics.Butterfly;
import forestry.lepidopterology.genetics.ButterflyDefinition;
import forestry.storage.features.BackpackItems;

public abstract class ItemGroupForestry extends CreativeModeTab {
	// todo remove in 1.20 when the creative tabs registry is added
	static void initTabs() {
		ItemGroups.tabForestry = new ItemGroupForestry(ForestryConstants.MOD_ID) {
			@Override
			public ItemStack makeIcon() {
				return CoreItems.FERTILIZER_COMPOUND.stack();
			}
		};
		ItemGroups.tabStorage = new ItemGroupForestry("storage") {
			@Override
			public ItemStack makeIcon() {
				return BackpackItems.MINER_BACKPACK.stack();
			}
		};
		ItemGroups.tabApiculture = new ItemGroupForestry("apiculture") {
			@Override
			public ItemStack makeIcon() {
				return SpeciesUtil.BEE_TYPE.get().getTypes().createStack(new Bee(BeeDefinition.FOREST.getGenome()), BeeLifeStage.DRONE);
			}
		};
		ItemGroups.tabArboriculture = new ItemGroupForestry("arboriculture") {
			@Override
			public ItemStack makeIcon() {
				return SpeciesUtil.TREE_TYPE.get().getTypes().createStack(new Tree(TreeDefinition.Oak.getGenome()), TreeLifeStage.SAPLING);
			}
		};
		ItemGroups.tabLepidopterology = new ItemGroupForestry("lepidopterology") {
			@Override
			public ItemStack makeIcon() {
				return SpeciesUtil.BUTTERFLY_TYPE.get().getTypes().createStack(new Butterfly(ButterflyDefinition.Brimstone.getGenome()), ButterflyLifeStage.BUTTERFLY);
			}
		};
	}

	private ItemGroupForestry(String label) {
		super(label);
	}
}
