package forestry.apiculture.features;

import forestry.apiculture.ModuleApiculture;
import forestry.apiculture.gui.ContainerAlveary;
import forestry.apiculture.gui.ContainerAlvearyHygroregulator;
import forestry.apiculture.gui.ContainerAlvearySieve;
import forestry.apiculture.gui.ContainerAlvearySwarmer;
import forestry.apiculture.gui.ContainerBeeHousing;
import forestry.apiculture.gui.ContainerHabitatLocator;
import forestry.apiculture.gui.ContainerImprinter;
import forestry.apiculture.gui.ContainerMinecartBeehouse;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class ApicultureContainers {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleApiculture.class);

	public static final FeatureMenuType<ContainerAlveary> ALVEARY = REGISTRY.container(ContainerAlveary::fromNetwork, "alveary");
	public static final FeatureMenuType<ContainerAlvearyHygroregulator> ALVEARY_HYGROREGULATOR = REGISTRY.container(ContainerAlvearyHygroregulator::fromNetwork, "alveary_hygroregulator");
	public static final FeatureMenuType<ContainerAlvearySieve> ALVEARY_SIEVE = REGISTRY.container(ContainerAlvearySieve::fromNetwork, "alveary_sieve");
	public static final FeatureMenuType<ContainerAlvearySwarmer> ALVEARY_SWARMER = REGISTRY.container(ContainerAlvearySwarmer::fromNetwork, "alveary_swarmer");
	public static final FeatureMenuType<ContainerBeeHousing> BEE_HOUSING = REGISTRY.container(ContainerBeeHousing::fromNetwork, "bee_housing");
	public static final FeatureMenuType<ContainerHabitatLocator> HABITAT_LOCATOR = REGISTRY.container(ContainerHabitatLocator::fromNetwork, "habitat_locator");
	public static final FeatureMenuType<ContainerImprinter> IMPRINTER = REGISTRY.container(ContainerImprinter::fromNetwork, "imprinter");
	public static final FeatureMenuType<ContainerMinecartBeehouse> BEEHOUSE_MINECART = REGISTRY.container(ContainerMinecartBeehouse::fromNetwork, "minecart_beehouse");
}
