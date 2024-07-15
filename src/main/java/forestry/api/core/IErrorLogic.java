/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import java.util.Set;

import forestry.api.IForestryApi;

/**
 * Keeps track of all errors for an object.
 * Create new instances using {@link IErrorManager#createErrorLogic()}.
 */
public interface IErrorLogic extends IErrorSource {
	/**
	 * Adds the error when condition is true, removes it when condition is false.
	 *
	 * @return The value of condition.
	 */
	boolean setCondition(boolean condition, IError error);

	/**
	 * @return {@code true} If this error logic currently has the given error.
	 */
	boolean contains(IError error);

	/**
	 * @return {@code true} if this error logic currently has any errors.
	 */
	boolean hasErrors();

	/**
	 * Removes all current errors.
	 */
	void clearErrors();

	/**
	 * @return The current errors as an array of numeric IDs. Used for network serialization.
	 */
	default short[] toArray() {
		IErrorManager manager = IForestryApi.INSTANCE.getErrorManager();
		Set<IError> errors = getErrors();
		short[] statesArray = new short[errors.size()];
		int i = 0;
		for (IError error : errors) {
			statesArray[i] = manager.getNumericId(error);
			i++;
		}
		return statesArray;
	}

	/**
	 * Sets this logic's errors using the specified errors array. Unknown errors are discarded.
	 * @param errorArray An array of numeric IDs that correspond to different errors.
	 */
	default void fromArray(short[] errorArray) {
		clearErrors();
		IErrorManager manager = IForestryApi.INSTANCE.getErrorManager();
		for (short errorId : errorArray) {
			IError error = manager.getError(errorId);
			if (error != null) {
				setCondition(true, error);
			}
		}
	}
}
