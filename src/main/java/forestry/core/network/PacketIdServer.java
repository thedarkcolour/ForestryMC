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
package forestry.core.network;

import net.minecraft.resources.ResourceLocation;

import forestry.core.utils.ModUtil;

/**
 * Packets sent to the server from the client
 */
public class PacketIdServer {
	// Core Gui
	public static final ResourceLocation GUI_SELECTION_REQUEST = ModUtil.modLoc("gui_selection_request");
	public static final ResourceLocation PIPETTE_CLICK = ModUtil.modLoc("pipette_click");
	public static final ResourceLocation CHIPSET_CLICK = ModUtil.modLoc("chipset_click");
	public static final ResourceLocation SOLDERING_IRON_CLICK = ModUtil.modLoc("soldering_iron_click");
	// Climate
	public static final ResourceLocation SELECT_CLIMATE_TARGETED = ModUtil.modLoc("select_climate_targeted");
	public static final ResourceLocation CLIMATE_LISTENER_UPDATE_REQUEST = ModUtil.modLoc("climate_listener_update_request");
	// Database
	public static final ResourceLocation INSERT_ITEM = ModUtil.modLoc("insert_item");
	public static final ResourceLocation EXTRACT_ITEM = ModUtil.modLoc("extract_item");
	// Sorting
	public static final ResourceLocation FILTER_CHANGE_RULE = ModUtil.modLoc("filter_change_rule");
	public static final ResourceLocation FILTER_CHANGE_GENOME = ModUtil.modLoc("filter_change_genome");
	// JEI
	public static final ResourceLocation WORKTABLE_RECIPE_REQUEST = ModUtil.modLoc("worktable_recipe_request");
	public static final ResourceLocation RECIPE_TRANSFER_REQUEST = ModUtil.modLoc("recipe_transfer_request");
	// Mail
	public static final ResourceLocation LETTER_INFO_REQUEST = ModUtil.modLoc("letter_info_request");
	public static final ResourceLocation TRADING_ADDRESS_REQUEST = ModUtil.modLoc("trading_address_request");
	public static final ResourceLocation LETTER_TEXT_SET = ModUtil.modLoc("letter_text_set");
}
