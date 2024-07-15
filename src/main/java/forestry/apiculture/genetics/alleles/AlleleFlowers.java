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
package forestry.apiculture.genetics.alleles;

import net.minecraft.network.chat.MutableComponent;

import genetics.api.alleles.AlleleCategorized;

import forestry.api.genetics.alleles.IAlleleFlowers;
import forestry.api.genetics.flowers.IFlowerProvider;

public class AlleleFlowers extends AlleleCategorized implements IAlleleFlowers {
	private final IFlowerProvider provider;

	public AlleleFlowers(String modId, String category, String name, IFlowerProvider provider, boolean isDominant) {
		super(modId, category, name, isDominant);
		this.provider = provider;
	}

	@Override
	public IFlowerProvider getType() {
		return provider;
	}

	@Override
	public MutableComponent getDisplayName() {
		return getType().getDescription();
	}
}
