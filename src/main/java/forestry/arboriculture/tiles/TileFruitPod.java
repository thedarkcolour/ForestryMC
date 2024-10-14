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

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.IForestryApi;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.core.IProduct;
import forestry.api.genetics.IFruitBearer;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.features.ArboricultureTiles;
import forestry.core.ClientsideCode;
import forestry.core.network.IStreamable;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.NBTUtilForestry;
import forestry.core.utils.SpeciesUtil;

public class TileFruitPod extends BlockEntity implements IFruitBearer, IStreamable {
	private static final short MAX_MATURITY = 2;
	private static final IGenome defaultGenome = SpeciesUtil.TREE_TYPE.get().getDefaultSpecies().getDefaultGenome();

	public static final String NBT_MATURITY = "MT";
	public static final String NBT_YIELD = "SP";
	public static final String NBT_FRUIT = "UID";

	private IGenome genome = defaultGenome;
	@Nullable
	private IFruit fruit = null;
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
	public void writeData(FriendlyByteBuf data) {
		if (fruit != null) {
			data.writeBoolean(true);
			data.writeResourceLocation(TreeChromosomes.FRUIT.getId(fruit));
		} else {
			data.writeBoolean(false);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		if (data.readBoolean()) {
			IValueAllele<?> stored = IForestryApi.INSTANCE.getAlleleManager().getAllele(data.readResourceLocation()).cast();

			if (stored.value() instanceof IFruit newFruit) {
				this.fruit = newFruit;
				ClientsideCode.markForUpdate(this.worldPosition);
			}
		}
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);
		if (this.fruit != null) {
			compoundNBT.putString(NBT_FRUIT, TreeChromosomes.FRUIT.getId(fruit).toString());
		}
		compoundNBT.putShort(NBT_MATURITY, this.maturity);
		compoundNBT.putFloat(NBT_YIELD, this.yield);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);

		String fruitNbt = nbt.getString(NBT_FRUIT);
		if (!fruitNbt.isEmpty()) {
			this.fruit = TreeChromosomes.FRUIT.getSafe(new ResourceLocation(fruitNbt));
		}
		if (this.fruit == null) {
			this.fruit = ForestryAlleles.FRUIT_COCOA.value();
		}

		this.maturity = nbt.getShort(NBT_MATURITY);
		this.yield = nbt.getFloat(NBT_YIELD);
	}

	/* UPDATING */
	public void onBlockTick(RandomSource rand) {
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
		if (fruit == null) {
			return ItemStack.EMPTY;
		}
		List<IProduct> products = fruit.getProducts();

		ItemStack pickBlock = ItemStack.EMPTY;
		float maxChance = 0.0f;
		for (IProduct product : products) {
			if (maxChance < product.chance()) {
				maxChance = product.chance();
				pickBlock = product.createStack();
			}
		}

		pickBlock.setCount(1);
		return pickBlock;
	}

	public List<ItemStack> getDrops() {
		return this.fruit == null ? List.of() : this.fruit.getFruits(this.genome, this.level, this.maturity);
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
		List<ItemStack> fruits = getDrops();
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
}
