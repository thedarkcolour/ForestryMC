package forestry.core.data;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import forestry.api.ForestryConstants;
import forestry.api.ForestryTags;
import forestry.apiculture.features.ApicultureBlocks;
import forestry.arboriculture.ForestryWoodType;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.features.CharcoalBlocks;
import forestry.core.blocks.EnumResourceType;
import forestry.core.features.CoreBlocks;
import forestry.database.features.DatabaseBlocks;
import forestry.energy.features.EnergyBlocks;
import forestry.factory.features.FactoryBlocks;
import forestry.farming.blocks.BlockFarm;
import forestry.farming.blocks.EnumFarmMaterial;
import forestry.farming.features.FarmingBlocks;
import forestry.mail.features.MailBlocks;
import forestry.modules.features.FeatureBlockGroup;
import forestry.worktable.features.WorktableBlocks;

public final class ForestryBlockTagsProvider extends BlockTagsProvider {
	public ForestryBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, ForestryConstants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ForestryTags.Blocks.MINEABLE_SCOOP).add(ApicultureBlocks.BEEHIVE.blockArray());
		tag(ForestryTags.Blocks.MINEABLE_GRAFTER).addTag(BlockTags.LEAVES);

		for (EnumFarmMaterial material : EnumFarmMaterial.values()) {
			tag(ForestryTags.Blocks.VALID_FARM_BASE).add(material.getBase());
		}
		tag(ForestryTags.Blocks.VALID_FARM_BASE).add(Blocks.SMOOTH_STONE);


		tag(BlockTags.MINEABLE_WITH_AXE)
				.add(CoreBlocks.NATURALIST_CHEST.blockArray())
				.add(CharcoalBlocks.WOOD_PILE.block())
				.add(WorktableBlocks.WORKTABLE.block());

		tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(CoreBlocks.APATITE_ORE.block())
				.add(CoreBlocks.DEEPSLATE_APATITE_ORE.block())
				.add(CoreBlocks.TIN_ORE.block())
				.add(CoreBlocks.DEEPSLATE_TIN_ORE.block())
				.add(CoreBlocks.RAW_TIN_BLOCK.block())
				.add(CharcoalBlocks.CHARCOAL.block())
				.add(EnergyBlocks.ENGINES.blockArray())
				.add(DatabaseBlocks.DATABASE.block());

		for (BlockFarm block : FarmingBlocks.FARM.getBlocks()) {
			tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
		}

		for (Block block : union(CoreBlocks.RESOURCE_STORAGE, FactoryBlocks.PLAIN, FactoryBlocks.TESR, MailBlocks.BASE)) {
			tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
		}

		tag(BlockTags.MINEABLE_WITH_SHOVEL)
				.add(CoreBlocks.HUMUS.block())
				.add(CoreBlocks.BOG_EARTH.block())
				.add(CoreBlocks.PEAT.block());

		for (Block block : union(
				CoreBlocks.BASE,
				ApicultureBlocks.ALVEARY, ApicultureBlocks.BASE,
				ArboricultureBlocks.DOORS,
				ArboricultureBlocks.PLANKS, ArboricultureBlocks.PLANKS_FIREPROOF, ArboricultureBlocks.PLANKS_VANILLA_FIREPROOF,
				ArboricultureBlocks.LOGS, ArboricultureBlocks.LOGS_FIREPROOF, ArboricultureBlocks.LOGS_VANILLA_FIREPROOF,
				ArboricultureBlocks.FENCES, ArboricultureBlocks.FENCES_FIREPROOF, ArboricultureBlocks.FENCES_VANILLA_FIREPROOF)) {
			tag(BlockTags.MINEABLE_WITH_AXE).add(block);
		}


		tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.addTag(ForestryTags.Blocks.ORES_TIN)
				.addTag(ForestryTags.Blocks.ORES_APATITE)
				.addTag(ForestryTags.Blocks.STORAGE_BLOCKS_RAW_TIN);
		tag(BlockTags.NEEDS_STONE_TOOL)
				.addTag(ForestryTags.Blocks.ORES_TIN)
				.addTag(ForestryTags.Blocks.ORES_APATITE)
				.addTag(ForestryTags.Blocks.STORAGE_BLOCKS_RAW_TIN);

		tag(ForestryTags.Blocks.CHARCOAL_BLOCK).add(CharcoalBlocks.CHARCOAL.block());
		tag(Tags.Blocks.CHESTS).add(CoreBlocks.NATURALIST_CHEST.getBlocks().toArray(Block[]::new));
		tag(BlockTags.PLANKS).add(ArboricultureBlocks.PLANKS.blockArray());
		tag(BlockTags.LOGS).add(ArboricultureBlocks.LOGS.blockArray());
		tag(BlockTags.LOGS_THAT_BURN).add(ArboricultureBlocks.LOGS.blockArray());
		tag(BlockTags.STAIRS).add(ArboricultureBlocks.STAIRS.blockArray());
		tag(BlockTags.WOODEN_STAIRS).add(ArboricultureBlocks.STAIRS.blockArray());
		tag(BlockTags.FENCES).add(ArboricultureBlocks.FENCES.blockArray());
		tag(BlockTags.WOODEN_FENCES).add(ArboricultureBlocks.FENCES.blockArray());
		tag(Tags.Blocks.FENCES).add(ArboricultureBlocks.FENCES.blockArray());
		tag(Tags.Blocks.FENCE_GATES).add(ArboricultureBlocks.FENCE_GATES.blockArray());
		tag(Tags.Blocks.FENCE_GATES_WOODEN).add(ArboricultureBlocks.FENCE_GATES.blockArray());
		tag(BlockTags.SLABS).add(ArboricultureBlocks.SLABS.blockArray());
		tag(BlockTags.WOODEN_SLABS).add(ArboricultureBlocks.SLABS.blockArray());
		tag(BlockTags.DOORS).add(ArboricultureBlocks.DOORS.blockArray());
		tag(BlockTags.WOODEN_DOORS).add(ArboricultureBlocks.DOORS.blockArray());

		tag(BlockTags.PLANKS).add(ArboricultureBlocks.PLANKS_FIREPROOF.blockArray());
		tag(BlockTags.LOGS).add(ArboricultureBlocks.LOGS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.PLANKS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.SLABS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STAIRS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.LOGS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.WOOD_FIREPROOF.blockArray());
		//tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STRIPPED_WOOD_FIREPROOF.blockArray());
		//tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STRIPPED_LOGS_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.FENCES_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.FENCE_GATES_FIREPROOF.blockArray());
		tag(BlockTags.STAIRS).add(ArboricultureBlocks.STAIRS_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_STAIRS).add(ArboricultureBlocks.STAIRS_FIREPROOF.blockArray());
		tag(BlockTags.FENCES).add(ArboricultureBlocks.FENCES_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCES).add(ArboricultureBlocks.FENCES_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_FENCES).add(ArboricultureBlocks.FENCES_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCE_GATES).add(ArboricultureBlocks.FENCE_GATES_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCE_GATES_WOODEN).add(ArboricultureBlocks.FENCE_GATES_FIREPROOF.blockArray());
		tag(BlockTags.SLABS).add(ArboricultureBlocks.SLABS_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_SLABS).add(ArboricultureBlocks.SLABS_FIREPROOF.blockArray());

		tag(BlockTags.PLANKS).add(ArboricultureBlocks.PLANKS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.LOGS).add(ArboricultureBlocks.LOGS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.PLANKS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.SLABS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STAIRS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.LOGS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.WOOD_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STRIPPED_WOOD_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.STRIPPED_LOGS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.FENCES_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.NON_FLAMMABLE_WOOD).add(ArboricultureBlocks.FENCE_GATES_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.STAIRS).add(ArboricultureBlocks.STAIRS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_STAIRS).add(ArboricultureBlocks.STAIRS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.FENCES).add(ArboricultureBlocks.FENCES_VANILLA_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCES).add(ArboricultureBlocks.FENCES_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_FENCES).add(ArboricultureBlocks.FENCES_VANILLA_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCE_GATES).add(ArboricultureBlocks.FENCE_GATES_VANILLA_FIREPROOF.blockArray());
		tag(Tags.Blocks.FENCE_GATES_WOODEN).add(ArboricultureBlocks.FENCE_GATES_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.SLABS).add(ArboricultureBlocks.SLABS_VANILLA_FIREPROOF.blockArray());
		tag(BlockTags.WOODEN_SLABS).add(ArboricultureBlocks.SLABS_VANILLA_FIREPROOF.blockArray());

		tag(BlockTags.SAPLINGS).add(ArboricultureBlocks.SAPLING_GE.block());
		tag(BlockTags.LEAVES).add(ArboricultureBlocks.LEAVES.block()).add(ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.blockArray()).add(ArboricultureBlocks.LEAVES_DEFAULT.blockArray()).add(ArboricultureBlocks.LEAVES_DECORATIVE.blockArray());

		addToTag(Tags.Blocks.ORES, ForestryTags.Blocks.ORES_TIN, ForestryTags.Blocks.ORES_APATITE);
		tag(ForestryTags.Blocks.ORES_TIN).add(CoreBlocks.TIN_ORE.block(), CoreBlocks.DEEPSLATE_TIN_ORE.block());
		tag(ForestryTags.Blocks.ORES_APATITE).add(CoreBlocks.APATITE_ORE.block(), CoreBlocks.DEEPSLATE_APATITE_ORE.block());
		tag(ForestryTags.Blocks.STORAGE_BLOCKS_RAW_TIN).add(CoreBlocks.RAW_TIN_BLOCK.block());

		addToTag(Tags.Blocks.STORAGE_BLOCKS, ForestryTags.Blocks.STORAGE_BLOCKS_APATITE, ForestryTags.Blocks.STORAGE_BLOCKS_BRONZE, ForestryTags.Blocks.STORAGE_BLOCKS_TIN);
		tag(ForestryTags.Blocks.STORAGE_BLOCKS_APATITE).add(CoreBlocks.RESOURCE_STORAGE.get(EnumResourceType.APATITE).block());
		tag(ForestryTags.Blocks.STORAGE_BLOCKS_TIN).add(CoreBlocks.RESOURCE_STORAGE.get(EnumResourceType.TIN).block());
		tag(ForestryTags.Blocks.STORAGE_BLOCKS_BRONZE).add(CoreBlocks.RESOURCE_STORAGE.get(EnumResourceType.BRONZE).block());

		tag(ForestryTags.Blocks.PALM_LOGS).add(ArboricultureBlocks.LOGS.get(ForestryWoodType.PALM).block());
		tag(ForestryTags.Blocks.PAPAYA_LOGS).add(ArboricultureBlocks.LOGS.get(ForestryWoodType.PAPAYA).block());

		tag(BlockTags.DIRT).add(CoreBlocks.HUMUS.block());

		tag(ForestryTags.Blocks.VANILLA_FLOWERS).addTag(BlockTags.FLOWERS);
		tag(ForestryTags.Blocks.NETHER_FLOWERS).add(Blocks.NETHER_WART, Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS, Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
		tag(ForestryTags.Blocks.CACTI_FLOWERS).add(Blocks.CACTUS);
		// todo is there a mushroom tag in later versions? should i add the nether fungi to this tag?
		tag(ForestryTags.Blocks.MUSHROOMS_FLOWERS).add(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM);
		tag(ForestryTags.Blocks.END_FLOWERS).add(Blocks.DRAGON_EGG, Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER);
		tag(ForestryTags.Blocks.JUNGLE_FLOWERS).add(Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.FERN, Blocks.LARGE_FERN, Blocks.POTTED_FERN);
		// todo what belongs in this tag?
		tag(ForestryTags.Blocks.SNOW_FLOWERS).addTag(BlockTags.FLOWERS);
		tag(ForestryTags.Blocks.WHEAT_FLOWERS).add(Blocks.WHEAT);
		tag(ForestryTags.Blocks.GOURD_FLOWERS).add(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM, Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);

		tag(ForestryTags.Blocks.PLANTABLE_FLOWERS)
				.addTag(BlockTags.FLOWERS)
				.add(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM)
				.add(Blocks.FERN)
				.add(Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.WARPED_ROOTS, Blocks.CRIMSON_ROOTS);

		//why is there no tag for cactus plantable, but there is for dead bush?
		tag(ForestryTags.Blocks.MODEST_BEE_GROUND).addTag(BlockTags.SAND).addTag(BlockTags.TERRACOTTA);
		tag(ForestryTags.Blocks.WINTRY_BEE_GROUND).addTag(BlockTags.DIRT).addTag(BlockTags.SNOW);
	}

	@SafeVarargs
	private void addToTag(TagKey<Block> tag, TagKey<Block>... providers) {
		TagsProvider.TagAppender<Block> builder = tag(tag);
		for (TagKey<Block> provider : providers) {
			builder.addTag(provider);
		}
	}

	@Override
	public String getName() {
		return "Forestry Block Tags";
	}

	private static Collection<Block> union(FeatureBlockGroup<?, ?>... features) {
		Set<Block> set = new LinkedHashSet<>();

		for (FeatureBlockGroup<?, ?> feature : features) {
			set.addAll(feature.getBlocks());
		}

		return set;
	}
}
