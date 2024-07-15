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

import java.io.File;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;

import forestry.api.client.IForestryClientApi;
import forestry.core.models.ClientManager;
import forestry.core.render.ForestryTextureManager;

public class ProxyCommonClient extends ProxyCommon {
	private final ForestryTextureManager manager = (ForestryTextureManager) IForestryClientApi.INSTANCE.getTextureManager();

	@Override
	public void registerBlock(Block block) {
		ClientManager.INSTANCE.registerBlockClient(block);
		this.manager.registerBlock(block);
	}

	@Override
	public void registerItem(Item item) {
		ClientManager.INSTANCE.registerItemClient(item);
		this.manager.registerItem(item);
	}

	@Override
	public File getForestryRoot() {
		return Minecraft.getInstance().gameDirectory;
	}
}
