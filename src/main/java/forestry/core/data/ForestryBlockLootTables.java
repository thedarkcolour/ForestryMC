package forestry.core.data;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import forestry.api.ForestryConstants;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.arboriculture.blocks.BlockDefaultLeaves;
import forestry.arboriculture.blocks.BlockDefaultLeavesFruit;
import forestry.arboriculture.blocks.BlockForestryDoor;
import forestry.arboriculture.blocks.ForestryLeafType;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.arboriculture.features.CharcoalBlocks;
import forestry.arboriculture.loot.CountBlockFunction;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.core.loot.OrganismFunction;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.features.LepidopterologyBlocks;
import forestry.modules.features.FeatureBlock;
import forestry.modules.features.FeatureBlockGroup;
import forestry.modules.features.ModFeatureRegistry;

/**
 * Data generator class that generates the block drop loot tables for forestry blocks.
 */
public class ForestryBlockLootTables extends BlockLoot {

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		for (BlockDecorativeLeaves leaves : ArboricultureBlocks.LEAVES_DECORATIVE.getBlocks()) {
			add(leaves, block -> droppingWithChances(block, leaves.getType(), NORMAL_LEAVES_SAPLING_CHANCES));
		}
		for (BlockDefaultLeaves leaves : ArboricultureBlocks.LEAVES_DEFAULT.getBlocks()) {
			add(leaves, block -> droppingWithChances(block, leaves.getType(), NORMAL_LEAVES_SAPLING_CHANCES));
		}
		for (Map.Entry<ForestryLeafType, FeatureBlock<BlockDefaultLeavesFruit, BlockItem>> entry : ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.getFeatureByType().entrySet()) {
			FeatureBlock<BlockDefaultLeaves, BlockItem> defaultLeaves = ArboricultureBlocks.LEAVES_DEFAULT.get(entry.getKey());
			Block defaultLeavesBlock = defaultLeaves.block();
			Block fruitLeavesBlock = entry.getValue().block();
			add(fruitLeavesBlock, (block) -> droppingWithChances(defaultLeavesBlock, entry.getKey(), NORMAL_LEAVES_SAPLING_CHANCES));
		}
		for (BlockForestryDoor door : ArboricultureBlocks.DOORS.getBlocks()) {
			add(door, BlockLoot.createDoorTable(door));
		}
		registerLootTable(CharcoalBlocks.ASH, (block) -> LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK)
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CoreItems.ASH)).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 1.0f / 3.0f))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.CHARCOAL)).apply(CountBlockFunction.builder()).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 23.0f / 40, 2))));
		registerLootTable(CoreBlocks.PEAT, (block) -> LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.DIRT))).withPool(LootPool.lootPool().apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))).add(LootItem.lootTableItem(CoreItems.PEAT.item()))));
		registerDropping(CoreBlocks.HUMUS, Blocks.DIRT);

		// todo fix all of these
		registerEmptyTables(ArboricultureBlocks.PODS); // Handled by internal logic
		registerEmptyTables(ArboricultureBlocks.SAPLING_GE); // Handled by internal logic
		registerEmptyTables(ArboricultureBlocks.LEAVES);  // Handled by internal logic
		registerEmptyTables(LepidopterologyBlocks.COCOON);
		registerEmptyTables(LepidopterologyBlocks.COCOON_SOLID);

		registerLootTable(CoreBlocks.APATITE_ORE, ForestryBlockLootTables::createApatiteOreDrops);
		registerLootTable(CoreBlocks.DEEPSLATE_APATITE_ORE, ForestryBlockLootTables::createApatiteOreDrops);

		registerLootTable(CoreBlocks.TIN_ORE, block -> createOreDrop(block, CoreItems.RAW_TIN.item()));
		registerLootTable(CoreBlocks.DEEPSLATE_TIN_ORE, block -> createOreDrop(block, CoreItems.RAW_TIN.item()));

		dropSelf(CoreBlocks.RAW_TIN_BLOCK.block());

		Set<ResourceLocation> visited = Sets.newHashSet();
		ModFeatureRegistry forestryRegistry = ModFeatureRegistry.getRegistries().get(ForestryConstants.MOD_ID);

		// i hate this system
		forestryRegistry.getModules().values().stream().flatMap(module -> module.getFeatures(Registry.BLOCK_REGISTRY).stream()).forEach(feature -> {
			if (feature instanceof FeatureBlock<?, ?> blockFeature) {
				Block block = blockFeature.block();
				ResourceLocation resourcelocation = block.getLootTable();
				if (resourcelocation != BuiltInLootTables.EMPTY && visited.add(resourcelocation)) {
					LootTable.Builder builder = this.map.remove(resourcelocation);

					if (builder == null) {
						builder = createSingleItemTable(block);
					}

					consumer.accept(resourcelocation, builder);
				}
			} else {
				throw new IllegalStateException("Found feature in BLOCK_REGISTRY that is not FeatureBlock.");
			}
		});

		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}

	private static LootTable.Builder createApatiteOreDrops(Block block) {
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CoreItems.APATITE.item()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
	}

	public static LootTable.Builder droppingWithChances(Block block, ForestryLeafType definition, float... chances) {
		return createSilkTouchOrShearsDispatchTable(block,
				applyExplosionCondition(block, LootItem.lootTableItem(ArboricultureItems.SAPLING)
						.apply(OrganismFunction.fromId(SpeciesUtil.TREE_TYPE.get().id(), definition.getSpeciesId())))
						.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, chances)));
	}

	public void registerLootTable(FeatureBlock<?, ?> featureBlock, Function<Block, LootTable.Builder> builderFunction) {
		add(featureBlock.block(), builderFunction);
	}

	public void registerDropping(FeatureBlock<?, ?> featureBlock, ItemLike drop) {
		dropOther(featureBlock.block(), drop);
	}

	public void registerEmptyTables(FeatureBlockGroup<?, ?> blockGroup) {
		registerEmptyTables(blockGroup.blockArray());
	}

	public void registerEmptyTables(FeatureBlock<?, ?> featureBlock) {
		registerEmptyTables(featureBlock.block());
	}

	public void registerEmptyTables(Block... blocks) {
		for (Block block : blocks) {
			add(block, noDrop());
		}
	}
}
