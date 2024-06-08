package forestry.worktable.features;

import forestry.core.items.ItemBlockForestry;
import forestry.modules.features.FeatureBlock;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import forestry.worktable.ModuleWorktable;
import forestry.worktable.blocks.WorktableBlock;
import forestry.worktable.blocks.WorktableBlockType;

@FeatureProvider
public class WorktableBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleWorktable.class);

	public static final FeatureBlock<WorktableBlock, ItemBlockForestry<?>> WORKTABLE = REGISTRY.block(() -> new WorktableBlock(WorktableBlockType.WORKTABLE), ItemBlockForestry::new, "worktable");
}
