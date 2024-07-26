package forestry.api.plugin;

import java.awt.Color;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.lepidopterology.genetics.IButterflySpeciesType;

/**
 * Builder used to register new butterfly and moth species and configure already existing ones.
 * Use {@link ILepidopterologyRegistration#registerSpecies} to obtain instances of this class.
 */
public interface IButterflySpeciesBuilder extends ISpeciesBuilder<IButterflySpeciesType, IButterflySpeciesBuilder> {
	/**
	 * Overrides the serum color set in {@link ILepidopterologyRegistration#registerSpecies}.
	 */
	IButterflySpeciesBuilder setSerumColor(Color color);

	IButterflySpeciesBuilder setFlightDistance(float flightDistance);

	/**
	 * Marks this butterfly as nocturnal, which makes the butterfly only spawn at night.
	 * Most, but not all, nocturnal butterflies are actually moths, so set {@link #setMoth} if this species is actually a moth.
	 */
	IButterflySpeciesBuilder setNocturnal(boolean nocturnal);

	/**
	 * Marks this butterfly species as a moth. The species name will show as "... Moth" instead of "... Butterfly".
	 * Most, but not all, moths are nocturnal, so {@link #setNocturnal} should probably be set too.
	 */
	IButterflySpeciesBuilder setMoth(boolean moth);

	IButterflySpeciesBuilder setSpawnBiomes(TagKey<Biome> biomeTag);

	/**
	 * Overrides the rarity set in {@link ILepidopterologyRegistration#registerSpecies}.
	 */
	IButterflySpeciesBuilder setRarity(float rarity);

	/**
	 * @param texturePath String texture path for this butterfly e.g. "forestry:butterfly/..."
	 */
	IButterflySpeciesBuilder setTexture(String texturePath);
}
