/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import java.util.List;

import net.minecraft.world.item.Item;

import forestry.api.genetics.ILifeStage;
import forestry.arboriculture.features.ArboricultureItems;

public enum TreeLifeStage implements ILifeStage {
	SAPLING("sapling"),
	POLLEN("pollen");

	public static final List<TreeLifeStage> VALUES = List.of(values());

	private final String translationKey;

	TreeLifeStage(String translationKey) {
		this.translationKey = translationKey;
	}

	public String getSerializedName() {
		return this.translationKey;
	}

	@Override
	public Item getItemForm() {
		return this == SAPLING ? ArboricultureItems.SAPLING.item() : ArboricultureItems.POLLEN_FERTILE.item();
	}

}
