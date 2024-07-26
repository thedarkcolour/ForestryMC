package forestry.arboriculture;

import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.ITreeSpeciesBuilder;
import forestry.arboriculture.genetics.Tree;
import forestry.core.genetics.Species;

public class TreeSpecies extends Species<ITreeSpeciesType, ITree> implements ITreeSpecies {
	private final TemperatureType temperature;
	private final HumidityType humidity;
	private final ITreeGenerator generator;

	public TreeSpecies(ResourceLocation id, ITreeSpeciesType speciesType, IGenome defaultGenome, ITreeSpeciesBuilder builder) {
		super(id, speciesType, defaultGenome, builder);

		this.temperature = builder.getTemperature();
		this.humidity = builder.getHumidity();
		this.generator = builder.getGenerator();
	}

	@Override
	public ITreeGenerator getGenerator() {
		return this.generator;
	}

	@Override
	public ILeafSpriteProvider getLeafSpriteProvider() {
		return null;
	}

	@Override
	public ItemStack getDecorativeLeaves() {
		return null;
	}

	@Override
	public TemperatureType getTemperature() {
		return this.temperature;
	}

	@Override
	public HumidityType getHumidity() {
		return this.humidity;
	}

	@Override
	public List<BlockState> getVanillaStates() {
		return null;
	}

	@Override
	public int getGermlingColour(TreeLifeStage stage, int renderPass) {
		return 0;
	}

	@Override
	public ITree createIndividual(IGenome genome) {
		return new Tree(genome);
	}
}
