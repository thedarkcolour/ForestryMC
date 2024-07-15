package forestry.arboriculture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.ICharcoalManager;
import forestry.api.arboriculture.TreeManager;
import forestry.api.core.ItemGroups;
import forestry.api.modules.ForestryModule;
import forestry.arboriculture.charcoal.CharcoalManager;
import forestry.core.config.Constants;
import forestry.modules.BlankForestryModule;
import forestry.api.modules.ForestryModuleIds;

public class ModuleCharcoal extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CHARCOAL;
	}

	@Override
	public void setupApi() {
		TreeManager.charcoalManager = new CharcoalManager();
	}

	@Override
	public void postInit() {
		ICharcoalManager manager = TreeManager.charcoalManager;
		if (manager != null) {
			manager.registerWall(Blocks.CLAY, 3);
			manager.registerWall(Blocks.END_STONE, 6);
			manager.registerWall(Blocks.END_STONE_BRICKS, 6);
			manager.registerWall(Blocks.DIRT, 2);
			manager.registerWall(Blocks.GRAVEL, 1);
			manager.registerWall(Blocks.NETHERRACK, 3);
		}
	}

	public static CreativeModeTab getGroup() {
		return ItemGroups.tabArboriculture;
	}
}
