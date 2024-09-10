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
package forestry.arboriculture.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.client.IClientModuleHandler;
import forestry.api.client.IForestryClientApi;
import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.models.ModelDecorativeLeaves;
import forestry.arboriculture.models.ModelDefaultLeaves;
import forestry.arboriculture.models.ModelDefaultLeavesFruit;
import forestry.arboriculture.models.ModelLeaves;
import forestry.arboriculture.models.SaplingModelLoader;
import forestry.core.models.ClientManager;

public class ArboricultureClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ArboricultureClientHandler::registerModelLoaders);
		modBus.addListener(ArboricultureClientHandler::registerSprites);
		modBus.addListener(ArboricultureClientHandler::onClientSetup);
	}

	@SuppressWarnings("removal")
	private static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ClientManager clientManager = ClientManager.INSTANCE;
			clientManager.registerModel(new ModelLeaves(), ArboricultureBlocks.LEAVES);
			clientManager.registerModel(new ModelDecorativeLeaves<>(BlockDecorativeLeaves.class), ArboricultureBlocks.LEAVES_DECORATIVE);
			clientManager.registerModel(new ModelDefaultLeaves(), ArboricultureBlocks.LEAVES_DEFAULT);
			clientManager.registerModel(new ModelDefaultLeavesFruit(), ArboricultureBlocks.LEAVES_DEFAULT_FRUIT);

			// fruit overlays require CUTOUT_MIPPED, even in Fast graphics
			ArboricultureBlocks.LEAVES_DEFAULT.getBlocks().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
			ItemBlockRenderTypes.setRenderLayer(ArboricultureBlocks.LEAVES.block(), RenderType.cutoutMipped());
			ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.getBlocks().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
			ArboricultureBlocks.LEAVES_DECORATIVE.getBlocks().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
			ItemBlockRenderTypes.setRenderLayer(ArboricultureBlocks.SAPLING_GE.block(), RenderType.cutout());
			ArboricultureBlocks.DOORS.getBlocks().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent()));

			ArboricultureBlocks.PODS.getBlocks().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
		});
	}

	private static void registerSprites(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
			// todo move into IClientRegistration
			for (IFruit fruit : TreeChromosomes.FRUIT.values()) {
				fruit.registerSprites(event);
			}
			for (ILeafSprite sprite : IForestryClientApi.INSTANCE.getTreeManager().getAllLeafSprites()) {
				event.addSprite(sprite.get(true, true));
				event.addSprite(sprite.get(true, false));
				event.addSprite(sprite.get(false, true));
				event.addSprite(sprite.get(false, false));
			}
		}
	}

	private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("sapling_ge", new SaplingModelLoader());
	}
}
