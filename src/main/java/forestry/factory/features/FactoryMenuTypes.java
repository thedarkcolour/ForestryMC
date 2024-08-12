package forestry.factory.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.factory.gui.ContainerBottler;
import forestry.factory.gui.ContainerCarpenter;
import forestry.factory.gui.ContainerCentrifuge;
import forestry.factory.gui.ContainerFabricator;
import forestry.factory.gui.ContainerFermenter;
import forestry.factory.gui.ContainerMoistener;
import forestry.factory.gui.ContainerRaintank;
import forestry.factory.gui.ContainerSqueezer;
import forestry.factory.gui.ContainerStill;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class FactoryMenuTypes {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.FACTORY);

	public static final FeatureMenuType<ContainerBottler> BOTTLER = REGISTRY.menuType(ContainerBottler::fromNetwork, "bottler");
	public static final FeatureMenuType<ContainerCarpenter> CARPENTER = REGISTRY.menuType(ContainerCarpenter::fromNetwork, "carpenter");
	public static final FeatureMenuType<ContainerCentrifuge> CENTRIFUGE = REGISTRY.menuType(ContainerCentrifuge::fromNetwork, "centrifuge");
	public static final FeatureMenuType<ContainerFabricator> FABRICATOR = REGISTRY.menuType(ContainerFabricator::fromNetwork, "fabricator");
	public static final FeatureMenuType<ContainerFermenter> FERMENTER = REGISTRY.menuType(ContainerFermenter::fromNetwork, "fermenter");
	public static final FeatureMenuType<ContainerMoistener> MOISTENER = REGISTRY.menuType(ContainerMoistener::fromNetwork, "moistener");
	public static final FeatureMenuType<ContainerRaintank> RAINTANK = REGISTRY.menuType(ContainerRaintank::fromNetwork, "raintank");
	public static final FeatureMenuType<ContainerSqueezer> SQUEEZER = REGISTRY.menuType(ContainerSqueezer::fromNetwork, "squeezer");
	public static final FeatureMenuType<ContainerStill> STILL = REGISTRY.menuType(ContainerStill::fromNetwork, "still");
}
