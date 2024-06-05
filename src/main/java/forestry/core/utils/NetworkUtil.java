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
package forestry.core.utils;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import forestry.api.climate.IClimateState;
import forestry.core.climate.AbsentClimateState;
import forestry.core.climate.ClimateStateHelper;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.IForestryPacketServer;
import forestry.core.network.IStreamable;
import forestry.core.network.NetworkHandler;

public class NetworkUtil {
	public static <P extends IForestryPacketClient> void sendNetworkPacket(P packet, BlockPos pos, Level level) {
		NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), packet);
	}

	public static void sendToPlayer(IForestryPacketClient packet, ServerPlayer player) {
		NetworkHandler.CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	// todo implement
	public static void inventoryChangeNotify(Player player, AbstractContainerMenu container) {
		if (player instanceof ServerPlayer) {
			throw new RuntimeException("Unimplemented");
			// ((ServerPlayer) player).refreshContainer(container);
		}
	}

	// Used for Streamable to prepare FriendlyByteBuf for sending over the network
	public static void writePayloadBuffer(FriendlyByteBuf buffer, Consumer<FriendlyByteBuf> dataWriter) {
		// write a placeholder value for the number of bytes, keeping its index for replacing later
		int dataBytesIndex = buffer.writerIndex();
		buffer.writeInt(0);
		// write data bytes
		dataWriter.accept(buffer);
		// replace placeholder with length of data bytes, not including length integer
		int numDataBytes = buffer.writerIndex() - dataBytesIndex - 4;
		buffer.setInt(dataBytesIndex, numDataBytes);
	}

	// Used for Streamable to read FriendlyByteBuf for receiving from the network
	public static FriendlyByteBuf readPayloadBuffer(FriendlyByteBuf buffer) {
		return new FriendlyByteBuf(buffer.readBytes(buffer.readInt()));
	}

	public static void sendToServer(IForestryPacketServer packet) {
		NetworkHandler.CHANNEL.sendToServer(packet);
	}

	public static void writeItemStacks(FriendlyByteBuf buffer, NonNullList<ItemStack> itemStacks) {
		buffer.writeVarInt(itemStacks.size());
		for (ItemStack stack : itemStacks) {
			buffer.writeItem(stack);
		}
	}

	public static NonNullList<ItemStack> readItemStacks(FriendlyByteBuf buffer) {
		int stackCount = buffer.readVarInt();
		NonNullList<ItemStack> itemStacks = NonNullList.create();
		for (int i = 0; i < stackCount; i++) {
			itemStacks.add(buffer.readItem());
		}
		return itemStacks;
	}

	public static void writeInventory(FriendlyByteBuf buffer, Container inventory) {
		int size = inventory.getContainerSize();
		buffer.writeVarInt(size);

		for (int i = 0; i < size; i++) {
			ItemStack stack = inventory.getItem(i);
			buffer.writeItem(stack);
		}
	}

	public static void readInventory(FriendlyByteBuf buffer, Container inventory) {
		int size = buffer.readVarInt();

		for (int i = 0; i < size; i++) {
			ItemStack stack = buffer.readItem();
			inventory.setItem(i, stack);
		}
	}

	// Assumes Enum.values().length < Byte.MAX_VALUE
	public static <T extends Enum<T>> void writeEnum(FriendlyByteBuf buffer, T enumValue) {
		buffer.writeByte(enumValue.ordinal());
	}

	public static <T extends Enum<T>> T readEnum(FriendlyByteBuf buffer, T[] enumValues) {
		Preconditions.checkArgument(enumValues.length < Byte.MAX_VALUE);
		return enumValues[buffer.readByte()];
	}

	public static void writeStreamable(FriendlyByteBuf buffer, @Nullable IStreamable streamable) {
		if (streamable != null) {
			buffer.writeBoolean(true);
			streamable.writeData(buffer);
		} else {
			buffer.writeBoolean(false);
		}
	}

	@Nullable
	public static <T extends IStreamable> T readStreamable(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> factory) {
		if (buffer.readBoolean()) {
			return factory.apply(buffer);
		}
		return null;
	}

	public static <T extends IStreamable> void writeStreamables(FriendlyByteBuf buffer, @Nullable List<T> streamables) {
		if (streamables == null) {
			buffer.writeVarInt(0);
		} else {
			buffer.writeVarInt(streamables.size());
			for (IStreamable streamable : streamables) {
				writeStreamable(buffer, streamable);
			}
		}
	}

	public static <T extends IStreamable> void readStreamables(FriendlyByteBuf buffer, List<T> outputList, Function<FriendlyByteBuf, T> factory) {
		outputList.clear();
		int length = buffer.readVarInt();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				T streamable = readStreamable(buffer, factory);
				outputList.add(streamable);
			}
		}
	}

	public static void writeClimateState(FriendlyByteBuf buffer, IClimateState climateState) {
		if (climateState.isPresent()) {
			buffer.writeBoolean(true);
			buffer.writeFloat(climateState.getTemperature());
			buffer.writeFloat(climateState.getHumidity());
			buffer.writeBoolean(climateState.isMutable());
		} else {
			buffer.writeBoolean(false);
		}
	}

	public static IClimateState readClimateState(FriendlyByteBuf buffer) {
		if (buffer.readBoolean()) {
			return ClimateStateHelper.of(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean());
		} else {
			return AbsentClimateState.INSTANCE;
		}
	}

	public static void writeBlockState(FriendlyByteBuf buffer, BlockState state) {
		buffer.writeId(Block.BLOCK_STATE_REGISTRY, state);
	}

	public static BlockState readBlockState(FriendlyByteBuf buffer) {
		return buffer.readById(Block.BLOCK_STATE_REGISTRY);
	}
}
