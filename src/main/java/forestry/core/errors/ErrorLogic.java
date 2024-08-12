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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import forestry.api.core.IErrorLogic;
import forestry.api.core.IError;

public class ErrorLogic implements IErrorLogic {
	private final Set<IError> errors = new HashSet<>();

	@Override
	public boolean setCondition(boolean condition, IError error) {
		if (condition) {
			this.errors.add(error);
		} else {
			this.errors.remove(error);
		}
		return condition;
	}

	@Override
	public boolean contains(IError error) {
		return this.errors.contains(error);
	}

	@Override
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}

	@Override
	public Set<IError> getErrors() {
		return Collections.unmodifiableSet(this.errors);
	}

	@Override
	public void clearErrors() {
		this.errors.clear();
	}
}
