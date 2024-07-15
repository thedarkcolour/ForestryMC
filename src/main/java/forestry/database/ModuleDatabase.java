package forestry.database;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.modules.IPacketRegistry;
import forestry.core.network.PacketIdServer;
import forestry.database.features.DatabaseMenuTypes;
import forestry.database.gui.GuiDatabase;
import forestry.database.network.packets.PacketExtractItem;
import forestry.database.network.packets.PacketInsertItem;
import forestry.modules.BlankForestryModule;
import forestry.api.modules.ForestryModuleIds;

public class ModuleDatabase extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.DATABASE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerMenuScreens() {
		MenuScreens.register(DatabaseMenuTypes.DATABASE.menuType(), GuiDatabase::new);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.INSERT_ITEM, PacketInsertItem.class, PacketInsertItem::decode, PacketInsertItem::handle);
		registry.serverbound(PacketIdServer.EXTRACT_ITEM, PacketExtractItem.class, PacketExtractItem::decode, PacketExtractItem::handle);
	}
}
