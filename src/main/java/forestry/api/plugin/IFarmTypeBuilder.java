package forestry.api.plugin;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmLogic;
import forestry.api.farming.IFarmType;
import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableFactory;
import forestry.api.farming.IWaterConsumption;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;

/**
 * Configures a new farm type.
 * It is REQUIRED to call {@link #setFertilizerConsumption} and {@link #setWaterConsumption}.
 */
public interface IFarmTypeBuilder {
	/**
	 * Used to override the icon used to display this farm in the Multiblock farm screen.
	 *
	 * @param stack The item to render as this farm type's icon.
	 */
	IFarmTypeBuilder setIcon(ItemStack stack);

	/**
	 * Used to override the IFarmLogic factory used to create farm logic instances for this farm type.
	 *
	 * @param factory A function for creating IFarmLogic. The boolean determines whether the logic should be manual.
	 */
	IFarmTypeBuilder setLogicFactory(BiFunction<IFarmType, Boolean, IFarmLogic> factory);

	/**
	 * Sets the fertilizer consumption as a flat rate. A single Forestry fertilizer is worth {@code 500}.
	 *
	 * @param consumption The amount of fertilizer used to harvest a single crop.
	 */
	default IFarmTypeBuilder setFertilizerConsumption(int consumption) {
		return setFertilizerConsumption(housing -> consumption);
	}

	/**
	 * Sets the fertilizer consumption according to the housing's state. A single Forestry fertilizer is worth {@code 500}.
	 *
	 * @param consumption The amount of fertilizer used to harvest a single crop based on the farm housing's state.
	 */
	IFarmTypeBuilder setFertilizerConsumption(ToIntFunction<IFarmHousing> consumption);

	/**
	 * Sets the water consumption of this farm as a flat rate.
	 *
	 * @param waterConsumption The rate of water consumption.
	 */
	default IFarmTypeBuilder setWaterConsumption(int waterConsumption) {
		return setWaterConsumption((housing, hydrationModifier) -> waterConsumption);
	}

	/**
	 * Sets the water consumption of this farm as a function of the farm's hydration modifier.
	 * The hydration modifier is the product of the farm's temperature, humidity, and rainfall modifiers.
	 *
	 * @param waterConsumption The water consumption according to the farm's climate.
	 */
	default IFarmTypeBuilder setWaterConsumption(Float2IntFunction waterConsumption) {
		return setWaterConsumption((housing, hydrationModifier) -> waterConsumption.get(hydrationModifier));
	}

	/**
	 * Sets the water consumption of this farm according to the farm's hydration modifier and state.
	 * The hydration modifier is the product of the farm's temperature, humidity, and rainfall modifiers.
	 *
	 * @param waterConsumption The water consumption according to the farm's climate and state.
	 */
	IFarmTypeBuilder setWaterConsumption(IWaterConsumption waterConsumption);

	/**
	 * Adds a soil used by this block for automatic planting.
	 *
	 * @param block The block form of the soil to use.
	 */
	default IFarmTypeBuilder addSoil(Block block) {
		return addSoil(new ItemStack(block), block.defaultBlockState());
	}

	/**
	 * Adds a soil used by this block for automatic planting.
	 *
	 * @param resource  The item form of the soil to be consumed by the farm in order to plant the soil.
	 * @param soilState The block state of the soil used for automatic planting.
	 */
	IFarmTypeBuilder addSoil(ItemStack resource, BlockState soilState);

	/**
	 * Adds a germling (seed item) to this farm type for displaying in JEI.
	 *
	 * @param germling The germling to add.
	 */
	IFarmTypeBuilder addGermling(ItemStack germling);

	/**
	 * Adds multiple germlings to this farm type for displaying in JEI.
	 *
	 * @param seedling The seedling to add.
	 */
	IFarmTypeBuilder addGermlings(Iterable<ItemStack> seedling);

	/**
	 * Adds a product to this farm type for displaying in JEI.
	 *
	 * @param product The product to add.
	 */
	IFarmTypeBuilder addProduct(ItemStack product);

	/**
	 * Adds multiple products to this farm type for displaying in JEI.
	 * Prefer {@link IFarmable#addProducts} if you have a farmable.
	 *
	 * @param products The products to add.
	 */
	IFarmTypeBuilder addProducts(Collection<ItemStack> products);

	/**
	 * Adds a custom farmable to this type. Does not support modifications by other mods.
	 * For adding a farmable that can have additional windfall items added to it, use {@link #addWindfallFarmable}.
	 *
	 * @param farmable The non-customizable implementation of farmable to add.
	 */
	IFarmTypeBuilder addFarmable(IFarmable farmable);

	/**
	 * Adds a new farmable to this farm type. Supports modifications by other mods using {@link #modifyWindfallFarmable}.
	 *
	 * @param germling The germling item used to uniquely identify this farmable, since farmables do not have IDs.
	 * @param factory  Factory used to construct the farmable when registration is complete. Accepts a germling and
	 *                 immutable set of windfall items, used by {@link IFarmable#isGermling} and {@link IFarmable#isWindfall}.
	 * @param action   A consumer that accepts windfall items. Windfall items include drops from blocks not directly
	 *                 harvested by the farm type, for example, the leaf blocks left behind by the Arboretum.
	 */
	IFarmTypeBuilder addWindfallFarmable(Item germling, IFarmableFactory factory, Consumer<IWindfallFarmableBuilder> action);

	/**
	 * Used to modify an already added windfall farmable for this farm type.
	 * Can be used to add additional windfall items (ex. modded leaf drops for the oak sapling plantable).
	 *
	 * @param germling The germling associated with the target farmable.
	 * @param action   A consumer that accepts windfall items.
	 */
	IFarmTypeBuilder modifyWindfallFarmable(Item germling, Consumer<IWindfallFarmableBuilder> action);
}
