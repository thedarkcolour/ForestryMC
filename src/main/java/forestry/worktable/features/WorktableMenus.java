package forestry.worktable.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import forestry.worktable.screens.WorktableMenu;

@FeatureProvider
public class WorktableMenus {
	public static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.WORKTABLE);

	public static final FeatureMenuType<WorktableMenu> WORKTABLE = REGISTRY.menuType(WorktableMenu::fromNetwork, "worktable");
}
