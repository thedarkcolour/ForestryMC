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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.genetics.ITree;
import forestry.core.ClientsideCode;
import forestry.core.network.IStreamable;
import forestry.core.utils.NBTUtilForestry;
import forestry.core.utils.SpeciesUtil;

/**
 * This is the base TE class for any block that needs to contain tree genome information.
 */
public abstract class TileTreeContainer extends BlockEntity implements IStreamable {
	@Nullable
	private ITree containedTree;

	public TileTreeContainer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/* SAVING & LOADING */
	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		if (this.containedTree != null) {
			Tag serialized = SpeciesUtil.serializeIndividual(this.containedTree);
			if (serialized != null) {
				nbt.put("ContainedTree", serialized);
			}
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);

		Tag treeNbt = nbt.get("ContainedTree");

		if (treeNbt != null) {
			this.containedTree = SpeciesUtil.deserializeIndividual(SpeciesUtil.TREE_TYPE.get(), treeNbt);
		}
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		ITree tree = getTree();
		if (tree != null) {
			data.writeBoolean(true);
			ResourceLocation speciesId = tree.getSpecies().id();
			data.writeResourceLocation(speciesId);
		} else {
			data.writeBoolean(false);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		if (data.readBoolean()) {
			ResourceLocation speciesId = data.readResourceLocation();
			ITree tree = SpeciesUtil.getTreeSpecies(speciesId).createIndividual();
			setTree(tree);
		}
	}

	/* CONTAINED TREE */
	public void setTree(ITree tree) {
		this.containedTree = tree;

		if (level != null && level.isClientSide) {
			ClientsideCode.markForUpdate(this.worldPosition);
			;
		}
	}

	@Nullable
	public ITree getTree() {
		return this.containedTree;
	}

	/* UPDATING */

	/**
	 * Leaves and saplings will implement their logic here.
	 */
	public abstract void onBlockTick(Level worldIn, BlockPos pos, BlockState state, RandomSource rand);

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag nbt = pkt.getTag();
		handleUpdateTag(nbt);
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
}
