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
package forestry.arboriculture.models;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.client.model.data.ModelData;

import forestry.arboriculture.blocks.BlockDefaultLeavesFruit;

public class ModelDefaultLeavesFruit extends ModelDecorativeLeaves<BlockDefaultLeavesFruit> {
	public ModelDefaultLeavesFruit() {
		super(BlockDefaultLeavesFruit.class);
	}

	@Override
	protected ModelDefaultLeaves.Key getInventoryKey(ItemStack stack) {
		Block block = Block.byItem(stack.getItem());
		return new ModelDefaultLeaves.Key(((BlockDefaultLeavesFruit) block).getSpeciesId(), Minecraft.useFancyGraphics());
	}

	@Override
	protected ModelDefaultLeaves.Key getWorldKey(BlockState state, ModelData extraData) {
		Block block = state.getBlock();
		return new ModelDefaultLeaves.Key(((BlockDefaultLeavesFruit) block).getSpeciesId(), Minecraft.useFancyGraphics());
	}
}
