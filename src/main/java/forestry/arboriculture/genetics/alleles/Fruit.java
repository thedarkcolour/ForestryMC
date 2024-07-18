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
package forestry.arboriculture.genetics.alleles;

import javax.annotation.Nullable;

import net.minecraft.network.chat.MutableComponent;

import genetics.api.alleles.AlleleCategorized;

import forestry.api.arboriculture.IFruitProvider;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.core.ISetupListener;

public class Fruit extends AlleleCategorized implements IFruit, ISetupListener {
	private final IFruitProvider provider;

	public Fruit(String name, IFruitProvider provider) {
		this(name, provider, false);
	}

	public Fruit(String name, IFruitProvider provider, boolean isDominant) {
		super(provider.getModID(), "fruit", name, isDominant);
		this.provider = provider;
	}

	@Override
	public IFruitProvider getProvider() {
		return this.provider;
	}

	@Override
	public MutableComponent getDisplayName() {
		return getProvider().getDescription();
	}

	@Nullable
	@Override
	public String getModelName() {
		return getProvider().getModelName();
	}

	@Override
	public int compareTo(IFruit o) {
		return 0;
	}

	@Override
	public void onStartSetup() {
		provider.onStartSetup();
	}

	@Override
	public void onFinishSetup() {
		provider.onFinishSetup();
	}
}
