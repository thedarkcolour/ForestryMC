package forestry.arboriculture;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.authlib.GameProfile;

import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.plugin.ITreeSpeciesBuilder;
import forestry.arboriculture.genetics.Tree;
import forestry.arboriculture.genetics.TreeGrowthHelper;
import forestry.core.genetics.Species;

import org.jetbrains.annotations.Nullable;

public class TreeSpecies extends Species<ITreeSpeciesType, ITree> implements ITreeSpecies {
	private final TemperatureType temperature;
	private final HumidityType humidity;
	private final ITreeGenerator generator;
	private final List<BlockState> vanillaLeafStates;
	private final ItemStack decorativeLeaves;

	public TreeSpecies(ResourceLocation id, ITreeSpeciesType speciesType, IGenome defaultGenome, ITreeSpeciesBuilder builder) {
		super(id, speciesType, defaultGenome, builder);

		this.temperature = builder.getTemperature();
		this.humidity = builder.getHumidity();
		this.generator = builder.getGenerator();
		this.vanillaLeafStates = builder.getVanillaLeafStates();
		this.decorativeLeaves = builder.getDecorativeLeaves();
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
		return this.decorativeLeaves;
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
	public List<BlockState> getVanillaLeafStates() {
		return this.vanillaLeafStates;
	}

	@Override
	public int getGermlingColor(ILifeStage stage, int renderPass) {
		return stage == TreeLifeStage.POLLEN ? getLeafSpriteProvider().getColor(false) : 0xffffff;
	}

	@Override
	public ITree createIndividual(IGenome genome) {
		return new Tree(genome);
	}

	@Override
	public int getEscritoireColor() {
		return getLeafSpriteProvider().getColor(false);
	}

	@Override
	public float getHeightModifier(IGenome genome) {
		return genome.getActiveValue(TreeChromosomes.HEIGHT);
	}

	@Override
	public void addTooltip(ITree individual, List<Component> tooltip) {
		// No info 4 u!
		if (!individual.isAnalyzed()) {
			addUnknownGenomeTooltip(tooltip);
			return;
		}

		IGenome genome = individual.getGenome();

		// You analyzed it? Juicy tooltip coming up!
		addHybridTooltip(tooltip, genome, TreeChromosomes.SPECIES, "for.trees.hybrid");

		Component saplingsAndMaturation = Component.literal("S: ").append(genome.getActiveName(TreeChromosomes.SAPLINGS)).append(", ").withStyle(ChatFormatting.YELLOW)
				.append(Component.literal("M: ").append(genome.getActiveName(TreeChromosomes.MATURATION)).withStyle(ChatFormatting.RED));
		Component heightAndGirth = Component.literal("H: ").append(genome.getActiveName(TreeChromosomes.HEIGHT)).append(", ").withStyle(ChatFormatting.LIGHT_PURPLE)
				.append(Component.literal("G: ").append(genome.getActiveName(TreeChromosomes.GIRTH)).withStyle(ChatFormatting.AQUA));
		Component yieldAndSappiness = Component.literal("Y: ").append(genome.getActiveName(TreeChromosomes.YIELD)).append(", ").withStyle(ChatFormatting.WHITE)
				.append(Component.literal("S: ").append(genome.getActiveName(TreeChromosomes.SAPPINESS)).withStyle(ChatFormatting.GOLD));
		tooltip.add(saplingsAndMaturation);
		tooltip.add(heightAndGirth);
		tooltip.add(yieldAndSappiness);

		if (genome.getActiveValue(TreeChromosomes.FIREPROOF)) {
			tooltip.add(Component.translatable("for.gui.fireresist").withStyle(ChatFormatting.RED));
		}

		if (genome.getActiveAllele(TreeChromosomes.FRUITS) != ForestryAlleles.FRUIT_NONE) {
			tooltip.add(Component.literal("F: ").append(genome.getActiveName(TreeChromosomes.FRUITS)).withStyle(ChatFormatting.GREEN));
		}
	}

	@Nullable
	@Override
	public BlockPos getGrowthPos(IGenome genome, LevelAccessor level, BlockPos pos, int expectedGirth, int expectedHeight) {
		return TreeGrowthHelper.getGrowthPos(level, genome, pos, expectedGirth, expectedHeight);
	}

	@Override
	public int getGirth(IGenome genome) {
		return genome.getActiveValue(TreeChromosomes.GIRTH);
	}

	@Override
	public boolean setLeaves(IGenome genome, LevelAccessor level, @Nullable GameProfile owner, BlockPos pos, RandomSource random) {
		return getGenerator().setLeaves(genome, level, owner, pos, random);
	}

	@Override
	public boolean setLogBlock(IGenome genome, LevelAccessor level, BlockPos pos, Direction facing) {
		return genome.getActiveValue(TreeChromosomes.SPECIES).getGenerator().setLogBlock(genome, level, pos, facing);
	}

	@Override
	public boolean allowsFruitBlocks(IGenome genome) {
		return genome.getActiveValue(TreeChromosomes.FRUITS).requiresFruitBlocks();
	}

	@Override
	public boolean trySpawnFruitBlock(LevelAccessor level, RandomSource rand, BlockPos pos) {
		return this.defaultGenome.getActiveValue(TreeChromosomes.FRUITS).trySpawnFruitBlock(this.defaultGenome, level, rand, pos);
	}
}
