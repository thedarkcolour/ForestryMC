package forestry.core.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import forestry.api.arboriculture.EnumForestryWoodType;
import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.arboriculture.blocks.BlockForestryDoor;
import forestry.arboriculture.blocks.BlockForestryFence;
import forestry.arboriculture.blocks.BlockForestryFenceGate;
import forestry.arboriculture.blocks.BlockForestryLog;
import forestry.arboriculture.blocks.BlockForestrySlab;
import forestry.arboriculture.blocks.BlockForestryStairs;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.core.config.Constants;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.cultivation.blocks.BlockPlanter;
import forestry.cultivation.blocks.BlockTypePlanter;
import forestry.cultivation.features.CultivationBlocks;
import forestry.farming.blocks.BlockFarm;
import forestry.farming.blocks.EnumFarmBlockType;
import forestry.farming.blocks.EnumFarmMaterial;
import forestry.farming.features.FarmingBlocks;
import forestry.modules.features.FeatureBlockGroup;
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

		// Backpacks
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

		// Vanilla fireproof wood types
		for (EnumVanillaWoodType woodType : EnumVanillaWoodType.VALUES) {
			// planks
			Block planks = ArboricultureBlocks.PLANKS_VANILLA_FIREPROOF.get(woodType).block();
			String woodTypeName = woodType.getSerializedName();
			String planksName = woodTypeName + "_planks";
			ModelFile planksModel = existingMcBlock(planksName);
			simpleBlock(planks, planksModel);
			generic3d(planks, mcLoc(planksName));
			// log
			BlockForestryLog log = ArboricultureBlocks.LOGS_VANILLA_FIREPROOF.get(woodType).block();
			String logName = woodTypeName + "_log";
			ModelFile logModel = existingMcBlock(logName);
			axisBlock(log, logModel, logModel);
			generic3d(log, mcLoc(logName));
			// wood
			BlockForestryLog wood = ArboricultureBlocks.WOOD_VANILLA_FIREPROOF.get(woodType).block();
			String woodName = woodTypeName + "_wood";
			ModelFile woodModel = existingMcBlock(woodName);
			axisBlock(wood, woodModel, woodModel);
			generic3d(wood, mcLoc(woodName));
			// stripped log
			BlockForestryLog strippedLog = ArboricultureBlocks.STRIPPED_LOGS_VANILLA_FIREPROOF.get(woodType).block();
			String strippedLogName = "stripped_" + woodTypeName + "_log";
			ModelFile strippedLogModel = existingMcBlock(strippedLogName);
			axisBlock(strippedLog, strippedLogModel, strippedLogModel);
			generic3d(strippedLog, mcLoc(strippedLogName));
			// stripped wood
			BlockForestryLog strippedWood = ArboricultureBlocks.STRIPPED_WOOD_VANILLA_FIREPROOF.get(woodType).block();
			String strippedWoodName = "stripped_" + woodTypeName + "_wood";
			ModelFile strippedWoodModel = existingMcBlock(strippedWoodName);
			axisBlock(strippedWood, strippedWoodModel, strippedWoodModel);
			generic3d(strippedWood, mcLoc(strippedWoodName));
			// slab
			SlabBlock slab = ArboricultureBlocks.SLABS_VANILLA_FIREPROOF.get(woodType).block();
			String slabName = woodTypeName + "_slab";
			ModelFile bottomSlabModel = existingMcBlock(slabName);
			ModelFile topSlabModel = existingMcBlock(slabName + "_top");
			slabBlock(slab, bottomSlabModel, topSlabModel, planksModel);
			generic3d(slab, mcLoc(slabName));
			// stairs
			StairBlock stairs = ArboricultureBlocks.STAIRS_VANILLA_FIREPROOF.get(woodType).block();
			String stairsName = woodTypeName + "_stairs";
			ModelFile stairsModel = existingMcBlock(stairsName);
			ModelFile innerStairsModel = existingMcBlock(stairsName + "_inner");
			ModelFile outerStairsModel = existingMcBlock(stairsName + "_outer");
			stairsBlock(stairs, stairsModel, innerStairsModel, outerStairsModel);
			generic3d(stairs, mcLoc(stairsName));
			// fence
			BlockForestryFence fence = ArboricultureBlocks.FENCES_VANILLA_FIREPROOF.get(woodType).block();
			String fenceName = woodTypeName + "_fence";
			ModelFile fencePostModel = existingMcBlock(fenceName + "_post");
			ModelFile fenceSideModel = existingMcBlock(fenceName + "_side");
			ModelFile fenceInventoryModel = existingMcBlock(fenceName + "_inventory");
			fourWayBlock(fence, fencePostModel, fenceSideModel);
			itemModels().withExistingParent(path(fence), fenceInventoryModel.getLocation());
			// fence gate
			BlockForestryFenceGate fenceGate = ArboricultureBlocks.FENCE_GATES_VANILLA_FIREPROOF.get(woodType).block();
			String fenceGateName = woodTypeName + "_fence_gate";
			ModelFile gateModel = existingMcBlock(fenceGateName);
			ModelFile gateOpenModel = existingMcBlock(fenceGateName + "_open");
			ModelFile gateWallModel = existingMcBlock(fenceGateName + "_wall");
			ModelFile gateWallOpenModel = existingMcBlock(fenceGateName + "_wall_open");
			fenceGateBlock(fenceGate, gateModel, gateOpenModel, gateWallModel, gateWallOpenModel);
			generic3d(fenceGate, mcLoc(fenceGateName));
		}

		// Forestry wood types
		for (EnumForestryWoodType woodType : EnumForestryWoodType.VALUES) {
			// Planks
			Block planks = ArboricultureBlocks.PLANKS.get(woodType).block();
			Block fireproofPlanks = ArboricultureBlocks.PLANKS_FIREPROOF.get(woodType).block();
			ModelFile planksModel = cubeAll(planks);

			simpleBlock(planks);
			simpleBlock(fireproofPlanks, planksModel);
			generic3d(planks);
			generic3d(fireproofPlanks, planks);

			// Logs, Wood
			BlockForestryLog log = ArboricultureBlocks.LOGS.get(woodType).block();
			ResourceLocation logTexture = blockTexture(log);

			logLike(woodType, ArboricultureBlocks.LOGS, ArboricultureBlocks.LOGS_FIREPROOF, logTexture, withSuffix(logTexture, "_top"));
			//logLike(woodType, ArboricultureBlocks.STRIPPED_LOGS, ArboricultureBlocks.STRIPPED_LOGS_FIREPROOF, withPrefix("stripped_", logTexture), withPrefix("stripped_", withSuffix(logTexture, "_top")));
			//logLike(woodType, ArboricultureBlocks.STRIPPED_WOOD, ArboricultureBlocks.STRIPPED_WOOD_FIREPROOF, withPrefix("stripped_", logTexture), withPrefix("stripped_", logTexture));
			logLike(woodType, ArboricultureBlocks.WOOD, ArboricultureBlocks.WOOD_FIREPROOF, logTexture, logTexture);

			// Slab
			BlockForestrySlab slab = ArboricultureBlocks.SLABS.get(woodType).block();
			BlockForestrySlab fireproofSlab = ArboricultureBlocks.SLABS_FIREPROOF.get(woodType).block();
			ResourceLocation planksLoc = blockTexture(planks);
			ModelFile bottomSlabModel = models().slab(path(slab), planksLoc, planksLoc, planksLoc);
			ModelFile topSlabModel = models().slabTop(path(slab) + "_top", planksLoc, planksLoc, planksLoc);
			slabBlock(slab, bottomSlabModel, topSlabModel, planksModel);
			slabBlock(fireproofSlab, bottomSlabModel, topSlabModel, planksModel);
			generic3d(slab);
			generic3d(fireproofSlab, slab);

			// Stairs
			BlockForestryStairs stairs = ArboricultureBlocks.STAIRS.get(woodType).block();
			BlockForestryStairs fireproofStairs = ArboricultureBlocks.STAIRS_FIREPROOF.get(woodType).block();
			ModelFile stairsModel = models().stairs(path(stairs), planksLoc, planksLoc, planksLoc);
			ModelFile innerStairsModel = models().stairsInner(path(stairs) + "_inner", planksLoc, planksLoc, planksLoc);
			ModelFile outerStairsModel = models().stairsOuter(path(stairs) + "_outer", planksLoc, planksLoc, planksLoc);
			stairsBlock(stairs, stairsModel, innerStairsModel, outerStairsModel);
			stairsBlock(fireproofStairs, stairsModel, innerStairsModel, outerStairsModel);
			generic3d(stairs);
			generic3d(fireproofStairs, stairs);

			// Fence
			BlockForestryFence fence = ArboricultureBlocks.FENCES.get(woodType).block();
			BlockForestryFence fireproofFence = ArboricultureBlocks.FENCES_FIREPROOF.get(woodType).block();
			ModelFile fencePostModel = models().fencePost(path(fence) + "_post", planksLoc);
			ModelFile fenceSideModel = models().fenceSide(path(fence) + "_side", planksLoc);
			ModelFile fenceInventoryModel = models().fenceInventory(path(fence) + "_inventory", planksLoc);
			fourWayBlock(fence, fencePostModel, fenceSideModel);
			fourWayBlock(fireproofFence, fencePostModel, fenceSideModel);
			itemModels().withExistingParent(path(fence), fenceInventoryModel.getLocation());
			itemModels().withExistingParent(path(fireproofFence), fenceInventoryModel.getLocation());

			// Fence Gate
			BlockForestryFenceGate fenceGate = ArboricultureBlocks.FENCE_GATES.get(woodType).block();
			BlockForestryFenceGate fireproofFenceGate = ArboricultureBlocks.FENCE_GATES_FIREPROOF.get(woodType).block();
			ModelFile gateModel = models().fenceGate(path(fenceGate), planksLoc);
			ModelFile gateOpenModel = models().fenceGateOpen(path(fenceGate) + "_open", planksLoc);
			ModelFile gateWallModel = models().fenceGateWall(path(fenceGate) + "_wall", planksLoc);
			ModelFile gateWallOpenModel = models().fenceGateWallOpen(path(fenceGate) + "_wall_open", planksLoc);
			fenceGateBlock(fenceGate, gateModel, gateOpenModel, gateWallModel, gateWallOpenModel);
			fenceGateBlock(fireproofFenceGate, gateModel, gateOpenModel, gateWallModel, gateWallOpenModel);
			generic3d(fenceGate);
			generic3d(fireproofFenceGate, fenceGate);

			// Door
			BlockForestryDoor door = ArboricultureBlocks.DOORS.get(woodType).block();
			doorBlock(door, withSuffix(blockTexture(door), "_bottom"), withSuffix(blockTexture(door), "_top"));
			generic2d(door);
		}
	}

	private void logLike(EnumForestryWoodType woodType, FeatureBlockGroup<BlockForestryLog, EnumForestryWoodType> logs, FeatureBlockGroup<BlockForestryLog, EnumForestryWoodType> fireproofLogs, ResourceLocation sideTexture, ResourceLocation topTexture) {
		BlockForestryLog wood = logs.get(woodType).block();
		BlockForestryLog fireproofWood = fireproofLogs.get(woodType).block();
		ModelFile woodModel = models().cubeColumn(path(wood), sideTexture, topTexture);
		axisBlock(wood, woodModel, woodModel);
		axisBlock(fireproofWood, woodModel, woodModel);
		generic3d(wood);
		generic3d(fireproofWood, wood);
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

	private ResourceLocation withSuffix(ResourceLocation loc, String suffix) {
		return new ResourceLocation(loc.getNamespace(), loc.getPath() + suffix);
	}

	public void generic3d(Block block, Block otherParent) {
		itemModels().withExistingParent(path(block), modLoc("block/" + path(otherParent)));
	}

	public void generic3d(Block block, ResourceLocation otherParentId) {
		itemModels().withExistingParent(path(block), new ResourceLocation(otherParentId.getNamespace(), "block/" + otherParentId.getPath()));
	}

	private ModelFile existingMcBlock(String path) {
		return models().getExistingFile(mcBlock(path));
	}

	// Everything below this line is boilerplate code adapted from https://github.com/thedarkcolour/ModKit
	// Makes a 3d cube of a block for item model
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
