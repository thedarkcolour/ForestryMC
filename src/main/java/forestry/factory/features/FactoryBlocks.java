package forestry.factory.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.core.items.ItemBlockTesr;
import forestry.core.items.ItemBlockForestry;
import forestry.factory.blocks.BlockFactoryPlain;
import forestry.factory.blocks.BlockFactoryTESR;
import forestry.factory.blocks.BlockTypeFactoryPlain;
import forestry.factory.blocks.BlockTypeFactoryTesr;
import forestry.modules.features.FeatureBlockGroup;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class FactoryBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.FACTORY);

	public static final FeatureBlockGroup<BlockFactoryTESR, BlockTypeFactoryTesr> TESR = REGISTRY.blockGroup(BlockFactoryTESR::new, BlockTypeFactoryTesr.values()).item(ItemBlockTesr::new).create();
	public static final FeatureBlockGroup<BlockFactoryPlain, BlockTypeFactoryPlain> PLAIN = REGISTRY.blockGroup(BlockFactoryPlain::new, BlockTypeFactoryPlain.values()).item(ItemBlockForestry::new).create();
}
