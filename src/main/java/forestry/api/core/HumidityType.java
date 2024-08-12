/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import java.util.List;

import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import forestry.api.ForestryTags;

/**
 * Many things Forestry use temperature and humidity of a biome to determine whether they can or how they can work or spawn at a given location.
 * <p>
 * This enum concerns humidity.
 */
public enum HumidityType {
	ARID(ForestryTags.Biomes.ARID_HUMIDITY, 0xaad0db),
	NORMAL(ForestryTags.Biomes.NORMAL_HUMIDITY, 0x4b7bff),
	DAMP(ForestryTags.Biomes.DAMP_HUMIDITY, 0x6e56b3);

	public static final List<HumidityType> VALUES = List.of(values());

	public final TagKey<Biome> tag;
	public final int color;

	HumidityType(TagKey<Biome> tag, int color) {
		this.tag = tag;
		this.color = color;
	}

	/**
	 * @return The humidity one tolerance step above, going no higher than {@link #DAMP}.
	 */
	public HumidityType up() {
		return up(1);
	}

	/**
	 * @return The humidity for any number of tolerance steps above, going no higher than {@link #DAMP}.
	 */
	public HumidityType up(int steps) {
		return VALUES.get(Mth.clamp(ordinal() + steps, 0, 2));
	}

	/**
	 * @return The humidity one tolerance step below, going no lower than {@link #ARID}.
	 */
	public HumidityType down() {
		return down(1);
	}

	/**
	 * @return The humidity for any number of tolerance step below, going no lower than {@link #ARID}.
	 */
	public HumidityType down(int steps) {
		return VALUES.get(Mth.clamp(ordinal() - steps, 0, 2));
	}

	/**
	 * @return If this humidity is wetter than or equal to another humidity.
	 */
	public boolean isWetterOrEqual(HumidityType other) {
		return ordinal() >= other.ordinal();
	}

	/**
	 * @return If this humidity is drier than or equal to another humidity.
	 */
	public boolean isDrierOrEqual(HumidityType other) {
		return ordinal() <= other.ordinal();
	}

	/**
	 * @return The humidity type corresponding to a biome's downfall value.
	 * @see forestry.api.climate.IClimateManager#getHumidity if you know the biome.
	 */
	public static HumidityType getFromValue(float downfall) {
		if (downfall > 0.85f) {
			return DAMP;
		} else if (downfall >= 0.3f) {
			return NORMAL;
		} else {
			return ARID;
		}
	}
}
