package forestry.worktable.features;

import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import forestry.worktable.ModuleWorktable;
import forestry.worktable.screens.WorktableMenu;

@FeatureProvider
public class WorktableMenus {
	public static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleWorktable.class);

	public static final FeatureMenuType<WorktableMenu> WORKTABLE = REGISTRY.menuType(WorktableMenu::fromNetwork, "worktable");
}
