package forestry.factory.features;

import forestry.factory.ModuleFactory;
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
public class FactoryContainers {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleFactory.class);

	public static final FeatureMenuType<ContainerBottler> BOTTLER = REGISTRY.container(ContainerBottler::fromNetwork, "bottler");
	public static final FeatureMenuType<ContainerCarpenter> CARPENTER = REGISTRY.container(ContainerCarpenter::fromNetwork, "carpenter");
	public static final FeatureMenuType<ContainerCentrifuge> CENTRIFUGE = REGISTRY.container(ContainerCentrifuge::fromNetwork, "centrifuge");
	public static final FeatureMenuType<ContainerFabricator> FABRICATOR = REGISTRY.container(ContainerFabricator::fromNetwork, "fabricator");
	public static final FeatureMenuType<ContainerFermenter> FERMENTER = REGISTRY.container(ContainerFermenter::fromNetwork, "fermenter");
	public static final FeatureMenuType<ContainerMoistener> MOISTENER = REGISTRY.container(ContainerMoistener::fromNetwork, "moistener");
	public static final FeatureMenuType<ContainerRaintank> RAINTANK = REGISTRY.container(ContainerRaintank::fromNetwork, "raintank");
	public static final FeatureMenuType<ContainerSqueezer> SQUEEZER = REGISTRY.container(ContainerSqueezer::fromNetwork, "squeezer");
	public static final FeatureMenuType<ContainerStill> STILL = REGISTRY.container(ContainerStill::fromNetwork, "still");
}
