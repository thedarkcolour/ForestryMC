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
package forestry.mail.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import forestry.api.mail.EnumAddressee;
import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketIdServer;
import forestry.core.utils.NetworkUtil;
import forestry.mail.gui.ContainerLetter;

public record PacketLetterInfoRequest(String recipientName, EnumAddressee addressType) implements IForestryPacketServer {
	public static void handle(PacketLetterInfoRequest msg, ServerPlayer player) {
		if (player.containerMenu instanceof ContainerLetter containerLetter) {
			containerLetter.handleRequestLetterInfo(player, msg.recipientName(), msg.addressType());
		}
	}

	@Override
	public ResourceLocation id() {
		return PacketIdServer.LETTER_INFO_REQUEST;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(recipientName);
		NetworkUtil.writeEnum(buffer, addressType);
	}

	public static PacketLetterInfoRequest decode(FriendlyByteBuf buffer) {
		return new PacketLetterInfoRequest(buffer.readUtf(), buffer.readEnum(EnumAddressee.class));
	}
}
