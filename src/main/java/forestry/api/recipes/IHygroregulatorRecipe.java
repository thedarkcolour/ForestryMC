/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.recipes;

import net.minecraftforge.fluids.FluidStack;

public interface IHygroregulatorRecipe extends IForestryRecipe {
	/**
	 * @return FluidStack containing information on fluid and amount.
	 */
	FluidStack getInputFluid();

	/**
	 * @return How long the temperature change from this recipe will last before more fluid is consumed.
	 */
	int getRetainTime();

	/**
	 * @return The humidity change that this recipe causes in one work cycle.
	 */
	byte getHumiditySteps();

	/**
	 * @return The temperature change that this recipe causes in one work cycle.
	 */
	byte getTemperatureSteps();
}
