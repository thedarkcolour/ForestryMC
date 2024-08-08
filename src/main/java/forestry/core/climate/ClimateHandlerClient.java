package forestry.core.climate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.LogicalSide;

import forestry.api.climate.IClimateProvider;
import forestry.core.render.ParticleRender;
import forestry.api.client.IClientModuleHandler;

// todo this is buggy and doesn't work across levels
public class ClimateHandlerClient implements IClientModuleHandler {
	// The current climate state at the position of the player.
	@Nullable
	private static IClimateProvider currentState = null;

	public static void setCurrentState(IClimateProvider currentState) {
		ClimateHandlerClient.currentState = currentState;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		MinecraftForge.EVENT_BUS.addListener(ClimateHandlerClient::onPlayerTick);
	}

	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.CLIENT) {
			return;
		}
		Player player = event.player;
		Level world = player.level;
		BlockPos pos = player.blockPosition();
		if (currentState != null) {
			int x = world.random.nextInt(11) - 5;
			int y = world.random.nextInt(5) - 1;
			int z = world.random.nextInt(11) - 5;
			ParticleRender.addClimateParticles(world, pos.offset(x, y, z), world.random, currentState.temperature(), currentState.humidity());
		}
	}
}
