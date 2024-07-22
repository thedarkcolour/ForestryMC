/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import java.util.List;
import java.util.Locale;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.ILifeStage;
import forestry.apiculture.features.ApicultureItems;

public enum BeeLifeStage implements ILifeStage {
	DRONE,
	PRINCESS,
	QUEEN,
	LARVAE;

	public static final List<BeeLifeStage> VALUES = List.of(values());

	private final String name;

	BeeLifeStage() {
		this.name = name().toLowerCase(Locale.ROOT);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ItemStack getItemForm() {
		return switch (this) {
			case DRONE -> ApicultureItems.BEE_DRONE.stack();
			case PRINCESS -> ApicultureItems.BEE_PRINCESS.stack();
			case QUEEN -> ApicultureItems.BEE_QUEEN.stack();
			case LARVAE -> ApicultureItems.BEE_LARVAE.stack();
		};
	}
}
