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

import forestry.api.ForestryConstants;

/**
 * Packets sent to the server from the client
 */
public class PacketIdServer {
	// Core Gui
	public static final ResourceLocation GUI_SELECTION_REQUEST = ForestryConstants.forestry("gui_selection_request");
	public static final ResourceLocation PIPETTE_CLICK = ForestryConstants.forestry("pipette_click");
	public static final ResourceLocation CHIPSET_CLICK = ForestryConstants.forestry("chipset_click");
	public static final ResourceLocation SOLDERING_IRON_CLICK = ForestryConstants.forestry("soldering_iron_click");
	// Climate
	public static final ResourceLocation SELECT_CLIMATE_TARGETED = ForestryConstants.forestry("select_climate_targeted");
	public static final ResourceLocation CLIMATE_LISTENER_UPDATE_REQUEST = ForestryConstants.forestry("climate_listener_update_request");
	// Database
	public static final ResourceLocation INSERT_ITEM = ForestryConstants.forestry("insert_item");
	public static final ResourceLocation EXTRACT_ITEM = ForestryConstants.forestry("extract_item");
	// Sorting
	public static final ResourceLocation FILTER_CHANGE_RULE = ForestryConstants.forestry("filter_change_rule");
	public static final ResourceLocation FILTER_CHANGE_GENOME = ForestryConstants.forestry("filter_change_genome");
	// JEI
	public static final ResourceLocation WORKTABLE_RECIPE_REQUEST = ForestryConstants.forestry("worktable_recipe_request");
	public static final ResourceLocation RECIPE_TRANSFER_REQUEST = ForestryConstants.forestry("recipe_transfer_request");
	// Mail
	public static final ResourceLocation LETTER_INFO_REQUEST = ForestryConstants.forestry("letter_info_request");
	public static final ResourceLocation TRADING_ADDRESS_REQUEST = ForestryConstants.forestry("trading_address_request");
	public static final ResourceLocation LETTER_TEXT_SET = ForestryConstants.forestry("letter_text_set");
}
