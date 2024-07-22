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

import java.awt.Color;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.Product;

// Fruits that grow unripe in tree leaves, then ripen over time.
public class RipeningFruit extends DummyFruit {
	private int colourCallow = 0xffffff;
	private int diffR;
	private int diffG;
	private int diffB;
	private List<Product> products;

	public RipeningFruit(String modid, String name, Supplier<List<Product>> product) {
		super(modid, family);
		this.products = ProductListWrapper.create();
		this.products.addProduct(product, 1.0f);
	}

	public RipeningFruit setColours(Color ripe, Color callow) {
		colourCallow = callow.getRGB();
		int ripeRGB = ripe.getRGB();

		diffR = (ripeRGB >> 16 & 255) - (colourCallow >> 16 & 255);
		diffG = (ripeRGB >> 8 & 255) - (colourCallow >> 8 & 255);
		diffB = (ripeRGB & 255) - (colourCallow & 255);

		return this;
	}

	public RipeningFruit setRipeningPeriod(int period) {
		ripeningPeriod = period;
		return this;
	}

	private float getRipeningStage(int ripeningTime) {
		if (ripeningTime >= ripeningPeriod) {
			return 1.0f;
		}

		return (float) ripeningTime / ripeningPeriod;
	}

	@Override
	public List<ItemStack> getFruits(IGenome genome, Level world, BlockPos pos, int ripeningTime) {
		NonNullList<ItemStack> product = NonNullList.create();
		products.addProducts(world, pos, product, Product::chance, world.random);

		return product;
	}

	@Override
	public List<Product> getProducts() {
		return products;
	}

	@Override
	public boolean isFruitLeaf(IGenome genome, LevelAccessor level, BlockPos pos) {
		return true;
	}

	@Override
	public int getColour(IGenome genome, BlockGetter world, BlockPos pos, int ripeningTime) {
		float stage = getRipeningStage(ripeningTime);
		return getColour(stage);
	}

	private int getColour(float stage) {
		int r = (colourCallow >> 16 & 255) + (int) (diffR * stage);
		int g = (colourCallow >> 8 & 255) + (int) (diffG * stage);
		int b = (colourCallow & 255) + (int) (diffB * stage);

		return (r & 255) << 16 | (g & 255) << 8 | b & 255;
	}

	@Override
	public int getDecorativeColor() {
		return getColour(1.0f);
	}
}
