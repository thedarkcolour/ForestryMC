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
import net.minecraft.world.item.Item;

public class ProxyCommon {
	// todo make sure to call this on each element in deferred register
	public void registerItem(Item item) {

	}

	// todo make sure to call this on each element in deferred register, used to be in IBlockFeature.register
	public void registerBlock(Block block) {

	}

	public File getForestryRoot() {
		return new File(".");
	}
}
