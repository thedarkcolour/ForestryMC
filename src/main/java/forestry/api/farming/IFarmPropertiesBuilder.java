package forestry.api.farming;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;

public interface IFarmPropertiesBuilder {
	IFarmPropertiesBuilder setIcon(Supplier<ItemStack> stackSupplier);

	default IFarmPropertiesBuilder setFertilizer(int fertilizer) {
		return setFertilizer((housing) -> fertilizer);
	}

	IFarmPropertiesBuilder setFertilizer(ToIntFunction<IFarmHousing> consumption);

	default IFarmPropertiesBuilder setWaterConsumption(int waterConsumption) {
		return setWaterConsumption((housing, hydrationModifier) -> waterConsumption);
	}

	default IFarmPropertiesBuilder setWaterConsumption(Float2IntFunction waterConsumption) {
		return setWaterConsumption((housing, hydrationModifier) -> waterConsumption.get(hydrationModifier));
	}

	IFarmPropertiesBuilder setWaterConsumption(IWaterConsumption waterConsumption);

	default IFarmPropertiesBuilder addSoil(Block block) {
		return addSoil(new ItemStack(block), block.defaultBlockState());
	}

	IFarmPropertiesBuilder addSoil(ItemStack resource, BlockState soilState);

	IFarmPropertiesBuilder addSeedlings(ItemStack... seedling);

	IFarmPropertiesBuilder addSeedlings(Collection<ItemStack> seedling);

	IFarmPropertiesBuilder addProducts(ItemStack... products);

	IFarmPropertiesBuilder addProducts(Collection<ItemStack> products);

	IFarmPropertiesBuilder addFarmables(String... identifiers);

	IFarmPropertiesBuilder setFactory(BiFunction<IFarmProperties, Boolean, IFarmLogic> factory);

	IFarmPropertiesBuilder setTranslationKey(String translationKey);

	IFarmProperties create();
}
