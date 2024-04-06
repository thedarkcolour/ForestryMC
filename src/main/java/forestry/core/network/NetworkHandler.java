package forestry.core.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.core.ClientsideCode;

public class NetworkHandler {

	private final EventNetworkChannel channel;

	public NetworkHandler() {
		channel = NetworkRegistry.newEventChannel(PacketHandlerServer.CHANNEL_ID, () -> PacketHandlerServer.VERSION, s -> s.equals(PacketHandlerServer.VERSION), s -> s.equals(PacketHandlerServer.VERSION));

		PacketHandlerServer packetHandlerServer = new PacketHandlerServer();
		channel.addListener(packetHandlerServer::onPacket);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			ClientsideCode.initClientPacketHandler(channel);
		}
	}
}
