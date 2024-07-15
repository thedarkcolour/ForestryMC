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
package forestry.apiculture.proxy;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.apiculture.features.ApicultureBlocks;
import forestry.apiculture.features.ApicultureMenuTypes;
import forestry.apiculture.gui.ContainerBeeHousing;
import forestry.apiculture.gui.GuiAlveary;
import forestry.apiculture.gui.GuiAlvearyHygroregulator;
import forestry.apiculture.gui.GuiAlvearySieve;
import forestry.apiculture.gui.GuiAlvearySwarmer;
import forestry.apiculture.gui.GuiBeeHousing;
import forestry.apiculture.gui.GuiHabitatLocator;
import forestry.apiculture.gui.GuiImprinter;
import forestry.apiculture.particles.ParticleSnow;
import forestry.api.client.IClientModuleHandler;

public class ApicultureClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ApicultureClientHandler::setupClient);
		modBus.addListener(ApicultureClientHandler::handleSprites);
	}

	public static void setupClient(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ApicultureBlocks.BEE_COMB.getBlocks().forEach((block) -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()));

			MenuScreens.register(ApicultureMenuTypes.ALVEARY.menuType(), GuiAlveary::new);
			MenuScreens.register(ApicultureMenuTypes.ALVEARY_HYGROREGULATOR.menuType(), GuiAlvearyHygroregulator::new);
			MenuScreens.register(ApicultureMenuTypes.ALVEARY_SIEVE.menuType(), GuiAlvearySieve::new);
			MenuScreens.register(ApicultureMenuTypes.ALVEARY_SWARMER.menuType(), GuiAlvearySwarmer::new);
			MenuScreens.register(ApicultureMenuTypes.BEE_HOUSING.menuType(), GuiBeeHousing<ContainerBeeHousing>::new);
			MenuScreens.register(ApicultureMenuTypes.HABITAT_LOCATOR.menuType(), GuiHabitatLocator::new);
			MenuScreens.register(ApicultureMenuTypes.IMPRINTER.menuType(), GuiImprinter::new);
		});
	}

	public static void handleSprites(TextureStitchEvent.Post event) {
		TextureAtlas map = event.getAtlas();
		if (!map.location().equals(TextureAtlas.LOCATION_PARTICLES)) {
			return;
		}
		for (int i = 0; i < ParticleSnow.sprites.length; i++) {
			ParticleSnow.sprites[i] = map.getSprite(new ResourceLocation("forestry:particle/snow." + (i + 1)));
		}
	}
}
