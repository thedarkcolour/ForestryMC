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
package forestry.storage.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.network.NetworkHooks;

import forestry.core.gui.IPagedInventory;
import forestry.storage.gui.ContainerNaturalistBackpack;

public class ItemInventoryBackpackPaged extends ItemInventoryBackpack implements IPagedInventory {
	private final ResourceLocation typeId;

	public ItemInventoryBackpackPaged(Player player, int size, ItemStack itemstack, ResourceLocation typeId) {
		super(player, size, itemstack);
		this.typeId = typeId;
	}

	@Override
	public void flipPage(ServerPlayer player, short page) {
		ItemStack backpack = getParent();
		SimpleMenuProvider provider = new SimpleMenuProvider((windowId, playerInv, p) -> ContainerNaturalistBackpack.makeContainer(windowId, p, backpack, page, this.typeId), backpack.getHoverName());
		NetworkHooks.openScreen(player, provider, buffer -> {
			buffer.writeByte(page);
			buffer.writeResourceLocation(this.typeId);
		});
	}
}
