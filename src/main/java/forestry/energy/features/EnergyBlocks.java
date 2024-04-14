package forestry.energy.features;

import forestry.core.items.ItemBlockBase;
import forestry.energy.ModuleEnergy;
import forestry.energy.blocks.EngineBlock;
import forestry.energy.blocks.EngineBlockType;
import forestry.modules.features.FeatureBlockGroup;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class EnergyBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleEnergy.class);

	public static final FeatureBlockGroup<EngineBlock, EngineBlockType> ENGINES = REGISTRY.blockGroup(EngineBlock::new, EngineBlockType.VALUES).itemWithType(ItemBlockBase::new).identifier("engine").create();
}
