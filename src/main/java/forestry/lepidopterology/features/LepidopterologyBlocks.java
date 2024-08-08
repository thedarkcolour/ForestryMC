package forestry.lepidopterology.features;

import net.minecraft.world.item.BlockItem;

import forestry.api.modules.ForestryModuleIds;
import forestry.lepidopterology.blocks.BlockCocoon;
import forestry.lepidopterology.blocks.BlockSolidCocoon;
import forestry.modules.features.FeatureBlock;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class LepidopterologyBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.LEPIDOPTEROLOGY);

	public static final FeatureBlock<BlockCocoon, BlockItem> COCOON = REGISTRY.block(BlockCocoon::new, "cocoon");
	// used only in world generation
	public static final FeatureBlock<BlockSolidCocoon, BlockItem> COCOON_SOLID = REGISTRY.block(BlockSolidCocoon::new, "cocoon_solid");
}
