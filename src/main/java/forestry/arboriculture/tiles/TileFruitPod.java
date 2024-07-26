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
package forestry.arboriculture.tiles;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.genetics.IFruitBearer;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IRegistryAllele;
import forestry.api.genetics.products.IProductList;
import forestry.api.genetics.Product;
import forestry.arboriculture.features.ArboricultureTiles;
import forestry.core.network.IStreamable;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.NBTUtilForestry;
import forestry.core.utils.RenderUtil;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;
import forestry.core.utils.SpeciesUtil;

import genetics.utils.AlleleUtils;

public class TileFruitPod extends BlockEntity implements IFruitBearer, IStreamable {

	private static final short MAX_MATURITY = 2;
	private static final IGenome defaultGenome = SpeciesUtil.TREE_TYPE.get().getKaryotype().getDefaultSpecies();

	private IGenome genome = defaultGenome;
	private IRegistryAllele<IFruit> fruit = ForestryAlleles.FRUIT_COCOA;
	private short maturity;
	private float yield;

	public TileFruitPod(BlockPos pos, BlockState state) {
		super(ArboricultureTiles.PODS.tileType(), pos, state);
	}

	public void setProperties(IGenome genome, IFruit allele, float yield) {
		this.genome = genome;
		this.fruit = allele;
		this.yield = yield;
		setChanged();
	}

	/* SAVING & LOADING */
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);

		IAllele stored = AlleleUtils.getAllele(compoundNBT.getString("UID"));
		if (stored instanceof IFruit fruit) {
			this.fruit = fruit;
		} else {
			this.fruit = defaultAllele;
		}

		maturity = compoundNBT.getShort("MT");
		yield = compoundNBT.getFloat("SP");
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);
		compoundNBT.putString("UID", fruit.alleleId().toString());
		compoundNBT.putShort("MT", maturity);
		compoundNBT.putFloat("SP", yield);
	}

	/* UPDATING */
	public void onBlockTick(Level world, BlockPos pos, BlockState state, RandomSource rand) {
		if (canMature() && rand.nextFloat() <= yield) {
			addRipeness(0.5f);
		}
	}

	public boolean canMature() {
		return maturity < MAX_MATURITY;
	}

	public short getMaturity() {
		return maturity;
	}

	public ItemStack getPickBlock() {
		IProductList products = fruit.getProvider().getProducts();

		ItemStack pickBlock = ItemStack.EMPTY;
		float maxChance = 0.0f;
		for (Product product : products.getPossibleProducts()) {
			if (maxChance < product.getChance()) {
				maxChance = product.getChance();
				pickBlock = product.copyStack();
			}
		}

		pickBlock.setCount(1);
		return pickBlock;
	}

	public NonNullList<ItemStack> getDrops() {
		return fruit.getFruits(genome, level, getBlockPos(), maturity);
	}

	/* NETWORK */
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		return NBTUtilForestry.writeStreamableToNbt(this, tag);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		NBTUtilForestry.readStreamableFromNbt(this, tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag nbt = pkt.getTag();
		handleUpdateTag(nbt);
	}

	/* IFRUITBEARER */
	@Override
	public boolean hasFruit() {
		return true;
	}

	@Override
	public List<ItemStack> pickFruit(ItemStack tool) {
		NonNullList<ItemStack> fruits = getDrops();
		maturity = 0;

		BlockState oldState = getBlockState();
		BlockState newState = oldState.setValue(CocoaBlock.AGE, 0);
		BlockUtil.setBlockWithBreakSound(level, getBlockPos(), newState, oldState);

		return fruits;
	}

	@Override
	public float getRipeness() {
		return (float) maturity / MAX_MATURITY;
	}

	@Override
	public void addRipeness(float add) {
		int previousAge = maturity;

		maturity += MAX_MATURITY * add;
		if (maturity > MAX_MATURITY) {
			maturity = MAX_MATURITY;
		}

		int age = maturity;
		if (age - previousAge > 0) {
			BlockState state = getBlockState().setValue(CocoaBlock.AGE, age);
			level.setBlockAndUpdate(getBlockPos(), state);
		}
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		if (fruit != defaultAllele) {
			data.writeUtf(fruit.getId().toString());
		} else {
			data.writeUtf("");
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		IAllele stored = AlleleUtils.getAllele(data.readUtf());
		if (stored instanceof IFruit fruit) {
			this.fruit = fruit;
		} else {
			this.fruit = defaultAllele;
		}
		RenderUtil.markForUpdate(getBlockPos());
	}
}
