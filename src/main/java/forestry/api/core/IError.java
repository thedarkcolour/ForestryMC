/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.ISpriteRegister;
import forestry.api.client.ITextureManager;

/**
 * An error describes when a certain working condition is not met and how to resolve the error.
 */
public interface IError {
	/**
	 * @return The unique ID for this error.
	 */
	ResourceLocation getId();

	/**
	 * @return Translation key for a short name that succinctly describes the error. Ex. "Too Hot"
	 */
	String getUnlocalizedDescription();

	/**
	 * @return Translation key for a detailed message on how to fix the error. Ex. "Move the bees to a cooler climate."
	 */
	String getUnlocalizedHelp();

	/**
	 * @return Location of an icon sprite registered to the Forestry texture manager at {@link ITextureManager}.
	 */
	ResourceLocation getSprite();
}
