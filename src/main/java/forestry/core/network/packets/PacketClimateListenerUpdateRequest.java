package forestry.core.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.climate.IClimateListener;
import forestry.api.modules.IForestryPacketServer;
import forestry.core.network.PacketIdServer;

public record PacketClimateListenerUpdateRequest(BlockPos pos) implements IForestryPacketServer {
	@Override
	public ResourceLocation id() {
		return PacketIdServer.CLIMATE_LISTENER_UPDATE_REQUEST;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	public static PacketClimateListenerUpdateRequest decode(FriendlyByteBuf buffer) {
		return new PacketClimateListenerUpdateRequest(buffer.readBlockPos());
	}

	public static void handle(PacketClimateListenerUpdateRequest msg, ServerPlayer player) {
		BlockEntity tileEntity = player.level.getBlockEntity(msg.pos());
		if (tileEntity != null) {
			LazyOptional<IClimateListener> listener = tileEntity.getCapability(ForestryCapabilities.CLIMATE_LISTENER);
			listener.ifPresent(l -> l.syncToClient(player));
		}
	}
}
