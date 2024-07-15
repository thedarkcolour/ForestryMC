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

import forestry.core.gui.IContainerLiquidTanks;
import forestry.api.modules.IForestryPacketServer;
import forestry.core.network.PacketIdServer;

public record PacketPipetteClick(int slot) implements IForestryPacketServer {
	public static void handle(PacketPipetteClick msg, ServerPlayer player) {
		if (player.containerMenu instanceof IContainerLiquidTanks tanksMenu) {
			tanksMenu.handlePipetteClick(msg.slot(), player);
		}
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(slot);
	}

	@Override
	public ResourceLocation id() {
		return PacketIdServer.PIPETTE_CLICK;
	}

	public static PacketPipetteClick decode(FriendlyByteBuf buffer) {
		return new PacketPipetteClick(buffer.readVarInt());
	}
}
