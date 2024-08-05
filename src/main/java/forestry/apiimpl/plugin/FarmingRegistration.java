package forestry.apiimpl.plugin;

import com.google.common.collect.ImmutableMap;

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
	private final ModifiableRegistrar<ResourceLocation, IFarmTypeBuilder, FarmTypeBuilder> farmTypes = new ModifiableRegistrar<>(IFarmTypeBuilder.class);
	private final Object2IntOpenHashMap<Ingredient> fertilizers = new Object2IntOpenHashMap<>();

	@Override
	public IFarmTypeBuilder createFarmType(ResourceLocation id, BiFunction<IFarmType, Boolean, IFarmLogic> logicFactory, ItemStack icon) {
		return this.farmTypes.create(id, new FarmTypeBuilder(id, logicFactory, icon));
	}

	@Override
	public void modifyFarmType(ResourceLocation id, Consumer<IFarmTypeBuilder> action) {
		this.farmTypes.modify(id, action);
	}

	@Override
	public void registerFertilizer(Ingredient fertilizer, int amount) {
		this.fertilizers.put(fertilizer, amount);
	}

	public Object2IntOpenHashMap<Ingredient> getFertilizers() {
		return this.fertilizers;
	}

	public ImmutableMap<ResourceLocation, IFarmType> buildFarmTypes() {
		return this.farmTypes.build(FarmTypeBuilder::build);
	}
}
