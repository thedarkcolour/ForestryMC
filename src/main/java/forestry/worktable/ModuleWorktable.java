package forestry.worktable;

import net.minecraft.client.gui.screens.MenuScreens;

import forestry.api.modules.ForestryModule;
import forestry.core.config.Constants;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.worktable.features.WorktableMenus;
import forestry.worktable.network.packets.PacketWorktableMemoryUpdate;
import forestry.worktable.network.packets.PacketWorktableRecipeRequest;
import forestry.worktable.network.packets.PacketWorktableRecipeUpdate;
import forestry.worktable.screens.WorktableScreen;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.WORKTABLE, name = "Worktable", author = "thedarkcolour", url = Constants.URL, unlocalizedDescription = "for.module.worktable.description")
public class ModuleWorktable extends BlankForestryModule {
	@Override
	public void registerGuiFactories() {
		MenuScreens.register(WorktableMenus.WORKTABLE.menuType(), WorktableScreen::new);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.WORKTABLE_RECIPE_REQUEST, PacketWorktableRecipeRequest.class, PacketWorktableRecipeRequest::decode, PacketWorktableRecipeRequest::handle);

		registry.clientbound(PacketIdClient.WORKTABLE_MEMORY_UPDATE, PacketWorktableMemoryUpdate.class, PacketWorktableMemoryUpdate::decode, PacketWorktableMemoryUpdate::handle);
		registry.clientbound(PacketIdClient.WORKTABLE_CRAFTING_UPDATE, PacketWorktableRecipeUpdate.class, PacketWorktableRecipeUpdate::decode, PacketWorktableRecipeUpdate::handle);
	}
}
