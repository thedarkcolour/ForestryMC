/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.errors;

import java.util.Set;

import forestry.api.core.IError;
import forestry.api.core.IErrorSource;

public enum FakeErrorSource implements IErrorSource {
	INSTANCE;

	@Override
	public Set<IError> getErrors() {
		return Set.of();
	}
}
