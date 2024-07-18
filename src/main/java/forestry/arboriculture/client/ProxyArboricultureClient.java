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
import net.minecraft.world.level.FoliageColor;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.genetics.alleles.AlleleFruits;
import forestry.arboriculture.models.ModelDecorativeLeaves;
import forestry.arboriculture.models.ModelDefaultLeaves;
import forestry.arboriculture.models.ModelDefaultLeavesFruit;
import forestry.arboriculture.models.ModelLeaves;
import forestry.arboriculture.models.SaplingModelLoader;
import forestry.arboriculture.models.LeafTexture;
import forestry.core.models.ClientManager;
import forestry.api.client.IClientModuleHandler;

@OnlyIn(Dist.CLIENT)
public class ProxyArboricultureClient extends ProxyArboriculture implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ProxyArboricultureClient::registerModelLoaders);
	}

	@Override
	public void initializeModels() {
		ClientManager clientManager = ClientManager.INSTANCE;
		clientManager.registerModel(new ModelLeaves(), ArboricultureBlocks.LEAVES);
		clientManager.registerModel(new ModelDecorativeLeaves(), ArboricultureBlocks.LEAVES_DECORATIVE);
		clientManager.registerModel(new ModelDefaultLeaves(), ArboricultureBlocks.LEAVES_DEFAULT);
		clientManager.registerModel(new ModelDefaultLeavesFruit(), ArboricultureBlocks.LEAVES_DEFAULT_FRUIT);
	}

	@Override
	public int getFoliageColorDefault() {
		return FoliageColor.getDefaultColor();
	}

	@Override
	public int getFoliageColorBirch() {
		return FoliageColor.getBirchColor();
	}

	@Override
	public int getFoliageColorSpruce() {
		return FoliageColor.getEvergreenColor();
	}

	@Override
	public void registerSprites(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location() != InventoryMenu.BLOCK_ATLAS) {
			return;
		}
		LeafTexture.registerAllSprites(event);
		for (IFruit alleleFruit : AlleleFruits.getFruitAlleles()) {
			alleleFruit.getProvider().registerSprites(event);
		}
	}

	@SuppressWarnings("removal")
	@Override
	public void setupClient(FMLClientSetupEvent event) {
		// fruit overlays require CUTOUT_MIPPED, even in Fast graphics
		ArboricultureBlocks.LEAVES_DEFAULT.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
		ItemBlockRenderTypes.setRenderLayer(ArboricultureBlocks.LEAVES.block(), RenderType.cutoutMipped());
		ArboricultureBlocks.LEAVES_DEFAULT_FRUIT.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
		ArboricultureBlocks.LEAVES_DECORATIVE.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()));
		ItemBlockRenderTypes.setRenderLayer(ArboricultureBlocks.SAPLING_GE.block(), RenderType.cutout());
		ArboricultureBlocks.DOORS.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent()));
	}

	public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("sapling_ge", SaplingModelLoader.INSTANCE);
	}
}
