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
package forestry.climatology.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateState;
import forestry.api.climate.IClimateTransformer;
import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketIdServer;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.NetworkUtil;

public record PacketSelectClimateTargeted(BlockPos pos, IClimateState climateState) implements IForestryPacketServer {
	@Override
	public ResourceLocation id() {
		return PacketIdServer.SELECT_CLIMATE_TARGETED;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		NetworkUtil.writeClimateState(buffer, climateState);
	}

	public static PacketSelectClimateTargeted decode(FriendlyByteBuf buffer) {
		return new PacketSelectClimateTargeted(buffer.readBlockPos(), NetworkUtil.readClimateState(buffer));
	}

	public static void handle(PacketSelectClimateTargeted msg, ServerPlayer player) {
		IClimateHousing housing = TileUtil.getTile(player.level, msg.pos(), IClimateHousing.class);
		if (housing != null) {
			IClimateTransformer transformer = housing.getTransformer();
			transformer.setTarget(msg.climateState());
		}
	}
}
