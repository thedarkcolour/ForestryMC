/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.fuels;

import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class FuelManager {
	/**
	 * Add new fuels for the fermenter here (i.e. fertilizer).
	 */
	public static Map<ItemStack, FermenterFuel> fermenterFuel;
	/**
	 * Add new resources for the moistener here (i.e. wheat)
	 */
	public static Map<ItemStack, MoistenerFuel> moistenerResource;
	/**
	 * Add new substrates for the rainmaker here
	 */
	public static Map<ItemStack, RainSubstrate> rainSubstrate;
	/**
	 * Add new fuels for EngineBronze (= biogas engine) here
	 */
	public static Map<Fluid, EngineBronzeFuel> biogasEngineFuel;
	/**
	 * Add new fuels for EngineCopper (= peat-fired engine) here
	 */
	public static Map<ItemStack, EngineCopperFuel> peatEngineFuel;

}
