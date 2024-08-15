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
package forestry.farming.logic.crops;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.core.utils.BlockUtil;

public class CropPeat extends Crop {
	public CropPeat(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	protected boolean isCrop(Level world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return CoreBlocks.PEAT.blockEqual(blockState);
	}

	@Override
	protected List<ItemStack> harvestBlock(Level level, BlockPos pos) {
		NonNullList<ItemStack> drops = NonNullList.create();
		drops.add(CoreItems.PEAT.stack());

		BlockUtil.sendDestroyEffects(level, pos, level.getBlockState(pos));

		level.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
		return drops;
	}
}
