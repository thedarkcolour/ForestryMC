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
 * @param fuel          Item that is valid fuel for a peat-fired engine.
 * @param powerPerCycle Power produced by this fuel per work cycle.
 * @param burnDuration  Amount of work cycles this item lasts before being consumed.
 */
public record EngineCopperFuel(ItemStack fuel, int powerPerCycle, int burnDuration) {
}
