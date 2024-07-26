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
package forestry.lepidopterology.proxy;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.client.IClientModuleHandler;
import forestry.lepidopterology.features.LepidopterologyEntities;
import forestry.lepidopterology.render.ButterflyEntityRenderer;
import forestry.lepidopterology.render.ButterflyItemModel;
import forestry.lepidopterology.render.ButterflyModel;

public class LepidopterologyClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(LepidopterologyClientHandler::setupRenderers);
		modBus.addListener(LepidopterologyClientHandler::setupLayers);
		modBus.addListener(LepidopterologyClientHandler::registerModelLoaders);
	}

	public static void setupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(LepidopterologyEntities.BUTTERFLY.entityType(), ButterflyEntityRenderer::new);
	}

	public static void setupLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ButterflyModel.LAYER, ButterflyModel::createLayer);
	}

	public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("butterfly_ge", new ButterflyItemModel.Loader());
	}
}
