package forestry.core.data.models;

import java.util.List;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.core.config.Constants;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.core.fluids.ForestryFluids;
import forestry.cultivation.blocks.BlockPlanter;
import forestry.cultivation.blocks.BlockTypePlanter;
import forestry.cultivation.features.CultivationBlocks;
import forestry.farming.blocks.BlockFarm;
import forestry.farming.blocks.EnumFarmBlockType;
import forestry.farming.blocks.EnumFarmMaterial;
import forestry.farming.features.FarmingBlocks;
import forestry.lepidopterology.blocks.BlockCocoon;
import forestry.lepidopterology.features.LepidopterologyBlocks;
import forestry.modules.features.FeatureBlock;

import deleteme.RegistryNameFinder;

public class ForestryBlockStateProvider extends BlockStateProvider {
	public ForestryBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Constants.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		// Farm blocks
		for (BlockFarm block : FarmingBlocks.FARM.getBlocks()) {
			if (block.getType() == EnumFarmBlockType.PLAIN) {
				plainFarm(block);
			} else {
				singleFarm(block);
			}

			generic3d(block);
		}

		for (BlockTypePlanter farmType : BlockTypePlanter.values()) {
			ModelFile file = models().getExistingFile(modBlock(farmType.getSerializedName()));
			horizontalBlock(CultivationBlocks.PLANTER.get(farmType, BlockPlanter.Mode.MANUAL).block(), file);
			horizontalBlock(CultivationBlocks.PLANTER.get(farmType, BlockPlanter.Mode.MANAGED).block(), file);
		}

		// Resources
		simpleBlock(CoreBlocks.BOG_EARTH.block());
		simpleBlock(CoreBlocks.HUMUS.block());

		simpleBlock(CoreBlocks.APATITE_ORE.block());
		simpleBlock(CoreBlocks.DEEPSLATE_APATITE_ORE.block());
		simpleBlock(CoreBlocks.TIN_ORE.block());
		simpleBlock(CoreBlocks.DEEPSLATE_TIN_ORE.block());
		simpleBlock(CoreBlocks.RAW_TIN_BLOCK.block());
		generic3d(CoreBlocks.APATITE_ORE.block());
		generic3d(CoreBlocks.DEEPSLATE_APATITE_ORE.block());
		generic3d(CoreBlocks.TIN_ORE.block());
		generic3d(CoreBlocks.DEEPSLATE_TIN_ORE.block());
		generic3d(CoreBlocks.RAW_TIN_BLOCK.block());

		generic2d(CoreItems.RAW_TIN);
		generic2d(CoreItems.INGOT_TIN);
		generic2d(CoreItems.GEAR_TIN);
		generic2d(CoreItems.INGOT_BRONZE);
		generic2d(CoreItems.GEAR_BRONZE);
		generic2d(CoreItems.GEAR_COPPER);

		// Fluids (doesn't actually show in game, but silences the warning spam from Minecraft)
		for (ForestryFluids fluid : ForestryFluids.values()) {
			Block block = fluid.getFeature().fluidBlock().block();
			ModelFile blockModel = particleOnly(path(block), fluid.getFeature().properties().resources[0]);
			getVariantBuilder(block).partialState().modelForState().modelFile(blockModel).addModel();
		}

		// Leaves (same as with fluids)
		for (TreeDefinition treeType : TreeDefinition.VALUES) {
			Block defaultBlock = ArboricultureBlocks.LEAVES_DEFAULT.get(treeType).block();
			Block defaultFruitBlock = ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.get(treeType).block();
			Block decorativeBlock = ArboricultureBlocks.LEAVES_DECORATIVE.get(treeType).block();
			ResourceLocation particle = treeType.getSpecies().getLeafSpriteProvider().getSprite(false, true);
			ModelFile file = models().getBuilder(path(defaultBlock)).texture("particle", particle);

			getVariantBuilder(defaultBlock).partialState().modelForState().modelFile(file).addModel();
			getVariantBuilder(defaultFruitBlock).partialState().modelForState().modelFile(file).addModel();
			getVariantBuilder(decorativeBlock).partialState().modelForState().modelFile(file).addModel();

			generic3d(defaultBlock);
			generic3d(defaultFruitBlock, defaultBlock);
			generic3d(decorativeBlock, defaultBlock);
		}
		getVariantBuilder(ArboricultureBlocks.LEAVES.block()).partialState().modelForState().modelFile(particleOnly(ArboricultureBlocks.LEAVES.getIdentifier(), blockTexture(Blocks.OAK_LEAVES))).addModel();

