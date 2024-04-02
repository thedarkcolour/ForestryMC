package forestry.cultivation.features;

import forestry.cultivation.ModuleCultivation;
import forestry.cultivation.gui.ContainerPlanter;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class CultivationContainers {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleCultivation.class);

	public static final FeatureMenuType<ContainerPlanter> PLANTER = REGISTRY.container(ContainerPlanter::fromNetwork, "planter");
}
