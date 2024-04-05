package forestry.core.data;

import com.google.common.collect.Table;

import deleteme.RegistryNameFinder;
import deleteme.Todos;

import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.EnumPropolis;
import forestry.core.config.Constants;
import forestry.core.data.builder.FilledCrateModelBuilder;
import forestry.core.fluids.ForestryFluids;
import forestry.cultivation.blocks.BlockPlanter;
import forestry.cultivation.blocks.BlockTypePlanter;
import forestry.cultivation.features.CultivationBlocks;
import forestry.lepidopterology.features.LepidopterologyItems;
import forestry.modules.features.FeatureBlock;
import forestry.modules.features.FeatureItem;
import forestry.storage.features.CrateItems;
import forestry.storage.items.ItemCrated;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForestryItemModelProvider extends ItemModelProvider {

	public ForestryItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Constants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(LepidopterologyItems.CATERPILLAR_GE.getIdentifier(), mcLoc("item/generated"))
				.texture("layer0", new ResourceLocation(Constants.MOD_ID, "item/caterpillar.body2"))
				.texture("layer1", new ResourceLocation(Constants.MOD_ID, "item/caterpillar.body"));
		withExistingParent(LepidopterologyItems.SERUM_GE.getIdentifier(), mcLoc("item/generated"))
				.texture("layer0", new ResourceLocation(Constants.MOD_ID, "item/liquids/jar.bottle"))
				.texture("layer1", new ResourceLocation(Constants.MOD_ID, "item/liquids/jar.contents"));

		// todo: custom loader
		Todos.todo();
		//	registerModel(ArboricultureItems.SAPLING, new ModelBuilder().parent("forge:item/default").loader(new ResourceLocation(Constants.MOD_ID, "sapling_ge")));
		for (FeatureItem<ItemCrated> featureCrated : CrateItems.getCrates()) {
			Item containedItem = featureCrated.get().getContained().getItem();
			String id = featureCrated.getIdentifier();

			if (ApicultureItems.BEE_COMBS.itemEqual(containedItem)) {
				filledCrateModelLayered(id, modLoc("item/bee_combs.0"), modLoc("item/bee_combs.1"));
			} else if (ApicultureItems.POLLEN_CLUSTER.itemEqual(containedItem)) {
				filledCrateModelLayered(id, modLoc("item/pollen.0"), modLoc("item/pollen.1"));
			} else {
				ResourceLocation contained = RegistryNameFinder.getRegistryName(containedItem);
				ResourceLocation contentsTexture;

				if (containedItem instanceof BlockItem && !(containedItem instanceof ItemNameBlockItem)) {
					contentsTexture = new ResourceLocation(contained.getNamespace(), "block/" + contained.getPath());
				} else {
					contentsTexture = new ResourceLocation(contained.getNamespace(), "item/" + contained.getPath());
				}

				filledCrateModel(id, contentsTexture);

			}
		}

		// manual overrides
		filledCrateModel(CrateItems.CRATED_CACTUS.getIdentifier(), mcLoc("block/cactus_side"));
		filledCrateModel(CrateItems.CRATED_MYCELIUM.getIdentifier(), mcLoc("block/mycelium_side"));
		filledCrateModel(CrateItems.CRATED_GRASS_BLOCK.getIdentifier(), mcLoc("block/grass_block_top"));
		filledCrateModel(CrateItems.CRATED_PROPOLIS.getIdentifier(), modLoc("item/propolis.0"));

		for (Table.Cell<BlockTypePlanter, BlockPlanter.Mode, FeatureBlock<BlockPlanter, BlockItem>> cell : CultivationBlocks.PLANTER.getFeatureByTypes().cellSet()) {
			Block block = cell.getValue().block();
			withExistingParent(RegistryNameFinder.getRegistryName(block).getPath(), new ResourceLocation(Constants.MOD_ID, "block/" + cell.getRowKey().getSerializedName()));
		}

		for (ForestryFluids fluid : ForestryFluids.values()) {
			BucketItem item = fluid.getBucket();
			if (item == null) {
				continue;
			}
			//"bucket_"
			//	registerModel(item, new ModelBuilder()
			//			.loader(new ResourceLocation("forge", "bucket"))
			//			.parent("forge:item/bucket_drip")
			//			.loaderData("fluid", new JsonPrimitive(RegistryNameFinder.getRegistryName(fluid.getFluid()).toString()))
			//	);
		}
	}

	private void filledCrateModel(String id, ResourceLocation texture) {
		getBuilder(id)
				.customLoader(FilledCrateModelBuilder::begin)
				.layer1(texture)
				.end();
	}

	private void filledCrateModelLayered(String id, ResourceLocation layer1, ResourceLocation layer2) {
		getBuilder(id)
				.customLoader(FilledCrateModelBuilder::begin)
				.layer1(layer1)
				.layer2(layer2)
				.end();
	}
}
