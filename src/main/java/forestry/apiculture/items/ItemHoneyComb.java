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
package forestry.apiculture.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.data.loading.DatagenModLoader;

import forestry.api.core.ItemGroups;
import forestry.core.items.ItemForestry;
import forestry.core.items.definitions.IColoredItem;

public class ItemHoneyComb extends ItemForestry implements IColoredItem {
	private final EnumHoneyComb type;

	public ItemHoneyComb(EnumHoneyComb type) {
		super(new Item.Properties().tab((type.isUnused() && !DatagenModLoader.isRunningDataGen()) ? null : ItemGroups.tabApiculture));

		this.type = type;
	}

	public boolean isUnused() {
		return this.type.isUnused();
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int tintIndex) {
		EnumHoneyComb honeyComb = this.type;
		if (tintIndex == 1) {
			return honeyComb.primaryColor;
		} else {
			return honeyComb.secondaryColor;
		}
	}
}
