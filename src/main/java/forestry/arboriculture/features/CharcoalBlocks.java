package forestry.arboriculture.features;

import net.minecraft.world.item.BlockItem;

import forestry.api.modules.ForestryModuleIds;
import forestry.arboriculture.ModuleCharcoal;
import forestry.arboriculture.blocks.BlockAsh;
import forestry.arboriculture.blocks.BlockCharcoal;
import forestry.arboriculture.blocks.BlockDecorativeWoodPile;
import forestry.arboriculture.blocks.BlockWoodPile;
import forestry.core.items.ItemBlockForestry;
import forestry.core.items.ItemProperties;
import forestry.modules.features.FeatureBlock;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IBlockFeature;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class CharcoalBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.CHARCOAL);

	public static final IBlockFeature<BlockCharcoal, ItemBlockForestry<BlockCharcoal>> CHARCOAL = REGISTRY.block(BlockCharcoal::new, (block) -> new ItemBlockForestry<>(block, new ItemProperties(ModuleCharcoal.getGroup()).burnTime(16000)), "charcoal");
	public static final IBlockFeature<BlockWoodPile, BlockItem> WOOD_PILE = REGISTRY.block(BlockWoodPile::new, (block) -> new ItemBlockForestry<>(block, new ItemProperties(ModuleCharcoal.getGroup()).burnTime(1200)), "wood_pile");
	public static final IBlockFeature<BlockDecorativeWoodPile, BlockItem> WOOD_PILE_DECORATIVE = REGISTRY.block(BlockDecorativeWoodPile::new, (block) -> new ItemBlockForestry<>(block, new ItemProperties(ModuleCharcoal.getGroup()).burnTime(1200)), "wood_pile_decorative");
	public static final IBlockFeature<BlockAsh, BlockItem> ASH = REGISTRY.block(BlockAsh::new, "ash_block");
}
