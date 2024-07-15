package forestry.core.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.climate.IClimatised;
import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;
import forestry.core.utils.NetworkUtil;

public record PacketClimateListenerUpdate(BlockPos pos, IClimatised state) implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.CLIMATE_LISTENER_UPDATE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		NetworkUtil.writeClimateState(buffer, state);
	}

	public static PacketClimateListenerUpdate decode(FriendlyByteBuf buffer) {
		return new PacketClimateListenerUpdate(buffer.readBlockPos(), NetworkUtil.readClimateState(buffer));
	}

	public static void handle(PacketClimateListenerUpdate msg, Player player) {
		ClimateRoot.getInstance().getListener(player.level, msg.pos).ifPresent(l -> l.setClimateState(msg.state));
	}
}
