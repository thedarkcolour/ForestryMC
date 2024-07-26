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
package forestry.arboriculture;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.Product;

public class DummyFruit implements IFruit {
	private final boolean dominant;

	public DummyFruit(boolean dominant) {
		this.dominant = dominant;
	}

	@Override
	public boolean isDominant() {
		return this.dominant;
	}

	@Override
	public List<ItemStack> getFruits(IGenome genome, Level level, BlockPos pos, int ripeningTime) {
		return List.of();
	}

	@Override
	public boolean requiresFruitBlocks() {
		return false;
	}

	@Override
	public boolean trySpawnFruitBlock(IGenome genome, LevelAccessor world, RandomSource rand, BlockPos pos) {
		return false;
	}

	@Override
	public int getColour(IGenome genome, BlockGetter world, BlockPos pos, int ripeningTime) {
		return 0xffffff;
	}

	@Override
	public int getDecorativeColor() {
		return 0xffffff;
	}

	@Override
	public boolean isFruitLeaf(IGenome genome, LevelAccessor level, BlockPos pos) {
		return false;
	}

	@Override
	public int getRipeningPeriod() {
		return 0;
	}

	@Override
	public List<Product> getProducts() {
		return List.of();
	}

	@Override
	public List<Product> getSpecialty() {
		return List.of();
	}

	@Nullable
	@Override
	public ResourceLocation getSprite(IGenome genome, BlockGetter world, BlockPos pos, int ripeningTime) {
		return getDecorativeSprite();
	}

	@Nullable
	@Override
	public String getModelName() {
		return null;
	}

	@Override
	public String getModId() {
		return ForestryConstants.MOD_ID;
	}
}
