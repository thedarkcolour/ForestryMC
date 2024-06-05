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

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import forestry.core.utils.BlockUtil;

//TODO consider movnig compat specific crops to compat
//TODO follow up in forge on generic crop interface...
public class CropBasicGrowthCraft extends Crop {

	private final BlockState blockState;
	private final boolean isRice;
	private final boolean isGrape;

	public CropBasicGrowthCraft(Level world, BlockState blockState, BlockPos position, boolean isRice, boolean isGrape) {
		super(world, position);
		this.blockState = blockState;
		this.isRice = isRice;
		this.isGrape = isGrape;
	}

	@Override
	protected boolean isCrop(Level world, BlockPos pos) {
		return world.getBlockState(pos) == blockState;
	}

	@Override
	protected NonNullList<ItemStack> harvestBlock(Level level, BlockPos pos) {
		Block block = blockState.getBlock();
		NonNullList<ItemStack> harvest = NonNullList.create();
		//TODO cast
		LootContext.Builder ctx = new LootContext.Builder((ServerLevel) level)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));
		harvest.addAll(block.getDrops(blockState, ctx));
		if (harvest.size() > 1) {
			harvest.remove(0); //Hops have rope as first drop.
		}

		BlockUtil.sendDestroyEffects(level, pos, blockState);

		if (isGrape) {
			level.removeBlock(pos, false);
		} else {
			level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
		}

		if (isRice) {
			// TODO: GrowthCraft for MC 1.9. Don't use meta, get the actual block state.
			level.setBlock(pos.below(), block.defaultBlockState(), Block.UPDATE_CLIENTS);
			//TODO flatten
		}

		return harvest;
	}

	@Override
	public String toString() {
		return String.format("CropBasicGrowthCraft [ position: [ %s ]; block: %s ]", position.toString(), blockState);
	}
}
