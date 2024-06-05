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
package forestry.core.tiles;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.network.NetworkHooks;

import forestry.api.genetics.IForestrySpeciesRoot;
import forestry.core.gui.ContainerNaturalistInventory;
import forestry.core.gui.IPagedInventory;
import forestry.core.inventory.InventoryNaturalistChest;

public abstract class TileNaturalistChest extends TileBase implements IPagedInventory {
	private static final float lidAngleVariationPerTick = 0.1F;
	public static final VoxelShape CHEST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

	private final IForestrySpeciesRoot speciesRoot;
	public float lidAngle;
	public float prevLidAngle;
	private int numPlayersUsing;

	public TileNaturalistChest(BlockEntityType type, BlockPos pos, BlockState state, IForestrySpeciesRoot speciesRoot) {
		super(type, pos, state);
		this.speciesRoot = speciesRoot;
		setInternalInventory(new InventoryNaturalistChest(this, speciesRoot));
	}

	public void increaseNumPlayersUsing() {
		if (numPlayersUsing == 0) {
			playLidSound(level, SoundEvents.CHEST_OPEN);
		}

		numPlayersUsing++;
        sendNetworkUpdate();
    }

	public void decreaseNumPlayersUsing() {
		numPlayersUsing--;
		if (numPlayersUsing < 0) {
			numPlayersUsing = 0;
		}
		if (numPlayersUsing == 0) {
			playLidSound(level, SoundEvents.CHEST_CLOSE);
		}
        sendNetworkUpdate();
    }

	@Override
	public void clientTick(Level level, BlockPos pos, BlockState state) {
		prevLidAngle = lidAngle;

		if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
			if (numPlayersUsing > 0) {
				lidAngle += lidAngleVariationPerTick;
			} else {
				lidAngle -= lidAngleVariationPerTick;
			}

			lidAngle = Math.max(Math.min(lidAngle, 1), 0);
		}
	}

	private void playLidSound(Level level, SoundEvent sound) {
		level.playSound(null, getBlockPos(), sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void flipPage(ServerPlayer player, short page) {
		NetworkHooks.openScreen(player, new PagedMenuProvider(page), p -> {
			p.writeBlockPos(this.worldPosition);
			p.writeVarInt(page);
		});
	}

	@Override
	public void openGui(ServerPlayer player, BlockPos pos) {
		NetworkHooks.openScreen(player, new PagedMenuProvider(0), p -> {
			p.writeBlockPos(this.worldPosition);
			p.writeVarInt(0);
		});
	}

	/* IStreamable */
	@Override
	public void writeData(FriendlyByteBuf data) {
		data.writeInt(numPlayersUsing);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(FriendlyByteBuf data) {
		numPlayersUsing = data.readInt();
	}

	//TODO page stuff.
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new ContainerNaturalistInventory(windowId, inv, this, 5);
	}

	public IForestrySpeciesRoot getSpeciesRoot() {
		return speciesRoot;
	}

	// ensures ContainerNaturalistInventory.page is correct on the server side
	private class PagedMenuProvider implements MenuProvider {
		private final int page;

		private PagedMenuProvider(int page) {
			this.page = page;
		}

		@Override
		public Component getDisplayName() {
			return TileNaturalistChest.this.getDisplayName();
		}

		@Override
		public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
			return new ContainerNaturalistInventory(windowId, playerInv, TileNaturalistChest.this, page);
		}
	}
}
