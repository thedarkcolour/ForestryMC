/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ISpecies;
import forestry.api.core.Product;

public interface IButterflySpecies extends ISpecies<IButterfly> {
	@Override
	IButterflySpeciesType getType();

	/**
	 * @return The ideal temperature for this butterfly to spawn and fly around in.
	 */
	TemperatureType getTemperature();

	/**
	 * @return The ideal humidity for this butterfly to spawn and fly around in.
	 */
	HumidityType getHumidity();

	/**
	 * Allows butterflies to restrict random spawns beyond the restrictions set by temperature() and humidity().
	 * None of the butterflies added by base Forestry have a biome restriction.
	 *
	 * @return Tag of biomes this butterfly is limited to spawning in, or null if this butterfly can spawn in any biome.
	 */
	@Nullable
	TagKey<Biome> getSpawnBiomes();

	/**
	 * @return Float between 0 and 1 representing the rarity of the species, will affect spawn rate.
	 */
	float getRarity();

	/**
	 * @return Float representing the distance below which this butterfly will take flight if it detects a player which is not sneaking.
	 */
	float getFlightDistance();

	/**
	 * @return {@code true} if this species is only active at night.
	 */
	boolean isNocturnal();

	/**
	 * @return {@code true} if this species is a Moth species instead of a Butterfly
	 */
	boolean isMoth();

	/**
	 * @return The loot that drops if you kill a butterfly.
	 */
	List<Product> getButterflyLoot();

	/**
	 * @return The loot that drops if you destroy a leaf that contains a caterpillar.
	 */
	List<Product> getCaterpillarProducts();

	/**
	 * @return The color of this butterfly's serum item. Also used for escritoire cells.
	 */
	int getSerumColor();

	@Override
	default int getEscritoireColor() {
		return getSerumColor();
	}
}
