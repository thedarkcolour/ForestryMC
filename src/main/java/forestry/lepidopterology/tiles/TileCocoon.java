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
package forestry.lepidopterology.tiles;

import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import forestry.Forestry;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.network.IStreamable;
import forestry.core.network.packets.PacketTileStream;
import forestry.core.owner.IOwnedTile;
import forestry.core.owner.IOwnerHandler;
import forestry.core.owner.OwnerHandler;
import forestry.core.utils.ItemStackUtil;
import forestry.core.utils.NetworkUtil;
import forestry.lepidopterology.blocks.BlockCocoon;
import forestry.lepidopterology.features.LepidopterologyTiles;
import forestry.lepidopterology.genetics.Butterfly;
import forestry.lepidopterology.genetics.ButterflyDefinition;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;

public class TileCocoon extends BlockEntity implements IStreamable, IOwnedTile, IButterflyCocoon {
	private final OwnerHandler ownerHandler = new OwnerHandler();
	private int maturationTime;
	private IButterfly caterpillar = ButterflyDefinition.CabbageWhite.createIndividual();
	private boolean isSolid;

	public TileCocoon(BlockPos pos, BlockState state, boolean isSolid) {
		super(isSolid ? LepidopterologyTiles.SOLID_COCOON.tileType() : LepidopterologyTiles.COCOON.tileType(), pos, state);
		this.isSolid = isSolid;
	}

	/* SAVING & LOADING */
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);

		if (compoundNBT.contains("Caterpillar")) {
			caterpillar = new Butterfly(compoundNBT.getCompound("Caterpillar"));
		}
		ownerHandler.read(compoundNBT);
		maturationTime = compoundNBT.getInt("CATMAT");
		isSolid = compoundNBT.getBoolean("isSolid");
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);

		CompoundTag subcompound = new CompoundTag();
		caterpillar.write(subcompound);
		compoundNBT.put("Caterpillar", subcompound);

		ownerHandler.write(compoundNBT);

		compoundNBT.putInt("CATMAT", maturationTime);
		compoundNBT.putBoolean("isSolid", isSolid);
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		IButterfly caterpillar = getCaterpillar();
		String speciesUID = caterpillar.getId();
		data.writeUtf(speciesUID);
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		String speciesUID = data.readUtf();
		IButterfly caterpillar = getButterfly(speciesUID);
		setCaterpillar(caterpillar);
	}

	private static IButterfly getButterfly(String speciesUID) {
		IAllele[] butterflyTemplate = ButterflyManager.butterflyRoot.getTemplates().getTemplate(speciesUID);
		Preconditions.checkNotNull(butterflyTemplate, "Could not find butterfly template for species: %s", speciesUID);
		return ButterflyManager.butterflyRoot.templateAsIndividual(butterflyTemplate);
	}

	@Override
	public IOwnerHandler getOwnerHandler() {
		return ownerHandler;
	}

	public void onBlockTick() {
		maturationTime++;

		IGenome caterpillarGenome = caterpillar.getGenome();
		int caterpillarMatureTime = Math
				.round((float) caterpillarGenome.getActiveValue(ButterflyChromosomes.LIFESPAN) / (caterpillarGenome.getActiveValue(ButterflyChromosomes.FERTILITY) * 2));

		if (maturationTime >= caterpillarMatureTime) {
			int age = getBlockState().getValue(BlockCocoon.AGE);
			if (age < 2) {
				maturationTime = 0;
				BlockState blockState = getBlockState().setValue(BlockCocoon.AGE, age);
				level.setBlock(worldPosition, blockState, Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
			} else if (caterpillar.canTakeFlight(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ())) {
				NonNullList<ItemStack> cocoonDrops = caterpillar.getCocoonDrop(this);
				for (ItemStack drop : cocoonDrops) {
					ItemStackUtil.dropItemStackAsEntity(drop, level, worldPosition);
				}
				level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
				attemptButterflySpawn(level, caterpillar, getBlockPos());
			}
		}
	}

	private boolean isListEmpty(NonNullList<ItemStack> cocoonDrops) {
		if (cocoonDrops.isEmpty()) {
			return true;
		}
		for (ItemStack stack : cocoonDrops) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private static void attemptButterflySpawn(Level world, IButterfly butterfly, BlockPos pos) {
		ButterflyManager.butterflyRoot.spawnButterflyInWorld(world, butterfly.copy(),
				pos.getX(), pos.getY() + 0.1f, pos.getZ());
		Forestry.LOGGER.trace("A caterpillar '{}' hatched at {}/{}/{}.", butterfly.getDisplayName(), pos.getX(), pos.getY(),
				pos.getZ());
	}

	@Override
	public BlockPos getCoordinates() {
		return getBlockPos();
	}

	@Override
	public IButterfly getCaterpillar() {
		return caterpillar;
	}

	@Override
	public void setCaterpillar(IButterfly butterfly) {
		this.caterpillar = butterfly;
		sendNetworkUpdate();
	}

	private void sendNetworkUpdate() {
		if (level != null && !level.isClientSide) {
			NetworkUtil.sendNetworkPacket(new PacketTileStream(this), worldPosition, level);
		}
	}

	public NonNullList<ItemStack> getCocoonDrops() {
		return caterpillar.getCocoonDrop(this);
	}

	@Override
	public boolean isSolid() {
		return isSolid;
	}

}
