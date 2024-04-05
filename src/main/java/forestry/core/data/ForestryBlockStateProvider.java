package forestry.core.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import forestry.core.config.Constants;
import forestry.core.features.CoreBlocks;
import forestry.farming.blocks.BlockFarm;
import forestry.farming.blocks.EnumFarmBlockType;
import forestry.farming.blocks.EnumFarmMaterial;
import forestry.farming.features.FarmingBlocks;
import forestry.modules.features.ModFeatureRegistry;
import forestry.storage.ModuleBackpacks;
import forestry.storage.items.ItemBackpack;

import deleteme.RegistryNameFinder;

public class ForestryBlockStateProvider extends BlockStateProvider {
	public ForestryBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Constants.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (BlockFarm block : FarmingBlocks.FARM.getBlocks()) {
			if (block.getType() == EnumFarmBlockType.PLAIN) {
				plainFarm(block);
			} else {
				singleFarm(block);
			}

			generic3d(block);
		}

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

		for (RegistryObject<Item> object : ModFeatureRegistry.get(ModuleBackpacks.class).getRegistry(Registry.ITEM_REGISTRY).getEntries()) {
			if (object.get() instanceof ItemBackpack) {
				String path = object.getId().getPath();
				boolean woven = path.endsWith("woven");

				itemModels().withExistingParent(path, woven ? modLoc("item/backpack/woven_neutral") : modLoc("item/backpack/normal_neutral"))
						.override().predicate(new ResourceLocation("mode"), 1).model(file(woven ? modLoc("item/backpack/woven_locked") : modLoc("item/backpack/normal_locked"))).end()
						.override().predicate(new ResourceLocation("mode"), 2).model(file(woven ? modLoc("item/backpack/woven_receive") : modLoc("item/backpack/normal_receive"))).end()
						.override().predicate(new ResourceLocation("mode"), 3).model(file(woven ? modLoc("item/backpack/woven_resupply") : modLoc("item/backpack/normal_resupply"))).end();
			}
		}
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

	// Everything below this line is boilerplate code adapted from https://github.com/thedarkcolour/ModKit
	public void generic3d(Block block) {
		String path = path(block);
		itemModels().withExistingParent(path, modLoc("block/" + path));
	}

	private static String path(Block block) {
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
}
