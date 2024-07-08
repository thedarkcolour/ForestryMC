/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import deleteme.BiomeCategory;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.core.config.Constants;
import forestry.core.data.ForestryTags;

/**
 * Many things Forestry use temperature and humidity of a biome to determine whether they can or how they can work or spawn at a given location.
 * <p>
 * This enum concerns temperature.
 */
public enum EnumTemperature {
	ICY(ForestryTags.Biomes.ICY_TEMPERATURE, "habitats/snow", 0xaafff0),
	COLD(ForestryTags.Biomes.COLD_TEMPERATURE, "habitats/taiga", 0x72ddf7),
	NORMAL(ForestryTags.Biomes.NORMAL_TEMPERATURE, "habitats/plains", 0xffd013),
	WARM(ForestryTags.Biomes.WARM_TEMPERATURE, "habitats/jungle", 0xfb8a24),
	HOT(ForestryTags.Biomes.HOT_TEMPERATURE, "habitats/desert", 0xd61439),
	HELLISH(ForestryTags.Biomes.HELLISH_TEMPERATURE, "habitats/nether", 0x81032d);

	public static EnumTemperature[] VALUES = values();

	public final TagKey<Biome> tag;
	public final ResourceLocation iconIndex;
	public final int color;

	EnumTemperature(TagKey<Biome> tag, String iconTexture, int color) {
		this.tag = tag;
		this.iconIndex = new ResourceLocation(Constants.MOD_ID, iconTexture);
		this.color = color;
	}

	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite getSprite() {
		return ForestryAPI.textureManager.getDefault(iconIndex);
	}

	/**
	 * Determines the EnumTemperature given a floating point representation of
	 * Minecraft temperature. Hellish biomes are handled based on their biome
	 * type - check BiomeHelper.isBiomeHellish.
	 *
	 * @param rawTemp raw temperature value
	 * @return EnumTemperature corresponding to value of rawTemp
	 */
	public static EnumTemperature getFromValue(float rawTemp) {
		if (rawTemp > 1.00f) {
			return HOT;
		} else if (rawTemp > 0.85f) {
			return WARM;
		} else if (rawTemp > 0.35f) {
			return NORMAL;
		} else if (rawTemp > 0.0f) {
			return COLD;
		} else {
			return ICY;
		}
	}

	public static EnumTemperature getFromBiome(Biome biome) {
		if (BiomeCategory.NETHER.is(biome)) {
			return HELLISH;
		}

		return getFromValue(biome.getBaseTemperature());
	}

	public static EnumTemperature getFromBiome(Biome biome, BlockPos pos) {
		if (BiomeCategory.NETHER.is(biome)) {
			return HELLISH;
		}

		return getFromValue(biome.getBaseTemperature());
	}
}
