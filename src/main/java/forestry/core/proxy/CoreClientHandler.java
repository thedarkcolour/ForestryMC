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
package forestry.core.proxy;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.client.IForestryClientApi;
import forestry.core.blocks.IMachinePropertiesTesr;
import forestry.core.blocks.MachinePropertiesTesr;
import forestry.core.circuits.GuiSolderingIron;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreMenuTypes;
import forestry.core.fluids.ForestryFluids;
import forestry.core.gui.ContainerNaturalistInventory;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.GuiAnalyzer;
import forestry.core.gui.GuiEscritoire;
import forestry.core.gui.GuiNaturalistInventory;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.models.ClientManager;
import forestry.core.models.FluidContainerModel;
import forestry.core.render.ColourProperties;
import forestry.core.render.ForestryTextureManager;
import forestry.core.render.RenderAnalyzer;
import forestry.core.render.RenderEngine;
import forestry.core.render.RenderEscritoire;
import forestry.core.render.RenderMachine;
import forestry.core.render.RenderMill;
import forestry.core.render.RenderNaturalistChest;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileBase;
import forestry.core.tiles.TileEscritoire;
import forestry.core.tiles.TileMill;
import forestry.core.tiles.TileNaturalistChest;
import forestry.energy.tiles.EngineBlockEntity;
import forestry.api.client.IClientModuleHandler;

public class CoreClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(CoreClientHandler::setupClient);
		modBus.addListener(CoreClientHandler::registerModelLoaders);
		modBus.addListener(CoreClientHandler::bakeModels);
		modBus.addListener(CoreClientHandler::registerReloadListeners);
		modBus.addListener(CoreClientHandler::registerBlockColors);
		modBus.addListener(CoreClientHandler::registerItemColors);
		modBus.addListener(CoreClientHandler::setupLayers);
	}

	private static void setupClient(FMLClientSetupEvent event) {
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
	}

	private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("fluid_container", FluidContainerModel.Loader.INSTANCE);
	}

	private static void bakeModels(ModelEvent.BakingCompleted event) {
		ClientManager.INSTANCE.onBakeModels(event);
	}

	public void setRenderDefaultMachine(IMachinePropertiesTesr<? extends TileBase> machineProperties, String baseTexture) {
		machineProperties.setRenderer(RenderMachine.MODEL_LAYER, part -> new RenderMachine(part, baseTexture));
	}

	public void setRenderMill(IMachinePropertiesTesr<? extends TileMill> machineProperties, String baseTexture) {
		machineProperties.setRenderer(RenderMill.MODEL_LAYER, part -> new RenderMill(part, baseTexture));
	}

	public void setRenderDefaultEngine(MachinePropertiesTesr<? extends EngineBlockEntity> properties, String baseTexture) {
		properties.setRenderer(RenderEngine.MODEL_LAYER, root -> new RenderEngine(root, baseTexture));
	}

	public void setRenderEscritoire(IMachinePropertiesTesr<? extends TileEscritoire> machineProperties) {
		machineProperties.setRenderer(RenderEscritoire.MODEL_LAYER, RenderEscritoire::new);
	}

	public void setRendererAnalyzer(IMachinePropertiesTesr<? extends TileAnalyzer> machineProperties) {
		machineProperties.setRenderer(RenderAnalyzer.MODEL_LAYER, RenderAnalyzer::new);
	}

	public void setRenderChest(IMachinePropertiesTesr<? extends TileNaturalistChest> machineProperties, String textureName) {
		machineProperties.setRenderer(RenderNaturalistChest.MODEL_LAYER, part -> new RenderNaturalistChest(part, textureName));
	}

	private static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(((ForestryTextureManager) IForestryClientApi.INSTANCE.getTextureManager()).getSpriteUploader());
		event.registerReloadListener(ColourProperties.INSTANCE);
		event.registerReloadListener(GuiElementFactory.INSTANCE);
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		ClientManager.INSTANCE.registerBlockColors(event);
	}

	private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		ClientManager.INSTANCE.registerItemColors(event);
	}

	private static void setupLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(RenderAnalyzer.MODEL_LAYER, RenderAnalyzer::createBodyLayer);
		event.registerLayerDefinition(RenderMachine.MODEL_LAYER, RenderMachine::createBodyLayer);

		event.registerLayerDefinition(RenderNaturalistChest.MODEL_LAYER, RenderNaturalistChest::createBodyLayer);
		event.registerLayerDefinition(RenderEscritoire.MODEL_LAYER, RenderEscritoire::createBodyLayer);
		event.registerLayerDefinition(RenderMill.MODEL_LAYER, RenderMill::createBodyLayer);
		event.registerLayerDefinition(RenderEngine.MODEL_LAYER, RenderEngine::createBodyLayer);
	}
}
