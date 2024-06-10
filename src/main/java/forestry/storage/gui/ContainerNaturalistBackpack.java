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
package forestry.storage.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IForestrySpeciesRoot;
import forestry.core.config.Constants;
import forestry.core.gui.ContainerItemInventory;
import forestry.core.gui.ContainerNaturalistInventory;
import forestry.core.gui.IGuiSelectable;
import forestry.core.gui.INaturalistMenu;
import forestry.storage.features.BackpackMenuTypes;
import forestry.storage.inventory.ItemInventoryBackpackPaged;
import forestry.storage.items.ItemBackpackNaturalist;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IIndividual;

public class ContainerNaturalistBackpack extends ContainerItemInventory<ItemInventoryBackpackPaged> implements IGuiSelectable, INaturalistMenu {
	private final int currentPage;
	private final IForestrySpeciesRoot<IIndividual> speciesRoot;

	public ContainerNaturalistBackpack(int windowId, Inventory inv, ItemInventoryBackpackPaged inventory, int selectedPage, String rootUid) {
		super(windowId, inventory, inv, 18, 120, BackpackMenuTypes.NATURALIST_BACKPACK.menuType());

		ContainerNaturalistInventory.addInventory(this, inventory, selectedPage);

		this.currentPage = selectedPage;
		this.speciesRoot = (IForestrySpeciesRoot<IIndividual>) GeneticsAPI.apiInstance.getRoot(rootUid).get();
	}

	@Override
	public void handleSelectionRequest(ServerPlayer player, int primary, int secondary) {
		inventory.flipPage(player, (short) primary);
	}

	@Override
	public IForestrySpeciesRoot<IIndividual> getSpeciesRoot() {
		return this.speciesRoot;
	}

	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}

	public static ContainerNaturalistBackpack fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
		ItemStack parent = buffer.readItem();
		ItemBackpackNaturalist backpack = (ItemBackpackNaturalist) parent.getItem();
		ItemInventoryBackpackPaged paged = new ItemInventoryBackpackPaged(playerInventory.player, Constants.SLOTS_BACKPACK_APIARIST, parent, backpack);
		int page = buffer.readByte();
		return new ContainerNaturalistBackpack(windowId, playerInventory, paged, page, buffer.readUtf());
	}
}
