package forestry.plugin;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.plugin.ISpeciesBuilder;
import forestry.api.plugin.ITreeSpeciesBuilder;
import forestry.arboriculture.TreeSpecies;
import forestry.arboriculture.worldgen.DefaultTreeGenerator;

public class TreeSpeciesBuilder extends SpeciesBuilder<ITreeSpeciesType, ITreeSpeciesBuilder> implements ITreeSpeciesBuilder {
	@Nullable
	private IWoodType woodType = null;
	@Nullable
	private ITreeGenerator generator = null;
	private float rarity = 1.0f;
	private boolean hasFruitLeaves = false;

	public TreeSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	protected ISpeciesFactory<ITreeSpeciesType, ITreeSpeciesBuilder> createSpeciesFactory() {
		return TreeSpecies::new;
	}

	@Override
	public ITreeSpeciesBuilder setWoodType(IWoodType woodType) {
		this.woodType = woodType;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setTreeFeature(Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory) {
		Preconditions.checkState(this.woodType != null, "Must call setWoodType before setTreeFeature");

		return setGenerator(new DefaultTreeGenerator(factory, this.woodType));
	}

	@Override
	public ITreeSpeciesBuilder setGenerator(ITreeGenerator generator) {
		this.generator = generator;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setHasFruitLeaves(boolean hasFruitLeaves) {
		this.hasFruitLeaves = hasFruitLeaves;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setRarity(float rarity) {
		this.rarity = rarity;
		return this;
	}
}
