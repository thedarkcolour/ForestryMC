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
 * Packets sent to the client from the server
 */
public class PacketIdClient {
	// Core Gui
	public static final ResourceLocation ERROR_UPDATE = ModUtil.modLoc("error_update");
	public static final ResourceLocation GUI_UPDATE = ModUtil.modLoc("gui_update");
	public static final ResourceLocation GUI_LAYOUT_SELECT = ModUtil.modLoc("gui_layout_select");
	public static final ResourceLocation GUI_ENERGY = ModUtil.modLoc("gui_energy");
	public static final ResourceLocation SOCKET_UPDATE = ModUtil.modLoc("socket_update");
	// Core Tile Entities
	public static final ResourceLocation TILE_FORESTRY_UPDATE = ModUtil.modLoc("tile_forestry_update");
	public static final ResourceLocation ITEMSTACK_DISPLAY = ModUtil.modLoc("itemstack_display");
	public static final ResourceLocation FX_SIGNAL = ModUtil.modLoc("fx_signal");
	public static final ResourceLocation TANK_LEVEL_UPDATE = ModUtil.modLoc("tank_level_update");
	// Core Genome
	public static final ResourceLocation GENOME_TRACKER_UPDATE = ModUtil.modLoc("genome_tracker_update");
	// Factory
	public static final ResourceLocation WORKTABLE_MEMORY_UPDATE = ModUtil.modLoc("worktable_memory_update");
	public static final ResourceLocation WORKTABLE_CRAFTING_UPDATE = ModUtil.modLoc("worktable_crafting_update");
	// Apiculture
	public static final ResourceLocation TILE_FORESTRY_ACTIVE = ModUtil.modLoc("tile_forestry_active");
	public static final ResourceLocation BEE_LOGIC_ACTIVE = ModUtil.modLoc("bee_logic_active");
	public static final ResourceLocation HABITAT_BIOME_POINTER = ModUtil.modLoc("habitat_biome_pointer");
	public static final ResourceLocation IMPRINT_SELECTION_RESPONSE = ModUtil.modLoc("imprint_selection_response");
	public static final ResourceLocation ALVERAY_CONTROLLER_CHANGE = ModUtil.modLoc("alveray_controller_change");
	// Arboriculture
	public static final ResourceLocation RIPENING_UPDATE = ModUtil.modLoc("ripening_update");
	// Mail
	public static final ResourceLocation TRADING_ADDRESS_RESPONSE = ModUtil.modLoc("trading_address_response");
	public static final ResourceLocation LETTER_INFO_RESPONSE_PLAYER = ModUtil.modLoc("letter_info_response_player");
	public static final ResourceLocation LETTER_INFO_RESPONSE_TRADER = ModUtil.modLoc("letter_info_response_trader");
	public static final ResourceLocation POBOX_INFO_RESPONSE = ModUtil.modLoc("pobox_info_response");
	// Climate
	public static final ResourceLocation UPDATE_CLIMATE = ModUtil.modLoc("update_climate");
	public static final ResourceLocation CLIMATE_LISTENER_UPDATE = ModUtil.modLoc("climate_listener_update");
	public static final ResourceLocation CLIMATE_PLAYER = ModUtil.modLoc("climate_player");
	// Sorting
	public static final ResourceLocation GUI_UPDATE_FILTER = ModUtil.modLoc("gui_update_filter");
	// JEI
	public static final ResourceLocation RECIPE_TRANSFER_UPDATE = ModUtil.modLoc("recipe_transfer_update");
}
