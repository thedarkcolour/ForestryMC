package forestry.apiculture.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.apiculture.gui.ContainerAlveary;
import forestry.apiculture.gui.ContainerAlvearyHygroregulator;
import forestry.apiculture.gui.ContainerAlvearySieve;
import forestry.apiculture.gui.ContainerAlvearySwarmer;
import forestry.apiculture.gui.ContainerBeeHousing;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class ApicultureMenuTypes {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.APICULTURE);

	public static final FeatureMenuType<ContainerAlveary> ALVEARY = REGISTRY.menuType(ContainerAlveary::fromNetwork, "alveary");
	public static final FeatureMenuType<ContainerAlvearyHygroregulator> ALVEARY_HYGROREGULATOR = REGISTRY.menuType(ContainerAlvearyHygroregulator::fromNetwork, "alveary_hygroregulator");
	public static final FeatureMenuType<ContainerAlvearySieve> ALVEARY_SIEVE = REGISTRY.menuType(ContainerAlvearySieve::fromNetwork, "alveary_sieve");
	public static final FeatureMenuType<ContainerAlvearySwarmer> ALVEARY_SWARMER = REGISTRY.menuType(ContainerAlvearySwarmer::fromNetwork, "alveary_swarmer");
	public static final FeatureMenuType<ContainerBeeHousing> BEE_HOUSING = REGISTRY.menuType(ContainerBeeHousing::fromNetwork, "bee_housing");
}
