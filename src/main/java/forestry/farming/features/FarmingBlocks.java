package forestry.farming.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.farming.blocks.BlockFarm;
import forestry.farming.blocks.EnumFarmBlockType;
import forestry.farming.blocks.EnumFarmMaterial;
import forestry.farming.items.ItemBlockFarm;
import forestry.modules.features.FeatureBlockTable;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class FarmingBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.FARMING);

	public static final FeatureBlockTable<BlockFarm, EnumFarmBlockType, EnumFarmMaterial> FARM = REGISTRY.blockTable(BlockFarm::create, EnumFarmBlockType.VALUES, EnumFarmMaterial.values()).item(ItemBlockFarm::new).identifier("farm").create();
}
