package forestry.plugin;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import forestry.api.farming.IFarmLogic;
import forestry.api.farming.IFarmType;
import forestry.api.plugin.IFarmTypeBuilder;
import forestry.api.plugin.IFarmingRegistration;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class FarmingRegistration implements IFarmingRegistration {
	private final HashMap<ResourceLocation, FarmTypeBuilder> builders = new HashMap<>();
	private final HashMap<ResourceLocation, Consumer<IFarmTypeBuilder>> modifications = new HashMap<>();
	private final Object2IntOpenHashMap<Ingredient> fertilizers = new Object2IntOpenHashMap<>();

	@Override
	public IFarmTypeBuilder createFarmType(ResourceLocation id, BiFunction<IFarmType, Boolean, IFarmLogic> logicFactory, ItemStack icon) {
		if (this.builders.containsKey(id)) {
			throw new IllegalStateException("An IFarmTypeBuilder has already been registered with ID: " + id);
		} else {
			FarmTypeBuilder builder = new FarmTypeBuilder(id, logicFactory, icon);
			this.builders.put(id, builder);
			return builder;
		}
	}

	@Override
	public void modifyFarmType(ResourceLocation id, Consumer<IFarmTypeBuilder> action) {
		this.modifications.merge(id, action, Consumer::andThen);
	}

	@Override
	public void registerFertilizer(Ingredient fertilizer, int amount) {
		this.fertilizers.put(fertilizer, amount);
	}

	public Object2IntOpenHashMap<Ingredient> getFertilizers() {
		return this.fertilizers;
	}

	public ImmutableMap<ResourceLocation, IFarmType> buildFarmTypes() {
		for (Map.Entry<ResourceLocation, Consumer<IFarmTypeBuilder>> entry : this.modifications.entrySet()) {
			IFarmTypeBuilder builder = this.builders.get(entry.getKey());

			// todo should this throw? what if mods want to optionally customize a farm type without making another plugin?
			if (builder == null) {
				throw new IllegalStateException("Tried customizing non-existent farm type with ID " + entry.getKey());
			}

			entry.getValue().accept(builder);
		}

		ImmutableMap.Builder<ResourceLocation, IFarmType> types = ImmutableMap.builderWithExpectedSize(this.builders.size());

		for (Map.Entry<ResourceLocation, FarmTypeBuilder> entry : this.builders.entrySet()) {
			types.put(entry.getKey(), entry.getValue().build());
		}

		return types.buildOrThrow();
	}
}
