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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import forestry.api.IForestryApi;
import forestry.api.genetics.ISpeciesType;
import forestry.core.gui.ContainerItemInventory;
import forestry.core.gui.ContainerNaturalistInventory;
import forestry.core.gui.IGuiSelectable;
import forestry.core.gui.INaturalistMenu;
import forestry.storage.features.BackpackMenuTypes;
import forestry.storage.inventory.ItemInventoryBackpackPaged;
import forestry.storage.items.ItemBackpack;

public class ContainerNaturalistBackpack extends ContainerItemInventory<ItemInventoryBackpackPaged> implements IGuiSelectable, INaturalistMenu {
	private final int currentPage;
	private final ISpeciesType<?, ?> speciesRoot;

	public ContainerNaturalistBackpack(int windowId, Inventory inv, ItemInventoryBackpackPaged inventory, int selectedPage, ResourceLocation rootUid) {
		super(windowId, inventory, inv, 18, 120, BackpackMenuTypes.NATURALIST_BACKPACK.menuType());

		ContainerNaturalistInventory.addInventory(this, inventory, selectedPage);

		this.currentPage = selectedPage;
		this.speciesRoot = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(rootUid);
	}

	public static ContainerNaturalistBackpack makeContainer(int windowId, Player player, ItemStack heldItem, int page, ResourceLocation typeId) {
		ItemInventoryBackpackPaged inventory = new ItemInventoryBackpackPaged(player, ItemBackpack.SLOTS_BACKPACK_APIARIST, heldItem, typeId);
		return new ContainerNaturalistBackpack(windowId, player.getInventory(), inventory, page, typeId);
	}

	@Override
	public void handleSelectionRequest(ServerPlayer player, int primary, int secondary) {
		inventory.flipPage(player, (short) primary);
	}

	@Override
	public ISpeciesType<?, ?> getSpeciesType() {
		return this.speciesRoot;
	}

	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}

	public static ContainerNaturalistBackpack fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
		int page = buffer.readByte();
		ResourceLocation typeId = buffer.readResourceLocation();
		ItemStack parent = playerInventory.getSelected();

		return makeContainer(windowId, playerInventory.player, parent, page, typeId);
	}
}
