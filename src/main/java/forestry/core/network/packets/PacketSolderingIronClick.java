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
import net.minecraft.world.item.ItemStack;

import forestry.core.gui.IContainerSocketed;
import forestry.api.modules.IForestryPacketServer;
import forestry.core.network.PacketIdServer;

public record PacketSolderingIronClick(int slot) implements IForestryPacketServer {
	@Override
	public ResourceLocation id() {
		return PacketIdServer.SOLDERING_IRON_CLICK;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(slot);
	}

	public static PacketSolderingIronClick decode(FriendlyByteBuf buffer) {
		return new PacketSolderingIronClick(buffer.readVarInt());
	}

	public static void handle(PacketSolderingIronClick msg, ServerPlayer player) {
		if (player.containerMenu instanceof IContainerSocketed socketMenu) {
			ItemStack itemstack = player.containerMenu.getCarried();

			socketMenu.handleSolderingIronClickServer(msg.slot(), player, itemstack);
		}
	}
}
