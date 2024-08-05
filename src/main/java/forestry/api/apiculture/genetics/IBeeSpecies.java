package forestry.api.apiculture.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.state.BlockState;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;
import forestry.api.core.Product;

public interface IBeeSpecies extends ISpecies<IBee> {
	@Override
	IBeeSpeciesType getType();

	/**
	 * @return The list of possible items that can be produced by this bee.
	 */
	List<Product> getProducts();

	/**
	 * @return The list of possible items that this bee can only produce when in a jubilant state.
	 */
	List<Product> getSpecialties();

	/**
	 * @return The preferred/ideal temperature for this bee.
	 */
	TemperatureType getTemperature();

	/**
	 * @return The preferred/ideal humidity for this bee.
	 */
	HumidityType getHumidity();

	boolean isNocturnal();

	boolean isJubilant(IGenome genome, IBeeHousing housing);

	/**
	 * @return The color of the bee's body. Used for tintIndex = 1 and is usually a shade of yellow, {@code 0xffdc16}.
	 */
	int getBody();

	/**
	 * @return The color of the bee's stripes. Used for tintIndex = 2 and is usually black.
	 */
	int getStripes();

	/**
	 * @return The color of the bee's outline. Used for tintIndex = 0.
	 */
	int getOutline();
}
