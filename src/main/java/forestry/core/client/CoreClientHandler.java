/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.client.IClientModuleHandler;
import forestry.api.client.IForestryClientApi;
import forestry.apiculture.features.ApicultureBlocks;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiimpl.plugin.PluginManager;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.circuits.GuiSolderingIron;
import forestry.core.config.Constants;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.core.features.CoreMenuTypes;
import forestry.core.features.CoreTiles;
import forestry.core.features.FluidsItems;
import forestry.core.fluids.ForestryFluids;
import forestry.core.gui.ContainerNaturalistInventory;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.GuiAnalyzer;
import forestry.core.gui.GuiEscritoire;
import forestry.core.gui.GuiNaturalistInventory;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.models.ClientManager;
import forestry.core.models.FluidContainerModel;
import forestry.core.models.ModelBlockCached;
import forestry.core.render.ColourProperties;
import forestry.core.render.ForestryBewlr;
import forestry.core.render.ForestryModelLayers;
import forestry.core.render.ForestryTextureManager;
import forestry.core.render.RenderAnalyzer;
import forestry.core.render.RenderEngine;
import forestry.core.render.RenderEscritoire;
import forestry.core.render.RenderMachine;
import forestry.core.render.RenderMill;
import forestry.core.render.RenderNaturalistChest;
import forestry.energy.features.EnergyTiles;
import forestry.factory.features.FactoryTiles;
import forestry.lepidopterology.features.LepidopterologyItems;
import forestry.mail.features.MailItems;
import forestry.storage.features.BackpackItems;
import forestry.storage.features.CrateItems;

