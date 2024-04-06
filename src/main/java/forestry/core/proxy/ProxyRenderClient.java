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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.core.blocks.IColoredBlock;
import forestry.core.blocks.MachinePropertiesTesr;
import forestry.core.features.CoreBlocks;
import forestry.core.fluids.ForestryFluids;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.models.ClientManager;
import forestry.core.models.FluidContainerModel;
import forestry.core.render.ColourProperties;
import forestry.core.render.ForestrySpriteUploader;
import forestry.core.render.RenderAnalyzer;
import forestry.core.render.RenderEscritoire;
import forestry.core.render.RenderMachine;
import forestry.core.render.RenderMill;
import forestry.core.render.RenderNaturalistChest;
import forestry.core.render.TextureManagerForestry;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileBase;
import forestry.core.tiles.TileEscritoire;
import forestry.core.tiles.TileMill;
import forestry.core.tiles.TileNaturalistChest;
import forestry.modules.IClientModuleHandler;
import forestry.modules.ModuleManager;

public class ProxyRenderClient extends ProxyRender implements IClientModuleHandler {

	@Override
	public boolean fancyGraphicsEnabled() {
		return Minecraft.useFancyGraphics();
	}

	@SuppressWarnings("removal")
	@Override
	public void setupClient(FMLClientSetupEvent event) {
		CoreBlocks.BASE.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));

		for (ForestryFluids fluid : ForestryFluids.values()) {
			ItemBlockRenderTypes.setRenderLayer(fluid.getFluid(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(fluid.getFlowing(), RenderType.translucent());
		}
	}

	@Override
	public void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("fluid_container", FluidContainerModel.Loader.INSTANCE);
	}



	@Override
	public void bakeModels(ModelEvent.BakingCompleted event) {
		ClientManager.getInstance().onBakeModels(event);
	}

	@Override
	public void setRenderDefaultMachine(MachinePropertiesTesr<? extends TileBase> machineProperties, String baseTexture) {
		machineProperties.setRenderer(RenderMachine.MODEL_LAYER, part -> new RenderMachine(part, baseTexture));
	}

	@Override
	public void setRenderMill(MachinePropertiesTesr<? extends TileMill> machineProperties, String baseTexture) {
		machineProperties.setRenderer(RenderMill.MODEL_LAYER, part -> new RenderMill(part, baseTexture));
	}

	@Override
	public void setRenderEscritoire(MachinePropertiesTesr<? extends TileEscritoire> machineProperties) {
		machineProperties.setRenderer(RenderEscritoire.MODEL_LAYER, RenderEscritoire::new);
	}

	@Override
	public void setRendererAnalyzer(MachinePropertiesTesr<? extends TileAnalyzer> machineProperties) {
		machineProperties.setRenderer(RenderAnalyzer.MODEL_LAYER, RenderAnalyzer::new);
	}

	@Override
	public void setRenderChest(MachinePropertiesTesr<? extends TileNaturalistChest> machineProperties, String textureName) {
		machineProperties.setRenderer(RenderNaturalistChest.MODEL_LAYER, part -> new RenderNaturalistChest(part, textureName));
	}

	@Override
	public void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		ClientManager.getInstance().registerBlockColors(event);

		Minecraft minecraft = Minecraft.getInstance();
		ForestrySpriteUploader spriteUploader = new ForestrySpriteUploader(minecraft.textureManager, TextureManagerForestry.LOCATION_FORESTRY_TEXTURE, "gui");
		TextureManagerForestry.getInstance().init(spriteUploader);
		ResourceManager resourceManager = minecraft.getResourceManager();
		if (resourceManager instanceof ReloadableResourceManager reloadableManager) {
			reloadableManager.registerReloadListener(ColourProperties.INSTANCE);
			reloadableManager.registerReloadListener(GuiElementFactory.INSTANCE);
			reloadableManager.registerReloadListener(spriteUploader);
		}
		//EntriesCategory.registerSearchTree();
		ModuleManager.getModuleHandler().runClientInit();
	}

	@Override
	public void registerItemColors(RegisterColorHandlersEvent.Item event) {
		ClientManager.getInstance().registerItemColors(event);


	}

	@Override
	public void setupLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(RenderAnalyzer.MODEL_LAYER, RenderAnalyzer::createBodyLayer);
		event.registerLayerDefinition(RenderMachine.MODEL_LAYER, RenderMachine::createBodyLayer);
		
		event.registerLayerDefinition(RenderNaturalistChest.MODEL_LAYER, RenderNaturalistChest::createBodyLayer);
		event.registerLayerDefinition(RenderEscritoire.MODEL_LAYER, RenderEscritoire::createBodyLayer);
		event.registerLayerDefinition(RenderMill.MODEL_LAYER, RenderMill::createBodyLayer);
	}
}
