package forestry.api.plugin;

import java.util.function.Function;

import net.minecraft.world.level.levelgen.feature.Feature;

import forestry.api.arboriculture.ITreeGenData;

public interface ITreeSpeciesBuilder extends ISpeciesBuilder<ITreeSpeciesBuilder> {
	/**
	 * Overrides the tree feature set in {@link IArboricultureRegistration#registerSpecies}.
	 */
	ITreeSpeciesBuilder setTreeFeature(Function<ITreeGenData, Feature<?>> factory);

	// todo not sure if this is needed
	ITreeSpeciesBuilder setHasFruitLeaves(boolean hasFruitLeaves);

	/**
	 * Sets the rarity for this tree to generate during world generation.
	 *
	 * @param rarity A float between 0 and 1 that determines how often this tree spawns naturally.
	 */
	ITreeSpeciesBuilder setRarity(float rarity);
}
