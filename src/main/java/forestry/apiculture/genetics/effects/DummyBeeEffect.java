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

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.genetics.IBeeEffect;

// A bee effect that does nothing. Used in the default "none" as well as for the Leporine bee's Easter effect.
public class DummyBeeEffect implements IBeeEffect {
	private final ResourceLocation dominantId;
	private final ResourceLocation recessiveId;

	public DummyBeeEffect(String modId, String name) {
		this.dominantId = new ResourceLocation(modId, "bee_effect_" + name + 'd');
		this.recessiveId = new ResourceLocation(modId, "bee_effect_" + name);
	}

	@Override
	public ResourceLocation id(boolean dominant) {
		return dominant ? this.dominantId : this.recessiveId;
	}
}
