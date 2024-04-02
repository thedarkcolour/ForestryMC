package forestry.database.features;

import forestry.database.ModuleDatabase;
import forestry.database.gui.ContainerDatabase;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class DatabaseContainers {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleDatabase.class);

	public static final FeatureMenuType<ContainerDatabase> DATABASE = REGISTRY.container(ContainerDatabase::fromNetwork, "database");
}
