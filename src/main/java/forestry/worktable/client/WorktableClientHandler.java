package forestry.worktable.client;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.client.IClientModuleHandler;
import forestry.worktable.features.WorktableMenus;
import forestry.worktable.screens.WorktableScreen;

public class WorktableClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(WorktableClientHandler::onClientSetup);
	}

	private static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(WorktableMenus.WORKTABLE.menuType(), WorktableScreen::new));
	}
}
