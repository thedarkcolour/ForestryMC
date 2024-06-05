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

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import forestry.core.utils.BlockUtil;
import forestry.core.utils.ItemStackUtil;

public class CropDestroy extends Crop {

	protected final BlockState blockState;
	@Nullable
	protected final BlockState replantState;

	protected final ItemStack germling;

	public CropDestroy(Level world, BlockState blockState, BlockPos position, @Nullable BlockState replantState) {
		this(world, blockState, position, replantState, ItemStack.EMPTY);
	}

	public CropDestroy(Level world, BlockState blockState, BlockPos position, @Nullable BlockState replantState, ItemStack germling) {
		super(world, position);
		this.blockState = blockState;
		this.replantState = replantState;
		this.germling = germling;
	}

	@Override
	protected boolean isCrop(Level world, BlockPos pos) {
		return world.getBlockState(pos) == blockState;
	}

	@Override
	protected NonNullList<ItemStack> harvestBlock(Level level, BlockPos pos) {
		Block block = blockState.getBlock();
		List<ItemStack> harvested = Block.getDrops(blockState, (ServerLevel) level, pos, level.getBlockEntity(pos));    //TODO - method safety
		NonNullList<ItemStack> nnHarvested = NonNullList.of(ItemStack.EMPTY, harvested.toArray(new ItemStack[0]));    //TODO very messy
		//float chance = ForgeEventFactory.fireBlockHarvesting(nnHarvested, level, pos, blockState, 0, 1.0F, false, null);
		//TODO: Fix dropping
		float chance = 1.0F;
		boolean removedSeed = germling.isEmpty();
		Iterator<ItemStack> dropIterator = harvested.iterator();
		while (dropIterator.hasNext()) {
			ItemStack next = dropIterator.next();
			if (level.random.nextFloat() <= chance) {
				if (!removedSeed && ItemStackUtil.isIdenticalItem(next, germling)) {
					next.shrink(1);
					if (next.isEmpty()) {
						dropIterator.remove();
					}
					removedSeed = true;
				}
			} else {
				dropIterator.remove();
			}
		}

		BlockUtil.sendDestroyEffects(level, pos, blockState);

		if (replantState != null) {
			level.setBlock(pos, replantState, Block.UPDATE_CLIENTS);
		} else {
			level.removeBlock(pos, false);
		}
		if (!(harvested instanceof NonNullList)) {
			return NonNullList.of(ItemStack.EMPTY, harvested.toArray(new ItemStack[0]));
		} else {
			return (NonNullList<ItemStack>) harvested;
		}
	}

	@Override
	public String toString() {
		return String.format("CropDestroy [ position: [ %s ]; block: %s ]", position.toString(), blockState);
	}
}
