package forestry.arboriculture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import forestry.api.arboriculture.ICharcoalManager;
import forestry.api.arboriculture.TreeManager;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.arboriculture.charcoal.CharcoalManager;
import forestry.modules.BlankForestryModule;

@ForestryModule
public class ModuleCharcoal extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CHARCOAL;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ModuleCharcoal::commonSetup);
	}

	@Override
	public void setupApi() {
		TreeManager.charcoalManager = new CharcoalManager();
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ICharcoalManager manager = TreeManager.charcoalManager;
			if (manager != null) {
				manager.registerWall(Blocks.CLAY, 3);
				manager.registerWall(Blocks.END_STONE, 6);
				manager.registerWall(Blocks.END_STONE_BRICKS, 6);
				manager.registerWall(Blocks.DIRT, 2);
				manager.registerWall(Blocks.GRAVEL, 1);
				manager.registerWall(Blocks.NETHERRACK, 3);
			}
		});
	}
}