		// Cocoons
		/*for (FeatureBlock<BlockCocoon, BlockItem> cocoon : List.of(LepidopterologyBlocks.COCOON, LepidopterologyBlocks.COCOON)) {
			getVariantBuilder(cocoon.block())
					.partialState().with(BlockCocoon.AGE, 0).modelForState().modelFile(models().getExistingFile(modBlock("cocoon_early"))).addModel()
					.partialState().with(BlockCocoon.AGE, 1).modelForState().modelFile(models().getExistingFile(modBlock("cocoon_middle"))).addModel()
					.partialState().with(BlockCocoon.AGE, 2).modelForState().modelFile(models().getExistingFile(modBlock("cocoon_late"))).addModel();
		}*/
	}

	private ModelFile particleOnly(String path, ResourceLocation particleTexture) {
		return models().getBuilder(path).texture("particle", particleTexture);
	}

	private void singleFarm(BlockFarm block) {
		EnumFarmMaterial material = block.getFarmMaterial();
		Block base = material.getBase();
		ResourceLocation texture = modLoc("block/farm/" + block.getType().getSerializedName());

		getVariantBuilder(block).partialState().modelForState().modelFile(farmPillar(path(block), base, texture, texture)).addModel();
	}

	private void plainFarm(BlockFarm block) {
		EnumFarmMaterial material = block.getFarmMaterial();
		Block base = material.getBase();

		// todo need to use reverse texture
		getVariantBuilder(block)
				.partialState().with(BlockFarm.STATE, BlockFarm.State.PLAIN)
				.modelForState().modelFile(farmPillar(path(block), base, modLoc("block/farm/top"), modLoc("block/farm/plain"))).addModel()
				.partialState().with(BlockFarm.STATE, BlockFarm.State.BAND)
				.modelForState().modelFile(farmPillar(path(block) + "_band", base, modLoc("block/farm/top"), modLoc("block/farm/band"))).addModel();
	}

	private ModelFile farmPillar(String path, Block base, ResourceLocation top, ResourceLocation side) {
		ModelFile baseModel = file(blockTexture(base));

		return models().getBuilder(path).customLoader(CompositeModelBuilder::begin)
				.child("base", models().nested()
						.parent(baseModel)
						.renderType("solid"))
				.child("overlay", models().nested()
						.parent(mcFile("cube_column"))
						.texture("end", top)
						.texture("side", side)
						// should we use cutout_mipped?
						.renderType("cutout"))
				.itemRenderOrder("base", "overlay")
				.end()
				// reuse the particle
				.parent(baseModel);
	}

	protected static ResourceLocation withSuffix(ResourceLocation loc, String suffix) {
		return new ResourceLocation(loc.getNamespace(), loc.getPath() + suffix);
	}

	public void generic3d(Block block, Block otherParent) {
		itemModels().withExistingParent(path(block), modLoc("block/" + path(otherParent)));
	}

	public void generic3d(Block block, ResourceLocation otherParentId) {
		itemModels().withExistingParent(path(block), new ResourceLocation(otherParentId.getNamespace(), "block/" + otherParentId.getPath()));
	}

	protected ModelFile existingMcBlock(String path) {
		return models().getExistingFile(mcBlock(path));
	}

	// Everything below this line is boilerplate code adapted from https://github.com/thedarkcolour/ModKit
	// Makes a 3d cube of a block for item model
	public void generic3d(Block block) {
		String path = path(block);
		itemModels().withExistingParent(path, modLoc("block/" + path));
	}

	public static String path(Block block) {
		return RegistryNameFinder.getRegistryName(block).getPath();
	}

	public static ModelFile.UncheckedModelFile file(ResourceLocation resourceLoc) {
		return new ModelFile.UncheckedModelFile(resourceLoc);
	}

	public ModelFile.UncheckedModelFile modFile(String path) {
		return file(this.modBlock(path));
	}

	public ModelFile.UncheckedModelFile mcFile(String path) {
		return file(this.mcBlock(path));
	}

	public ResourceLocation modBlock(String name) {
		return this.modLoc("block/" + name);
	}

	public ResourceLocation mcBlock(String name) {
		return this.mcLoc("block/" + name);
	}

	public void generic2d(ItemLike item) {
		generic2d(RegistryNameFinder.getRegistryName(item.asItem()));
	}

	/**
	 * Makes a 2d single layer item like hopper, gold ingot, or redstone dust item models
	 */
	public void generic2d(ResourceLocation itemId) {
		layer0(itemId, "item/generated");
	}

	public void layer0(ResourceLocation itemId, String parentName) {
		String path = itemId.getPath();

		itemModels().getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile(parentName))
				.texture("layer0", new ResourceLocation(itemId.getNamespace(), "item/" + path));
	}
}
