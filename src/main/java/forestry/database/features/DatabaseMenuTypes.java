package forestry.database.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.database.gui.ContainerDatabase;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class DatabaseMenuTypes {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.DATABASE);

	public static final FeatureMenuType<ContainerDatabase> DATABASE = REGISTRY.menuType(ContainerDatabase::fromNetwork, "database");
}
