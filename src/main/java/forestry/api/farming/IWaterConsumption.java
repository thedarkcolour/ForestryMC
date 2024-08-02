package forestry.api.farming;

/**
 * Used to determine the water consumption of a Forestry farm.
 */
@FunctionalInterface
public interface IWaterConsumption {
	/**
	 * Determines water consumption based on the farm's housing state and climate.
	 *
	 * @param housing           The farm housing.
	 * @param hydrationModifier A product of the farm's temperature, humidity, and rainfall.
	 * @return The water consumption.
	 */
	int get(IFarmHousing housing, float hydrationModifier);
}
