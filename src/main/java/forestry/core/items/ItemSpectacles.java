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
package forestry.core.items;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import forestry.api.ForestryConstants;
import forestry.api.core.ItemGroups;
import forestry.core.config.Constants;
import forestry.core.utils.ItemTooltipUtil;

public class ItemSpectacles extends ArmorItem {
	public static final String TEXTURE_NATURALIST_ARMOR_PRIMARY = ForestryConstants.MOD_ID + ":" + Constants.TEXTURE_PATH_ITEM + "/naturalist_armor_1.png";

	public ItemSpectacles() {
		super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, (new Item.Properties()).durability(100).tab(ItemGroups.tabForestry));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return TEXTURE_NATURALIST_ARMOR_PRIMARY;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag advanced) {
		ItemTooltipUtil.addInformation(stack, level, tooltip, advanced);
	}
}
