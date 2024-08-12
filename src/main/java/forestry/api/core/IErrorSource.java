/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import java.util.Set;

public interface IErrorSource {
	IErrorSource EMPTY = Set::of;

	/**
	 * @return The current errors in this error source, or an empty set if there are no errors.
	 */
	Set<IError> getErrors();
}
