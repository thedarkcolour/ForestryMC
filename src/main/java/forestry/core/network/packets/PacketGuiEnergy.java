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
import net.minecraft.world.entity.player.Player;

import forestry.core.gui.ContainerTile;
import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;

public record PacketGuiEnergy(int windowId, int value) implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.GUI_ENERGY;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(windowId);
		buffer.writeVarInt(value);
	}

	public static PacketGuiEnergy decode(FriendlyByteBuf buffer) {
		return new PacketGuiEnergy(buffer.readVarInt(), buffer.readVarInt());
	}

	public static void handle(PacketGuiEnergy msg, Player player) {
		if (player.containerMenu.containerId == msg.windowId && player.containerMenu instanceof ContainerTile<?> menu) {
			menu.onGuiEnergy(msg.value);
		}
	}
}
