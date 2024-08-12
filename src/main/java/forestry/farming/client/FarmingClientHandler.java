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
package forestry.farming.client;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.farming.features.FarmingMenuTypes;
import forestry.farming.gui.GuiFarm;
import forestry.api.client.IClientModuleHandler;

public class FarmingClientHandler implements IClientModuleHandler {
	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(FarmingClientHandler::onClientSetup);
	}

	private static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(FarmingMenuTypes.FARM.menuType(), GuiFarm::new));
	}
}
