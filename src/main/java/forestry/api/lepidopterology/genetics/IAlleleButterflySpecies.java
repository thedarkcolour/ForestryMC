/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.client.ISpriteRegister;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.genetics.products.IDynamicProductList;

public interface IAlleleButterflySpecies extends IAlleleForestrySpecies, ISpriteRegister {

	/**
	 * @return the IButterflyRoot
	 */
	@Override
	IButterflySpeciesType getSpecies();

	/**
	 * @return Path of the texture to use for entity rendering.
	 */
	ResourceLocation getEntityTexture();

	/**
	 * @return Path of the texture to the item model.
	 */
	ResourceLocation getItemTexture();

	/**
	 * Allows butterflies to restrict random spawns beyond the restrictions set by temperature() and humidity().
	 *
	 * @return Biome tag this butterfly species can be spawned in.
	 */
	TagKey<Biome> getSpawnBiomes();

	boolean strictSpawnMatch();

	/**
	 * @return Float between 0 and 1 representing the rarity of the species, will affect spawn rate.
	 */
	float getRarity();

	/**
	 * @return Float representing the distance below which this butterfly will take flight if it detects a player which is not sneaking.
	 */
	float getFlightDistance();

	/**
	 * @return true if this species is only active at night.
	 */
	boolean isNocturnal();

	/**
	 * @return The loot that drops if you kill a butterfly.
	 */
	IDynamicProductList getButterflyLoot();

	/**
	 * @return The loot that drops if you destroy a leaf that contains a caterpillar.
	 */
	IDynamicProductList getCaterpillarLoot();
}
