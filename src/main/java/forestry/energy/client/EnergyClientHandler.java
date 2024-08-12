package forestry.energy.client;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.energy.features.EnergyMenus;
import forestry.energy.screen.BiogasEngineScreen;
import forestry.energy.screen.PeatEngineScreen;

public class EnergyClientHandler implements forestry.api.client.IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(EnergyClientHandler::setupClient);
	}

	private static void setupClient(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(EnergyMenus.ENGINE_BIOGAS.menuType(), BiogasEngineScreen::new);
			MenuScreens.register(EnergyMenus.ENGINE_PEAT.menuType(), PeatEngineScreen::new);
		});
	}
}
