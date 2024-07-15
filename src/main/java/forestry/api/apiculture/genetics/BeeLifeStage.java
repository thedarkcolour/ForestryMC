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

	public static final BeeLifeStage[] VALUES = values();

	private final String name;

	BeeLifeStage() {
		this.name = this.toString().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String getName() {
		return name;
	}
}
