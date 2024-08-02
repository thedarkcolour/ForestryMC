package forestry.farming;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import forestry.api.farming.IFarmType;
import forestry.api.farming.IFarmingManager;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class FarmingManager implements IFarmingManager {
	private final Object2IntOpenHashMap<Ingredient> fertilizers;
	private final ImmutableMap<ResourceLocation, IFarmType> farmTypes;

	public FarmingManager(Object2IntOpenHashMap<Ingredient> fertilizers, ImmutableMap<ResourceLocation, IFarmType> farmTypes) {
		this.farmTypes = farmTypes;
		this.fertilizers = fertilizers;
	}

	@Override
	public int getFertilizeValue(ItemStack stack) {
		for (Object2IntMap.Entry<Ingredient> fertilizer : this.fertilizers.object2IntEntrySet()) {
			if (fertilizer.getKey().test(stack)) {
				return fertilizer.getIntValue();
			}
		}
		return 0;
	}

	@Nullable
	@Override
	public IFarmType getFarmType(ResourceLocation id) {
		return this.farmTypes.get(id);
	}
}
