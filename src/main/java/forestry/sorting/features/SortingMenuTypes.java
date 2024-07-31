package forestry.sorting.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import forestry.sorting.gui.ContainerGeneticFilter;

@FeatureProvider
public class SortingMenuTypes {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.SORTING);

	public static final FeatureMenuType<ContainerGeneticFilter> GENETIC_FILTER = REGISTRY.menuType(ContainerGeneticFilter::fromNetwork, "genetic_filter");
}
