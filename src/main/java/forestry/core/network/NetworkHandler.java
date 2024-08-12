package forestry.core.network;

import java.util.function.BiConsumer;
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
import forestry.api.ForestryConstants;
import forestry.api.IForestryApi;
import forestry.api.modules.IForestryPacket;
import forestry.api.modules.IForestryPacketClient;
import forestry.api.modules.IForestryPacketServer;
import forestry.api.modules.IPacketRegistry;

public class NetworkHandler {
	public static final ResourceLocation CHANNEL_ID = ForestryConstants.forestry("channel");
	// todo correspond to mod version
	public static final String VERSION = "1.0.0";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(CHANNEL_ID, () -> VERSION, VERSION::equals, VERSION::equals);

	public static void register() {
		IPacketRegistry registry = new NetworkHandler.PacketRegistry(CHANNEL);

		// packets must be registered in the same order across runs (until 1.20.4)
		IForestryApi.INSTANCE.getModuleManager().getLoadedModules().forEach(module -> module.registerPackets(registry));
	}

	private static final class PacketRegistry implements IPacketRegistry {
		private final SimpleChannel channel;
		private int packetId;

		private PacketRegistry(SimpleChannel channel) {
			this.channel = channel;
		}

		@Override
		public <P extends IForestryPacketServer> void serverbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> packetHandler) {
			this.channel.registerMessage(this.packetId++, packetClass, IForestryPacket::write, decoder, (msg, ctxSupplier) -> handleServerbound(msg, ctxSupplier, packetHandler));
		}

		@Override
		public <P extends IForestryPacketClient> void clientbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, Player> packetHandler) {
			this.channel.registerMessage(this.packetId++, packetClass, IForestryPacket::write, decoder, (msg, ctxSupplier) -> handleClientbound(msg, ctxSupplier, packetHandler));
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
