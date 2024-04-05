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
package forestry.farming.blocks;

import java.util.Locale;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import forestry.api.core.IBlockSubtype;

public enum EnumFarmMaterial implements IBlockSubtype {
	STONE_BRICK(Blocks.STONE_BRICKS, ChatFormatting.DARK_GRAY),
	MOSSY_STONE_BRICK(Blocks.MOSSY_STONE_BRICKS, ChatFormatting.DARK_GRAY),
	CRACKED_STONE_BRICK(Blocks.CRACKED_STONE_BRICKS, ChatFormatting.DARK_GRAY),
	BRICK(Blocks.BRICKS, ChatFormatting.GOLD),
	CUT_SANDSTONE(Blocks.CUT_SANDSTONE, ChatFormatting.YELLOW),
	SANDSTONE_CHISELED(Blocks.CHISELED_SANDSTONE, ChatFormatting.YELLOW),
	BRICK_NETHER(Blocks.NETHER_BRICKS, ChatFormatting.DARK_RED),
	BRICK_CHISELED(Blocks.CHISELED_STONE_BRICKS, ChatFormatting.GOLD),
	QUARTZ(Blocks.QUARTZ_BLOCK, ChatFormatting.WHITE),
	QUARTZ_CHISELED(Blocks.CHISELED_QUARTZ_BLOCK, ChatFormatting.WHITE),
	QUARTZ_LINES(Blocks.QUARTZ_PILLAR, ChatFormatting.WHITE);

	private final Block base;
	private final ChatFormatting formatting;

	EnumFarmMaterial(Block base, ChatFormatting formatting) {
		this.base = base;
		this.formatting = formatting;
	}

	public ChatFormatting getFormatting() {
		return formatting;
	}

	public void saveToCompound(CompoundTag compound) {
		compound.putInt("FarmBlock", this.ordinal());
	}

	public Component getDisplayName() {
		return base.getName();
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public Block getBase() {
		return base;
	}
}
