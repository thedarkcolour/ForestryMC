/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

/**
 * Some miscellaneous lists and settings for bees.
 *
 * @author SirSengir
 */
public class BeeManager {
	/**
	 * List of items that can induce swarming. Integer denotes x in 1000 chance.
	 */
	public static final Map<ItemStack, Integer> inducers = new HashMap<>();

	/**
	 * Used to check whether a player is wearing Apiarist Armor.
	 *
	 * @implNote Only null if the "apiculture" module is not enabled.
	 */
	@Nullable
	public static IArmorApiaristHelper armorApiaristHelper;
}
