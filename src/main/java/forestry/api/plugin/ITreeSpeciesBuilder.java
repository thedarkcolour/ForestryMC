package forestry.api.plugin;

import java.util.function.Function;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.arboriculture.worldgen.DefaultTreeGenerator;

public interface ITreeSpeciesBuilder extends ISpeciesBuilder<ITreeSpeciesType, ITreeSpeciesBuilder> {
	/**
	 * Shortcut to create a tree generator using the tree generator built into Forestry.
	 */
	ITreeSpeciesBuilder setTreeFeature(Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory);

	ITreeSpeciesBuilder setGenerator(ITreeGenerator generator);

	// todo not sure if this is needed
	ITreeSpeciesBuilder setHasFruitLeaves(boolean hasFruitLeaves);

	/**
	 * Overrides the wood type set in {@link IArboricultureRegistration#registerSpecies}.
	 */
	ITreeSpeciesBuilder setWoodType(IWoodType woodType);

	/**
	 * Sets the rarity for this tree to generate during world generation.
	 *
	 * @param rarity A float between 0 and 1 that determines how often this tree spawns naturally.
	 */
	ITreeSpeciesBuilder setRarity(float rarity);

	ITreeGenerator getGenerator();
}
