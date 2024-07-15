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
package forestry.apiculture.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.apiculture.gui.ContainerImprinter;
import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;

public record PacketImprintSelectionResponse(int primaryIndex, int secondaryIndex) implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.IMPRINT_SELECTION_RESPONSE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(primaryIndex);
		buffer.writeVarInt(secondaryIndex);
	}

	public static PacketImprintSelectionResponse decode(FriendlyByteBuf buffer) {
		return new PacketImprintSelectionResponse(buffer.readVarInt(), buffer.readVarInt());
	}

	public static void handle(PacketImprintSelectionResponse msg, Player player) {
		if (player.containerMenu instanceof ContainerImprinter imprinter) {
			imprinter.setSelection(msg.primaryIndex(), msg.secondaryIndex());
		}
	}
}
