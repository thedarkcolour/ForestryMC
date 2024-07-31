package forestry.database.client;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.database.features.DatabaseMenuTypes;
import forestry.database.gui.GuiDatabase;

public class DatabaseClientHandler implements forestry.api.client.IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(DatabaseClientHandler::setupClient);
	}

	private static void setupClient(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(DatabaseMenuTypes.DATABASE.menuType(), GuiDatabase::new);
		});
	}
}
