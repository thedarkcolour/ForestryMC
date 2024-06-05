package forestry.database;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.modules.ForestryModule;
import forestry.core.config.Constants;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdServer;
import forestry.database.features.DatabaseMenuTypes;
import forestry.database.gui.GuiDatabase;
import forestry.database.network.packets.PacketExtractItem;
import forestry.database.network.packets.PacketInsertItem;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.DATABASE, name = "Database", author = "Nedelosk", url = Constants.URL, unlocalizedDescription = "for.module.database.description")
public class ModuleDatabase extends BlankForestryModule {
	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerGuiFactories() {
		MenuScreens.register(DatabaseMenuTypes.DATABASE.menuType(), GuiDatabase::new);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.INSERT_ITEM, PacketInsertItem.class, PacketInsertItem::decode, PacketInsertItem::handle);
		registry.serverbound(PacketIdServer.EXTRACT_ITEM, PacketExtractItem.class, PacketExtractItem::decode, PacketExtractItem::handle);
	}
}
