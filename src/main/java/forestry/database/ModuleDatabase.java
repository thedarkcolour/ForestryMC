package forestry.database;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.IClientModuleHandler;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IPacketRegistry;
import forestry.core.network.PacketIdServer;
import forestry.database.client.DatabaseClientHandler;
import forestry.database.network.packets.PacketExtractItem;
import forestry.database.network.packets.PacketInsertItem;
import forestry.modules.BlankForestryModule;

@ForestryModule
public class ModuleDatabase extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.DATABASE;
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.INSERT_ITEM, PacketInsertItem.class, PacketInsertItem::decode, PacketInsertItem::handle);
		registry.serverbound(PacketIdServer.EXTRACT_ITEM, PacketExtractItem.class, PacketExtractItem::decode, PacketExtractItem::handle);
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new DatabaseClientHandler());
	}
}
