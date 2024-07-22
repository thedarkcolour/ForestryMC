package forestry.api.apiculture.genetics;

import java.util.List;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.Product;

public interface IBeeSpecies extends ISpecies<IBee> {
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

	@Override
	IBeeSpeciesType getType();

	boolean isJubilant(IGenome genome, IBeeHousing housing);
}
