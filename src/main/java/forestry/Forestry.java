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

package forestry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.common.Mod;

import forestry.api.ForestryConstants;
import forestry.api.IForestryApi;
import forestry.core.EventHandlerCore;
import forestry.core.network.NetworkHandler;
import forestry.modules.ForestryModuleManager;
import forestry.plugin.PluginManager;

/**
 * Forestry Minecraft Mod
 *
 * @author SirSengir
 */
@Mod(ForestryConstants.MOD_ID)
public class Forestry {
	public static final Logger LOGGER = LogManager.getLogger(ForestryConstants.MOD_ID);

	public Forestry() {
		ForestryModuleManager moduleManager = (ForestryModuleManager) IForestryApi.INSTANCE.getModuleManager();
		moduleManager.init();
		NetworkHandler.register();
		MinecraftForge.EVENT_BUS.register(EventHandlerCore.class);

		PluginManager.loadPlugins();
		PluginManager.registerErrors();
	}
}
