/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************//*

package forestry.climatology.proxy;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.climatology.PreviewHandlerClient;
import forestry.climatology.features.ClimatologyMenuTypes;
import forestry.climatology.gui.GuiHabitatFormer;
import forestry.api.client.IClientModuleHandler;

public class ClimatologyClimateHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		MinecraftForge.EVENT_BUS.register(new PreviewHandlerClient());
		modBus.addListener(ClimatologyClimateHandler::setupClient);
	}

	public static void setupClient(FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(ClimatologyMenuTypes.HABITAT_FORMER.menuType(), GuiHabitatFormer::new));
	}
}
*/
