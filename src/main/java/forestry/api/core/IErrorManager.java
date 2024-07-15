/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

public interface IErrorManager {
	// todo move into IErrorRegistration or something
	void registerError(IError state);

	@Nullable
	IError getError(short id);

	IError getError(ResourceLocation name);

	Collection<IError> getRegisteredErrors();

	IErrorLogic createErrorLogic();

	short getNumericId(IError error);
}