public class CoreClientHandler implements IClientModuleHandler {
	public static BlockEntityWithoutLevelRenderer bewlr;

	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(CoreClientHandler::onClientSetup);
		modBus.addListener(CoreClientHandler::registerModelLoaders);
		modBus.addListener(CoreClientHandler::bakeModels);
		modBus.addListener(CoreClientHandler::setupLayers);
		modBus.addListener(CoreClientHandler::clientSetupRenderers);
		modBus.addListener(CoreClientHandler::handleTextureRemap);
		modBus.addListener(CoreClientHandler::registerReloadListeners);
		modBus.addListener(CoreClientHandler::registerBlockColors);
		modBus.addListener(CoreClientHandler::registerItemColors);
	}

	private static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			CoreBlocks.BASE.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));

			for (ForestryFluids fluid : ForestryFluids.values()) {
				ItemBlockRenderTypes.setRenderLayer(fluid.getFluid(), RenderType.translucent());
				ItemBlockRenderTypes.setRenderLayer(fluid.getFlowing(), RenderType.translucent());
			}

			MenuScreens.register(CoreMenuTypes.ALYZER.menuType(), GuiAlyzer::new);
			MenuScreens.register(CoreMenuTypes.ANALYZER.menuType(), GuiAnalyzer::new);
			MenuScreens.register(CoreMenuTypes.NATURALIST_INVENTORY.menuType(), GuiNaturalistInventory<ContainerNaturalistInventory>::new);
			MenuScreens.register(CoreMenuTypes.ESCRITOIRE.menuType(), GuiEscritoire::new);
			MenuScreens.register(CoreMenuTypes.SOLDERING_IRON.menuType(), GuiSolderingIron::new);
		});

		bewlr = new ForestryBewlr(Minecraft.getInstance().getBlockEntityRenderDispatcher());
	}

	private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("fluid_container", FluidContainerModel.Loader.INSTANCE);

		PluginManager.registerClient();
	}

	private static void bakeModels(ModelEvent.BakingCompleted event) {
		ClientManager.INSTANCE.onBakeModels(event);
	}

	private static void setupLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ForestryModelLayers.ANALYZER_LAYER, RenderAnalyzer::createBodyLayer);
		event.registerLayerDefinition(ForestryModelLayers.MACHINE_LAYER, RenderMachine::createBodyLayer);
		event.registerLayerDefinition(ForestryModelLayers.NATURALIST_CHEST_LAYER, RenderNaturalistChest::createBodyLayer);
		event.registerLayerDefinition(ForestryModelLayers.ESCRITOIRE_LAYER, RenderEscritoire::createBodyLayer);
		event.registerLayerDefinition(ForestryModelLayers.MILL_LAYER, RenderMill::createBodyLayer);
		event.registerLayerDefinition(ForestryModelLayers.ENGINE_LAYER, RenderEngine::createBodyLayer);
	}

	// todo switch to using Vanilla renderers directly + ditch render helper object
	private static void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		// Core
		event.registerBlockEntityRenderer(CoreTiles.ANALYZER.tileType(), RenderAnalyzer::new);
		event.registerBlockEntityRenderer(CoreTiles.ESCRITOIRE.tileType(), RenderEscritoire::new);
		// Apiculture/Arboriculture/Lepidopterology
		event.registerBlockEntityRenderer(CoreTiles.APIARIST_CHEST.tileType(), ctx -> new RenderNaturalistChest(ctx, "apiaristchest"));
		event.registerBlockEntityRenderer(CoreTiles.ARBORIST_CHEST.tileType(), ctx -> new RenderNaturalistChest(ctx, "arbchest"));
		event.registerBlockEntityRenderer(CoreTiles.LEPIDOPTERIST_CHEST.tileType(), ctx -> new RenderNaturalistChest(ctx, "lepichest"));
		// Engine
		event.registerBlockEntityRenderer(EnergyTiles.CLOCKWORK_ENGINE.tileType(), ctx -> new RenderEngine(ctx, Constants.TEXTURE_PATH_BLOCK + "/engine_clock_"));
		event.registerBlockEntityRenderer(EnergyTiles.BIOGAS_ENGINE.tileType(), ctx -> new RenderEngine(ctx, Constants.TEXTURE_PATH_BLOCK + "/engine_bronze_"));
		event.registerBlockEntityRenderer(EnergyTiles.PEAT_ENGINE.tileType(), ctx -> new RenderEngine(ctx, Constants.TEXTURE_PATH_BLOCK + "/engine_copper_"));
		// Factory
		event.registerBlockEntityRenderer(FactoryTiles.BOTTLER.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/bottler_"));
		event.registerBlockEntityRenderer(FactoryTiles.CARPENTER.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/carpenter_"));
		event.registerBlockEntityRenderer(FactoryTiles.CENTRIFUGE.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/centrifuge_"));
		event.registerBlockEntityRenderer(FactoryTiles.FERMENTER.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/fermenter_"));
		event.registerBlockEntityRenderer(FactoryTiles.MOISTENER.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/moistener_"));
		event.registerBlockEntityRenderer(FactoryTiles.SQUEEZER.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/squeezer_"));
		event.registerBlockEntityRenderer(FactoryTiles.STILL.tileType(), ctx -> new RenderMachine(ctx, Constants.TEXTURE_PATH_BLOCK + "/still_"));
		event.registerBlockEntityRenderer(FactoryTiles.RAINMAKER.tileType(), ctx -> new RenderMill(ctx, Constants.TEXTURE_PATH_BLOCK + "/rainmaker_"));
	}

	private static void handleTextureRemap(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
			ModelBlockCached.clear();
		}
	}

	private static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(((ForestryTextureManager) IForestryClientApi.INSTANCE.getTextureManager()).getSpriteUploader());
		event.registerReloadListener(ColourProperties.INSTANCE);
		event.registerReloadListener(GuiElementFactory.INSTANCE);

		((ForestryTextureManager) IForestryClientApi.INSTANCE.getTextureManager()).init();
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		// Apiculture
		event.register(ClientManager.FORESTRY_BLOCK_COLOR, ApicultureBlocks.BEE_COMB.blockArray());
		// Arboriculture
		event.register(ClientManager.FORESTRY_BLOCK_COLOR, ArboricultureBlocks.LEAVES.block());
		event.register(ClientManager.FORESTRY_BLOCK_COLOR, ArboricultureBlocks.LEAVES_DEFAULT.blockArray());
		event.register(ClientManager.FORESTRY_BLOCK_COLOR, ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.blockArray());
		event.register(ClientManager.FORESTRY_BLOCK_COLOR, ArboricultureBlocks.LEAVES_DECORATIVE.blockArray());
	}

	private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		// Core
		event.register(ClientManager.FORESTRY_ITEM_COLOR, CoreItems.ELECTRON_TUBES.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, CoreItems.CIRCUITBOARDS.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, FluidsItems.CONTAINERS.itemArray());
		// Apiculture
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ApicultureBlocks.BEE_COMB.blockArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR,
				ApicultureItems.BEE_QUEEN.item(),
				ApicultureItems.BEE_DRONE.item(),
				ApicultureItems.BEE_PRINCESS.item(),
				ApicultureItems.BEE_LARVAE.item()
		);
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ApicultureItems.HONEY_DROPS.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ApicultureItems.PROPOLIS.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ApicultureItems.POLLEN_CLUSTER.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ApicultureItems.BEE_COMBS.itemArray());

		// Arboriculture
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureBlocks.LEAVES.block());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureBlocks.LEAVES_DEFAULT.blockArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.blockArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureBlocks.LEAVES_DECORATIVE.blockArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureItems.SAPLING.item());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, ArboricultureItems.POLLEN_FERTILE.item());

		// Lepidopterology
		event.register(ClientManager.FORESTRY_ITEM_COLOR, LepidopterologyItems.CATERPILLAR_GE.item());
		event.register(ClientManager.FORESTRY_ITEM_COLOR, LepidopterologyItems.SERUM_GE.item());

		// Backpacks
		event.register(ClientManager.FORESTRY_ITEM_COLOR,
				BackpackItems.APIARIST_BACKPACK.item(),
				BackpackItems.LEPIDOPTERIST_BACKPACK.item(),
				BackpackItems.MINER_BACKPACK.item(),
				BackpackItems.MINER_BACKPACK_T_2.item(),
				BackpackItems.DIGGER_BACKPACK.item(),
				BackpackItems.DIGGER_BACKPACK_T_2.item(),
				BackpackItems.FORESTER_BACKPACK.item(),
				BackpackItems.FORESTER_BACKPACK_T_2.item(),
				BackpackItems.HUNTER_BACKPACK.item(),
				BackpackItems.HUNTER_BACKPACK_T_2.item(),
				BackpackItems.ADVENTURER_BACKPACK.item(),
				BackpackItems.ADVENTURER_BACKPACK_T_2.item(),
				BackpackItems.BUILDER_BACKPACK.item(),
				BackpackItems.BUILDER_BACKPACK_T_2.item()
		);

		// Crates
		event.register(ClientManager.FORESTRY_ITEM_COLOR, CrateItems.CRATED_BEE_COMBS.itemArray());
		event.register(ClientManager.FORESTRY_ITEM_COLOR,
				CrateItems.CRATED_GRASS_BLOCK.item(),
				CrateItems.CRATED_POLLEN_CLUSTER_NORMAL.item(),
				CrateItems.CRATED_POLLEN_CLUSTER_CRYSTALLINE.item(),
				CrateItems.CRATED_PROPOLIS.item());

		// Mail
		event.register(ClientManager.FORESTRY_ITEM_COLOR, MailItems.STAMPS.itemArray());
	}
}
