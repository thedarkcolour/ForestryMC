package forestry.core.network.packets;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;
import forestry.core.utils.NetworkUtil;

public record PacketClimateListenerUpdate(BlockPos pos, @Nullable ClimateState state) implements IForestryPacketClient {
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
		// todo climate manager
		//ClimateRoot.getInstance().getListener(player.level, msg.pos).ifPresent(l -> l.setClimateState(msg.state));
	}
}
