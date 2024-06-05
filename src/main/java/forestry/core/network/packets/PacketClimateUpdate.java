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

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateTransformer;
import forestry.core.climate.ClimateTransformer;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.PacketIdClient;
import forestry.core.tiles.TileUtil;

public record PacketClimateUpdate(BlockPos pos, ClimateTransformer transformer) implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.UPDATE_CLIMATE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		transformer.writeData(buffer);
	}

	public static PacketClimateUpdate decode(FriendlyByteBuf buffer) {
		return new PacketClimateUpdate(buffer.readBlockPos(), new ClimateTransformer(buffer));
	}

	public static void handle(PacketClimateUpdate msg, Player player) {
		IClimateHousing housing = TileUtil.getTile(player.level, msg.pos, IClimateHousing.class);
		if (housing == null) {
			return;
		}
		IClimateTransformer transformer = housing.getTransformer();
		if (transformer instanceof ClimateTransformer climateTransformer) {
			climateTransformer.copy(msg.transformer);
		}
	}
}
