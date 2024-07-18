package forestry.api.plugin;

import java.awt.Color;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

/**
 * Builder used to register new butterfly species and configure already existing ones.
 * Use {@link ILepidopterologyRegistration#registerSpecies} to obtain instances of this class.
 */
public interface IButterflySpeciesBuilder extends ISpeciesBuilder<IButterflySpeciesBuilder> {
	/**
	 * Overrides the serum color set in {@link ILepidopterologyRegistration#registerSpecies}.
	 */
	IButterflySpeciesBuilder setSerumColor(Color color);

	/**
	 * Overrides the rarity set in {@link ILepidopterologyRegistration#registerSpecies}.
	 */
	IButterflySpeciesBuilder setRarity(float rarity);
}
