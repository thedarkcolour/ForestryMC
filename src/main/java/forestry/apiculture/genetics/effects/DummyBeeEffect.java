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
package forestry.apiculture.genetics.effects;

import forestry.api.apiculture.genetics.IBeeEffect;

// A bee effect that does nothing. Used in the default "none" as well as for the Leporine bee's Easter effect.
public class DummyBeeEffect implements IBeeEffect {
	private final boolean dominant;

	public DummyBeeEffect(boolean dominant) {
		this.dominant = dominant;
	}

	@Override
	public boolean isDominant() {
		return this.dominant;
	}
}
