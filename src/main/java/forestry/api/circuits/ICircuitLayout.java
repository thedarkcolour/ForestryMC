/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.circuits;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ICircuitLayout {

	/**
	 * unique ID for this circuit layout
	 */
	String getUID();

	/**
	 * localized name for this circuit layout
	 */
	Component getName();

	/**
	 * localized string for how this circuit layout is used
	 */
	MutableComponent getUsage();

	/**
	 * Specifies where a circuit layout is used.
	 */
	ICircuitSocketType getSocketType();

}
