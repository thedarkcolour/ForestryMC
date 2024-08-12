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

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;

public record PacketHabitatBiomePointer(BlockPos pos) implements IForestryPacketClient {
	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	@Override
	public ResourceLocation id() {
		return PacketIdClient.HABITAT_BIOME_POINTER;
	}

	public static PacketHabitatBiomePointer decode(FriendlyByteBuf buffer) {
		return new PacketHabitatBiomePointer(buffer.readBlockPos());
	}

	public static void handle(PacketHabitatBiomePointer msg, Player player) {
		BlockPos pos = msg.pos();
		//TextureHabitatLocator.getInstance().setTargetCoordinates(pos);//TODO: TextureHabitatLocator
	}
}
