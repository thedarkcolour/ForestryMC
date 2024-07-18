/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import java.util.Locale;

import forestry.api.genetics.ILifeStage;

public enum ButterflyLifeStage implements ILifeStage {
	BUTTERFLY, SERUM, CATERPILLAR, COCOON;

	public String getName() {
		return toString().toLowerCase(Locale.ROOT);
	}
}
