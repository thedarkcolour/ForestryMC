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
package forestry.core.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import forestry.Forestry;

public enum ColourProperties implements ResourceManagerReloadListener {
	INSTANCE;

	private final Properties defaultMappings = new Properties();
	private final Properties mappings = new Properties();

	public synchronized int get(String key) {
		return Integer.parseInt(mappings.getProperty(key, defaultMappings.getProperty(key, "d67fff")), 16);
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		try {
			InputStream defaultFontStream = ColourProperties.class.getResourceAsStream("/config/forestry/colour.properties");
			mappings.load(defaultFontStream);
			defaultMappings.load(defaultFontStream);

			defaultFontStream.close();
		} catch (IOException e) {
			Forestry.LOGGER.error("Failed to load colors.properties.", e);
		}
	}

}
