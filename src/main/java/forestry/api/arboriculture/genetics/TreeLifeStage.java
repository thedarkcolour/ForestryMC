/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import forestry.api.genetics.ILifeStage;

public enum TreeLifeStage implements ILifeStage {
	SAPLING("sapling"), POLLEN("pollen");

	public static final TreeLifeStage[] VALUES = values();

	private final String name;

	TreeLifeStage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
