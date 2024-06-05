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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableInfo;
import forestry.core.utils.BlockUtil;
import forestry.farming.logic.crops.CropDestroy;

public class FarmableGourd implements IFarmable {

	private final ItemStack seed;
	private final Block stem;
	private final Block fruit;

	public FarmableGourd(ItemStack seed, Block stem, Block fruit) {
		this.seed = seed;
		this.stem = stem;
		this.fruit = fruit;
	}

	@Override
	public boolean isSaplingAt(Level level, BlockPos pos, BlockState state) {
		return state.getBlock() == stem;
	}

	@Override
	public ICrop getCropAt(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() != fruit) {
			return null;
		}

		return new CropDestroy(level, state, pos, null);
	}

	@Override
	public boolean isGermling(ItemStack stack) {
		return ItemStack.isSame(stack, seed);
	}

	@Override
	public void addInformation(IFarmableInfo info) {
		info.addSeedlings(seed);
		info.addProducts(new ItemStack(fruit));
	}

	@Override
	public boolean isWindfall(ItemStack stack) {
		return false;
	}

	@Override
	public boolean plantSaplingAt(Player player, ItemStack germling, Level level, BlockPos pos) {
		return BlockUtil.setBlockWithPlaceSound(level, pos, stem.defaultBlockState());
	}

}
