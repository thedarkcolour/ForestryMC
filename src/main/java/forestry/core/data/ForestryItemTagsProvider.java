package forestry.core.data;

import javax.annotation.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import forestry.api.ForestryConstants;
import forestry.api.ForestryTags;
import forestry.apiculture.features.ApicultureItems;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.features.CoreItems;
import forestry.core.items.ItemFruit;
import forestry.mail.features.MailItems;

public final class ForestryItemTagsProvider extends ItemTagsProvider {
	public ForestryItemTagsProvider(DataGenerator generator, ForestryBlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, blockTagsProvider, ForestryConstants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		// Copy block tags
		copy(ForestryTags.Blocks.CHARCOAL_BLOCK, ForestryTags.Items.CHARCOAL_BLOCK);
		copy(Tags.Blocks.CHESTS, Tags.Items.CHESTS);
		copy(BlockTags.PLANKS, ItemTags.PLANKS);
		copy(BlockTags.LOGS, ItemTags.LOGS);
		copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
		copy(BlockTags.NON_FLAMMABLE_WOOD, ItemTags.NON_FLAMMABLE_WOOD);
		copy(BlockTags.STAIRS, ItemTags.STAIRS);
		copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		copy(BlockTags.FENCES, ItemTags.FENCES);
		copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
		copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
		copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
		copy(BlockTags.SLABS, ItemTags.SLABS);
		copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		copy(BlockTags.DOORS, ItemTags.DOORS);
		copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);

		tag(ItemTags.SAPLINGS).add(ArboricultureItems.SAPLING.get());
		copy(BlockTags.LEAVES, ItemTags.LEAVES);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);
		copy(ForestryTags.Blocks.ORES_TIN, ForestryTags.Items.ORES_TIN);
		copy(ForestryTags.Blocks.ORES_APATITE, ForestryTags.Items.ORES_APATITE);
		copy(ForestryTags.Blocks.STORAGE_BLOCKS_RAW_TIN, ForestryTags.Items.STORAGE_BLOCKS_RAW_TIN);

		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		copy(ForestryTags.Blocks.STORAGE_BLOCKS_APATITE, ForestryTags.Items.STORAGE_BLOCKS_APATITE);
		copy(ForestryTags.Blocks.STORAGE_BLOCKS_TIN, ForestryTags.Items.STORAGE_BLOCKS_TIN);
		copy(ForestryTags.Blocks.STORAGE_BLOCKS_BRONZE, ForestryTags.Items.STORAGE_BLOCKS_BRONZE);

		copy(BlockTags.DIRT, ItemTags.DIRT);

		// Add item-specific tags
		addToTag(ForestryTags.Items.GEARS, ForestryTags.Items.GEARS_BRONZE, ForestryTags.Items.GEARS_COPPER, ForestryTags.Items.GEARS_TIN);
		tag(ForestryTags.Items.GEARS_BRONZE).add(CoreItems.GEAR_BRONZE.item());
		tag(ForestryTags.Items.GEARS_TIN).add(CoreItems.GEAR_TIN.item());
		tag(ForestryTags.Items.GEARS_COPPER).add(CoreItems.GEAR_COPPER.item());
		tag(ForestryTags.Items.GEARS_STONE);

		addToTag(Tags.Items.INGOTS, ForestryTags.Items.INGOTS_BRONZE, ForestryTags.Items.INGOTS_TIN);
		tag(ForestryTags.Items.INGOTS_BRONZE).add(CoreItems.INGOT_BRONZE.item());
		tag(ForestryTags.Items.INGOTS_TIN).add(CoreItems.INGOT_TIN.item());

		tag(ForestryTags.Items.DUSTS_ASH).add(CoreItems.ASH.item());
		tag(ForestryTags.Items.GEMS_APATITE).add(CoreItems.APATITE.item());
		tag(ForestryTags.Items.RAW_MATERIALS_TIN).add(CoreItems.RAW_TIN.item());

		addToTag(Tags.Items.STORAGE_BLOCKS, ForestryTags.Items.STORAGE_BLOCKS_APATITE, ForestryTags.Items.STORAGE_BLOCKS_BRONZE, ForestryTags.Items.STORAGE_BLOCKS_TIN);

		tag(Tags.Items.RAW_MATERIALS).addTag(ForestryTags.Items.RAW_MATERIALS_TIN);

		tag(ItemTags.SAPLINGS).add(ArboricultureItems.SAPLING.item());
		tag(ForestryTags.Items.BEE_COMBS).add(ApicultureItems.BEE_COMBS.itemArray());
		tag(ForestryTags.Items.VILLAGE_COMBS).add(ApicultureItems.BEE_COMBS.getItems().stream().filter(item -> !item.isUnused()).toArray(Item[]::new));
		tag(ForestryTags.Items.PROPOLIS).add(ApicultureItems.PROPOLIS.itemArray());
		tag(ForestryTags.Items.DROP_HONEY).add(ApicultureItems.HONEY_DROPS.itemArray());

		addToTag(Tags.Items.ORES, ForestryTags.Items.ORES_TIN, ForestryTags.Items.ORES_APATITE);

		tag(ForestryTags.Items.STAMPS).add(MailItems.STAMPS.itemArray());

		tag(ForestryTags.Items.FORESTRY_FRUITS).add(CoreItems.FRUITS.itemArray());
		tag(ForestryTags.Items.FRUITS).addTag(ForestryTags.Items.FORESTRY_FRUITS);
		tag(ForestryTags.Items.CHERRY).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.CHERRY));
		tag(ForestryTags.Items.WALNUT).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.WALNUT));
		tag(ForestryTags.Items.CHESTNUT).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.CHESTNUT));
		tag(ForestryTags.Items.LEMON).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.LEMON));
		tag(ForestryTags.Items.PLUM).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.PLUM));
		tag(ForestryTags.Items.DATE).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.DATES));
		tag(ForestryTags.Items.PAPAYA).add(CoreItems.FRUITS.item(ItemFruit.EnumFruit.PAPAYA));

		tag(ForestryTags.Items.DUSTS_ASH).add(CoreItems.ASH.item());
		tag(ForestryTags.Items.SAWDUST).add(CoreItems.WOOD_PULP.item());

		tag(ForestryTags.Items.CRAFTING_TABLES)
				.addOptionalTag(new ResourceLocation("c", "player_workstations/crafting_tables"))
				.addOptionalTag(new ResourceLocation("c", "workbenches"))
				.addOptionalTag(new ResourceLocation("c", "workbench"))
				.add(Items.CRAFTING_TABLE);

		tag(ForestryTags.Items.SCOOPS).add(ApicultureItems.SCOOP.item());
	}

	@SafeVarargs
	private void addToTag(TagKey<Item> tag, TagKey<Item>... providers) {
		TagsProvider.TagAppender<Item> builder = tag(tag);
		for (TagKey<Item> provider : providers) {
			builder.addTag(provider);
		}
	}

	@Override
	public String getName() {
		return "Forestry Item Tags";
	}
}
