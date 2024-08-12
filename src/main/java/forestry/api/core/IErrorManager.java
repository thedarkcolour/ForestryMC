/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;

/**
 * Keeps track of error information used by Forestry.
 * Register errors using {@link forestry.api.plugin.IForestryPlugin#registerErrors}.
 */
public interface IErrorManager {
	/**
	 * Retrieves an error by its numeric ID.
	 *
	 * @param id The numeric ID of the error.
	 * @return The error with the given ID, or {@code null} if no error is registered with that ID.
	 */
	@Nullable
	IError getError(short id);

	/**
	 * Retrieves an error by its unique ID. Safe for NBT serialization.
	 *
	 * @param errorId The ID of the error.
	 * @return The error with the ID.
	 */
	@Nullable
	IError getError(ResourceLocation errorId);

	/**
	 * @return A set of all registered errors.
	 */
	List<IError> getErrors();

	/**
	 * @return A new instance of an IErrorLogic.
	 */
	IErrorLogic createErrorLogic();

	/**
	 * Retrieves the numeric ID of this error. Only use for network synchronization.
	 *
	 * @param error The error.
	 * @return The numeric ID of the error.
	 */
	short getNumericId(IError error);
}
