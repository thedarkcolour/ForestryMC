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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.products.Product;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.farming.logic.crops.CropDestroy;

public class FarmableGE implements IFarmable {

	private final Set<Item> windfall = new HashSet<>();

	//TODO would be nice to make this class more granular so windfall and germling checks could be more specific
	public FarmableGE() {
		windfall.addAll(AlleleManager.geneticRegistry.getRegisteredFruitFamilies().values().stream()
			.map(TreeManager.treeRoot::getFruitProvidersForFruitFamily)
			.flatMap(Collection::stream)
			.flatMap(p -> Stream.concat(p.getProducts().getPossibleProducts().stream(), p.getSpecialty().getPossibleProducts().stream()))
			.map(Product::getItem)
			.collect(Collectors.toSet()));
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
		ITreeSpeciesType treeRoot = TreeManager.treeRoot;

		ITree tree = treeRoot.create(germling);
		return tree != null && treeRoot.plantSapling(level, tree, player.getGameProfile(), pos);
	}

	@Override
	public boolean isGermling(ItemStack stack) {
		return TreeManager.treeRoot.isMember(stack);
	}

	@Override
	public boolean isWindfall(ItemStack stack) {
		return windfall.contains(stack.getItem());
	}

}
