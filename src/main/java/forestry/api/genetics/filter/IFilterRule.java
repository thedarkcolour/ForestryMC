package forestry.api.genetics.filter;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IFilterRule {
	boolean isValid(ItemStack itemStack, IFilterData data);

	/**
	 * If a species type with this uid is registered, the filter will only get stack with individuals from this root.
	 */
	@Nullable
	default ResourceLocation getSpeciesTypeId() {
		return null;
	}
}
