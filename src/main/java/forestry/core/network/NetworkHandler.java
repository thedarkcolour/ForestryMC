package forestry.core.network;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import forestry.Forestry;
import forestry.core.config.Constants;
import forestry.modules.ModuleManager;

public class NetworkHandler {
	public static final ResourceLocation CHANNEL_ID = new ResourceLocation(Constants.MOD_ID, "channel");
	// todo correspond to mod version
	public static final String VERSION = "1.0.0";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(CHANNEL_ID, () -> VERSION, VERSION::equals, VERSION::equals);

	public static void register() {
		// in case modules are ever parallelized
		IPacketRegistry registry = new NetworkHandler.PacketRegistry(new AtomicInteger());

		ModuleManager.getLoadedModules().forEach(module -> module.registerPackets(registry));
	}

	private record PacketRegistry(AtomicInteger packetId) implements IPacketRegistry {
		@Override
		public <P extends IForestryPacketServer> void serverbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> packetHandler) {
			CHANNEL.registerMessage(packetId.getAndIncrement(), packetClass, IForestryPacket::write, decoder, (msg, ctxSupplier) -> handleServerbound(msg, ctxSupplier, packetHandler));
		}

		@Override
		public <P extends IForestryPacketClient> void clientbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, Player> packetHandler) {
			CHANNEL.registerMessage(packetId.getAndIncrement(), packetClass, IForestryPacket::write, decoder, (msg, ctxSupplier) -> handleClientbound(msg, ctxSupplier, packetHandler));
		}
	}

	private static <T extends IForestryPacket> void handleServerbound(T message, Supplier<NetworkEvent.Context> ctxSupplier, BiConsumer<T, ServerPlayer> handler) {
		var ctx = ctxSupplier.get();

		if (ctx.getSender() == null) {
			Forestry.LOGGER.warn("the player was null, message: {}", message);
		} else {
			ctx.enqueueWork(() -> handler.accept(message, ctx.getSender()));
		}

		ctx.setPacketHandled(true);
	}

	private static <T extends IForestryPacket> void handleClientbound(T message, Supplier<NetworkEvent.Context> ctxSupplier, BiConsumer<T, Player> handler) {
		var ctx = ctxSupplier.get();
		ctx.enqueueWork(() -> handler.accept(message, Minecraft.getInstance().player));
		ctx.setPacketHandled(true);
	}
}
