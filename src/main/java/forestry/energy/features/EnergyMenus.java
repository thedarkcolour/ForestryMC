package forestry.energy.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.energy.menu.BiogasEngineMenu;
import forestry.energy.menu.PeatEngineMenu;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class EnergyMenus {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.ENERGY);

	public static final FeatureMenuType<BiogasEngineMenu> ENGINE_BIOGAS = REGISTRY.menuType(BiogasEngineMenu::fromNetwork, "engine_biogas");
	public static final FeatureMenuType<PeatEngineMenu> ENGINE_PEAT = REGISTRY.menuType(PeatEngineMenu::fromNetwork, "engine_peat");
}
