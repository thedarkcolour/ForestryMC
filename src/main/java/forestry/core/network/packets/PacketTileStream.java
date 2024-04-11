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

import java.io.IOException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.core.network.ForestryPacket;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.IForestryPacketHandlerClient;
import forestry.core.network.IStreamable;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.PacketIdClient;
import forestry.core.tiles.TileUtil;

import javax.annotation.Nullable;

public class PacketTileStream extends ForestryPacket implements IForestryPacketClient {
	private final BlockPos pos;
	@Nullable
	private final IStreamable streamable;
	@Nullable
	private final PacketBufferForestry payload;

	public <T extends BlockEntity & IStreamable> PacketTileStream(T streamable) {
		this.pos = streamable.getBlockPos();
		this.streamable = streamable;
		this.payload = null;
	}

	private PacketTileStream(BlockPos pos, PacketBufferForestry payload) {
		this.pos = pos;
		this.streamable = null;
		this.payload = payload;
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.TILE_FORESTRY_UPDATE;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		// write a placeholder value for the number of bytes, keeping its index for replacing later
		int dataBytesIndex = data.writerIndex();
		data.writeInt(0);
		// write data bytes
		streamable.writeData(data);
		// replace placeholder with length of data bytes, not including length integer
		int numDataBytes = data.writerIndex() - dataBytesIndex - 4;
		data.setInt(dataBytesIndex, numDataBytes);
	}

	public static PacketTileStream decode(FriendlyByteBuf data) {
		return new PacketTileStream(data.readBlockPos(), new PacketBufferForestry(data.readBytes(data.readInt())));
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			PacketTileStream packet = decode(data);
			IStreamable tile = TileUtil.getTile(player.level, packet.pos, IStreamable.class);

			if (tile != null) {
				tile.readData(packet.payload);
			}
		}
	}
}
