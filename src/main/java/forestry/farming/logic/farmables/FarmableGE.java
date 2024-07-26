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
package forestry.farming.logic.farmables;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.core.utils.SpeciesUtil;
import forestry.farming.logic.crops.CropDestroy;

public class FarmableGE implements IFarmable {

	private final Set<Item> windfall = new HashSet<>();

	//TODO would be nice to make this class more granular so windfall and germling checks could be more specific
	public FarmableGE() {
		for (IFruit fruit : TreeChromosomes.FRUITS.values()) {
			for (Product product : fruit.getProducts()) {
				this.windfall.add(product.item());
			}
		}
	}

	@Override
	public boolean isSaplingAt(Level level, BlockPos pos, BlockState state) {
		return ArboricultureBlocks.SAPLING_GE.blockEqual(state);
	}

	@Override
	@Nullable
	public ICrop getCropAt(Level level, BlockPos pos, BlockState state) {
		if (!state.is(BlockTags.LOGS)) {
			return null;
		}

		return new CropDestroy(level, state, pos, null);
	}

	@Override
	public boolean plantSaplingAt(Player player, ItemStack germling, Level level, BlockPos pos) {
		ITreeSpeciesType treeRoot = SpeciesUtil.TREE_TYPE.get();

		return IIndividualHandler.filter(germling, individual -> individual instanceof ITree tree && treeRoot.plantSapling(level, tree, player.getGameProfile(), pos));
	}

	@Override
	public boolean isGermling(ItemStack stack) {
		return SpeciesUtil.TREE_TYPE.get().isMember(stack);
	}

	@Override
	public boolean isWindfall(ItemStack stack) {
		return windfall.contains(stack.getItem());
	}

}
