package forestry.mail.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.core.items.ItemBlockForestry;
import forestry.mail.blocks.BlockMail;
import forestry.mail.blocks.BlockTypeMail;
import forestry.modules.features.FeatureBlockGroup;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class MailBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.MAIL);

	public static final FeatureBlockGroup<BlockMail, BlockTypeMail> BASE = REGISTRY.blockGroup(BlockMail::new, BlockTypeMail.values()).item(ItemBlockForestry::new).create();
}
