package forestry.api.farming;

import java.util.Collection;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An IFarmType describes a type of farm, defining rules for how the farm can operate.
 * A farm type contains two instances of {@link IFarmLogic}, one manual, and one managed.
 * In base Forestry, each farm type has two legacy farm blocks and one circuit upgrade, so the farm type can be used in
 * both legacy block farms and in a quadrant of a multiblock farm.
 */
public interface IFarmType {
	/**
	 * Determines how much fertilizer should be used to harvest a single crop.
	 * Forestry's default Fertilizer is worth {@code 500} fertilizer per item.
	 *
	 * @param housing The farm housing.
	 * @return The amount of fertilizer used to harvest a single crop.
	 */
	int getFertilizerConsumption(IFarmHousing housing);

	/**
	 * @param hydrationModifier A modifier that depends on the weather and the biome of the farm.
	 * @return The amount of water that the {@link IFarmHousing} automatically removes after this logic cultivated
	 * a block or harvested a crop.
	 */
	int getWaterConsumption(IFarmHousing housing, float hydrationModifier);

	Component getDisplayName(boolean manual);

	String getTranslationKey();

	/**
	 * @return the itemStack that represents this farm logic. Used as an icon for the farm logic.
	 */
	ItemStack getIcon();

	/**
	 * @return {@code true} if the given block state is a valid soil state.
	 */
	boolean isAcceptedSoil(BlockState state);

	/**
	 * @return {@code true} if the given stack is the item form of a soil.
	 */
	boolean isAcceptedResource(ItemStack stack);

	/**
	 * Checks if the given stack is a seedling (plantable sapling, seed, etc.) for any {@link IFarmable} of this farm.
	 */
	boolean isAcceptedSeedling(ItemStack stack);

	boolean isAcceptedWindfall(ItemStack stack);

	Collection<Soil> getSoils();

	/**
	 * @return The farmable resources produced by this farm type.
	 */
	List<IFarmable> getFarmables();

	/**
	 * Returns the instance of the manual or managed {@link IFarmLogic}.
	 */
	IFarmLogic getLogic(boolean manual);
}
