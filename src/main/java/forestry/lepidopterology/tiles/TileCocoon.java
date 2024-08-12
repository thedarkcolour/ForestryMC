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

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.owner.IOwnedTile;
import forestry.core.owner.IOwnerHandler;
import forestry.core.owner.OwnerHandler;
import forestry.core.utils.ItemStackUtil;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.blocks.BlockCocoon;
import forestry.lepidopterology.features.LepidopterologyTiles;

public class TileCocoon extends BlockEntity implements IOwnedTile {
	private final OwnerHandler ownerHandler = new OwnerHandler();
	private int maturationTime;
	private IButterfly caterpillar = SpeciesUtil.BUTTERFLY_TYPE.get().getDefaultSpecies().createIndividual();
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
			caterpillar = SpeciesUtil.deserializeIndividual(SpeciesUtil.BUTTERFLY_TYPE.get(), compoundNBT.getCompound("Caterpillar"));
		}
		this.ownerHandler.read(compoundNBT);
		this.maturationTime = compoundNBT.getInt("CATMAT");
		this.isSolid = compoundNBT.getBoolean("isSolid");
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);

		Tag tag = SpeciesUtil.serializeIndividual(this.caterpillar);
		if (tag != null) {
			compoundNBT.put("Caterpillar", tag);
		}

		this.ownerHandler.write(compoundNBT);

		compoundNBT.putInt("CATMAT", this.maturationTime);
		compoundNBT.putBoolean("isSolid", this.isSolid);
	}

	@Override
	public IOwnerHandler getOwnerHandler() {
		return ownerHandler;
	}

	public void onBlockTick() {
		maturationTime++;

		IGenome caterpillarGenome = this.caterpillar.getGenome();
		int caterpillarMatureTime = Math
				.round((float) caterpillarGenome.getActiveValue(ButterflyChromosomes.LIFESPAN) / (caterpillarGenome.getActiveValue(ButterflyChromosomes.FERTILITY) * 2));

		if (maturationTime >= caterpillarMatureTime) {
			int age = getBlockState().getValue(BlockCocoon.AGE);
			if (age < 2) {
				maturationTime = 0;
				BlockState blockState = getBlockState().setValue(BlockCocoon.AGE, age + 1);
				level.setBlock(worldPosition, blockState, Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
			} else if (this.caterpillar.canTakeFlight(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ())) {
				List<ItemStack> cocoonDrops = this.caterpillar.getCocoonDrop(this.isSolid, this.caterpillar.getGenome().getActiveValue(ButterflyChromosomes.COCOON));
				for (ItemStack drop : cocoonDrops) {
					ItemStackUtil.dropItemStackAsEntity(drop, level, worldPosition);
				}
				level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
				attemptButterflySpawn(level, this.caterpillar, getBlockPos());
			}
		}
	}

	private static void attemptButterflySpawn(Level world, IButterfly butterfly, BlockPos pos) {
		SpeciesUtil.BUTTERFLY_TYPE.get().spawnButterflyInWorld(world, butterfly.copy(), pos.getX(), pos.getY() + 0.1f, pos.getZ());
		//Forestry.LOGGER.trace("A caterpillar '{}' hatched at {}/{}/{}.", butterfly.getDisplayName(), pos.getX(), pos.getY(), pos.getZ());
	}

	public IButterfly getCaterpillar() {
		return this.caterpillar;
	}

	public void setCaterpillar(IButterfly caterpillar) {
		this.caterpillar = caterpillar;
	}

	public List<ItemStack> getCocoonDrops() {
		return this.caterpillar.getCocoonDrop(this.isSolid, this.caterpillar.getGenome().getActiveValue(ButterflyChromosomes.COCOON));
	}
}
