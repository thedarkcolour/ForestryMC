/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import java.util.Locale;

import forestry.api.genetics.ILifeStage;

public enum BeeLifeStage implements ILifeStage {
	DRONE, PRINCESS, QUEEN, LARVAE;

	private final String name;

	BeeLifeStage() {
		this.name = name().toLowerCase(Locale.ROOT);
	}

	@Override
	public String getName() {
		return this.name;
	}
}
