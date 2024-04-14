/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.fuels;

import net.minecraft.world.item.ItemStack;

/**
 * todo data driven
 *
 * @param item Rain substrate capable of activating the rainmaker.
 * @param duration Duration of the rain shower triggered by this substrate in Minecraft ticks.
 * @param speed Speed of activation sequence triggered.
 * @param reverse Whether the substrate stops rain instead of creating rain.
 */
public record RainSubstrate(ItemStack item, int duration, float speed, boolean reverse) {
	public RainSubstrate(ItemStack item, float speed) {
		this(item, 0, speed, true);
	}

	public RainSubstrate(ItemStack item, int duration, float speed) {
		this(item, duration, speed, false);
	}
}
