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
package forestry.core.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import forestry.core.gui.IGuiSelectable;
import forestry.api.modules.IForestryPacketServer;
import forestry.core.network.PacketIdServer;

public record PacketGuiSelectRequest(int primaryIndex, int secondaryIndex) implements IForestryPacketServer {
	public static void handle(PacketGuiSelectRequest msg, ServerPlayer player) {
		AbstractContainerMenu container = player.containerMenu;

		if (container instanceof IGuiSelectable guiSelectable) {
			guiSelectable.handleSelectionRequest(player, msg.primaryIndex(), msg.secondaryIndex());
		}
	}

	@Override
	public ResourceLocation id() {
		return PacketIdServer.GUI_SELECTION_REQUEST;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(primaryIndex);
		buffer.writeVarInt(secondaryIndex);
	}

	public static PacketGuiSelectRequest decode(FriendlyByteBuf buffer) {
		return new PacketGuiSelectRequest(buffer.readVarInt(), buffer.readVarInt());
	}
}
