package forestry.climatology;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.TickEvent;

import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimatised;
import forestry.core.climate.ForestryClimateManager;
import forestry.core.climate.WorldClimateHolder;
import forestry.core.network.packets.PacketClimatePlayer;
import forestry.core.utils.NetworkUtil;

// todo this doesn't work for multiple players
public class ClimateHandlerServer {
	private static final ForestryClimateManager MANAGER = (ForestryClimateManager) IForestryApi.INSTANCE.getClimateManager();
	@Nullable
	private static IClimatised previousState = null;

	static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;

			if (player.level instanceof ServerLevel level) {
				BlockPos pos = player.blockPosition();
				WorldClimateHolder worldClimateHolder = WorldClimateHolder.get(level);
				ClimateState climateState = worldClimateHolder.getAdjustedState(pos);

				if (level.getGameTime() % 100 == 0 && !climateState.equals(previousState)) {
					ClimateHandlerServer.previousState = climateState;
					NetworkUtil.sendToPlayer(new PacketClimatePlayer(climateState), (ServerPlayer) player);
				}
			}
		}
	}
}
