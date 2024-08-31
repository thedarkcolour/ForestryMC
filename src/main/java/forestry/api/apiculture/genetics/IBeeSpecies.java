package forestry.api.apiculture.genetics;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.core.HumidityType;
import forestry.api.core.IProductProducer;
import forestry.api.core.ISpecialtyProducer;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;

public interface IBeeSpecies extends ISpecies<IBee>, IProductProducer, ISpecialtyProducer {
	@Override
	IBeeSpeciesType getType();

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
