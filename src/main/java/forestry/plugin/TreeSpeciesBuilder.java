package forestry.plugin;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.IWoodType;
import forestry.api.plugin.ITreeSpeciesBuilder;
import forestry.arboriculture.worldgen.DefaultTreeGenerator;

public class TreeSpeciesBuilder extends SpeciesBuilder<ITreeSpeciesBuilder> implements ITreeSpeciesBuilder {
	private IWoodType woodType;
	private int complexity = -1;

	public TreeSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	public ITreeSpeciesBuilder setWoodType(IWoodType woodType) {
		return null;
	}

	@Override
	public ITreeSpeciesBuilder setTreeFeature(Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory) {
		return setGenerator(new DefaultTreeGenerator(factory, this.woodType));
	}

	@Override
	public ITreeSpeciesBuilder setGenerator(ITreeGenerator generator) {
		return null;
	}

	@Override
	public ITreeSpeciesBuilder setHasFruitLeaves(boolean hasFruitLeaves) {
		return null;
	}

	@Override
	public ITreeSpeciesBuilder setRarity(float rarity) {
		return null;
	}
}
