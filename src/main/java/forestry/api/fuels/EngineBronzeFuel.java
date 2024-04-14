/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.fuels;

import net.minecraft.world.level.material.Fluid;

/**
 * todo data driven
 *
 * @param liquid                Fluid that is valid fuel for a biogas engine.
 * @param powerPerCycle         Power produced by this fuel per work cycle of the engine.
 * @param burnDuration          How many work cycles a single "stack" of this type lasts.
 * @param dissipationMultiplier By how much the normal heat dissipation rate of 1 is multiplied when using this fuel type.
 */
public record EngineBronzeFuel(Fluid liquid, int powerPerCycle, int burnDuration, int dissipationMultiplier) {
}
