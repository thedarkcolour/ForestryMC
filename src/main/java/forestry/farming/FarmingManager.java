package forestry.farming;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import forestry.api.farming.IFarmType;
import forestry.api.farming.IFarmingManager;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class FarmingManager implements IFarmingManager {
	private final Object2IntOpenHashMap<Item> fertilizers;
	private final ImmutableMap<ResourceLocation, IFarmType> farmTypes;

	public FarmingManager(Object2IntOpenHashMap<Item> fertilizers, ImmutableMap<ResourceLocation, IFarmType> farmTypes) {
		this.farmTypes = farmTypes;
		this.fertilizers = fertilizers;
	}

	@Override
	public int getFertilizeValue(ItemStack stack) {
		return this.fertilizers.getInt(stack.getItem());
	}

	@Nullable
	@Override
	public IFarmType getFarmType(ResourceLocation id) {
		return this.farmTypes.get(id);
	}
}
