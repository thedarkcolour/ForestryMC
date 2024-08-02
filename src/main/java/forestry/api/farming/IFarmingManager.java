/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.farming;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IFarmingManager {
	default List<IFarmable> getFarmables(ResourceLocation farmTypeId) {
		IFarmType farmType = getFarmType(farmTypeId);
		return farmType == null ? List.of() : farmType.getFarmables();
	}

	/**
	 * @return The value of the fertilizer when used in a farm.
	 */
	int getFertilizeValue(ItemStack stack);

	@Nullable
	IFarmType getFarmType(ResourceLocation id);
}
