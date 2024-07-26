/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.arboriculture.genetics;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeEffect;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.core.genetics.Individual;
import forestry.core.genetics.mutations.Mutation;
import forestry.core.utils.SpeciesUtil;

public class Tree extends Individual<ITreeSpecies, ITree, ITreeSpeciesType> implements ITree, IPlantable {
	public static final Codec<Tree> CODEC = RecordCodecBuilder.create(instance -> {
		Codec<IGenome> genomeCodec = SpeciesUtil.TREE_TYPE.get().getKaryotype().getGenomeCodec();

		return instance.group(
				genomeCodec.fieldOf("genome").forGetter(IIndividual::getGenome),
				genomeCodec.optionalFieldOf("mate", null).forGetter(IIndividual::getMate)
		).apply(instance, Tree::new);
	});

	public Tree(IGenome genome) {
		super(genome);
	}

	public Tree(IGenome genome, @Nullable IGenome mate) {
		super(genome);

		this.mate = mate;
	}

	/* EFFECTS */
	@Override
	public IEffectData[] doEffect(IEffectData[] storedData, Level level, BlockPos pos) {
		ITreeEffect effect = this.genome.getActiveValue(TreeChromosomes.EFFECT);

		storedData[0] = doEffect(effect, storedData[0], level, pos);

		// Return here if the primary can already not be combined
		if (!effect.isCombinable()) {
			return storedData;
		}

		ITreeEffect secondary = this.genome.getInactiveValue(TreeChromosomes.EFFECT);
		if (!secondary.isCombinable()) {
			return storedData;
		}

		storedData[1] = doEffect(secondary, storedData[1], level, pos);

		return storedData;
	}

	private IEffectData doEffect(ITreeEffect effect, IEffectData storedData, Level world, BlockPos pos) {
		storedData = effect.validateStorage(storedData);
		return effect.doEffect(getGenome(), storedData, world, pos);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IEffectData[] doFX(IEffectData[] storedData, Level level, BlockPos pos) {
		return storedData;
	}

	/* GROWTH */
	@Override
	public Feature<NoneFeatureConfiguration> getTreeGenerator(WorldGenLevel level, BlockPos pos, boolean wasBonemealed) {
		return genome.getActiveValue(TreeChromosomes.SPECIES).getGenerator().getTreeFeature(this);
	}

	@Override
	public boolean canStay(BlockGetter level, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState state = level.getBlockState(below);

		Block block = state.getBlock();
		return block.canSustainPlant(state, level, below, Direction.UP, this);
	}

	// IPlantable
	@Override
	public BlockState getPlant(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos);
	}

	// IPlantable
	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.PLAINS;
	}

	@Override
	@Nullable
	public BlockPos canGrow(LevelAccessor level, BlockPos pos, int expectedGirth, int expectedHeight) {
		return TreeGrowthHelper.canGrow(level, genome, pos, expectedGirth, expectedHeight);
	}

	@Override
	public int getRequiredMaturity() {
		return genome.getActiveValue(TreeChromosomes.MATURATION);
	}

	@Override
	public int getGirth() {
		return genome.getActiveValue(TreeChromosomes.GIRTH);
	}

	@Override
	public int getResilience() {
		int base = (int) (getGenome().getActiveValue(TreeChromosomes.SAPLINGS) * getGenome().getActiveValue(TreeChromosomes.SAPPINESS) * 100);
		return (Math.max(base, 1)) * 10;
	}

	@Override
	public float getHeightModifier() {
		return this.genome.getActiveValue(TreeChromosomes.HEIGHT);
	}

	@Override
	public boolean setLeaves(LevelAccessor level, @Nullable GameProfile owner, BlockPos pos, RandomSource rand) {
		return this.genome.getActiveValue(TreeChromosomes.SPECIES).getGenerator().setLeaves(genome, level, owner, pos, rand);
	}

	@Override
	public boolean setLogBlock(LevelAccessor level, BlockPos pos, Direction facing) {
		return this.genome.getActiveValue(TreeChromosomes.SPECIES).getGenerator().setLogBlock(genome, level, pos, facing);
	}

	@Override
	public boolean allowsFruitBlocks() {
		return this.genome.getActiveValue(TreeChromosomes.FRUITS).requiresFruitBlocks();
	}

	@Override
	public boolean trySpawnFruitBlock(LevelAccessor level, RandomSource rand, BlockPos pos) {
		IFruit provider = this.genome.getActiveValue(TreeChromosomes.FRUITS);
		//Collection<IFruitFamily> suitable = genome.getActiveAllele(TreeChromosomes.SPECIES).getSuitableFruit();
		return provider.trySpawnFruitBlock(this.genome, level, rand, pos);
	}

	@Override
	public void addTooltip(List<Component> list) {

		// No info 4 u!
		/*if (!isAnalyzed) {
			list.add(new StringTextComponent("<").appendSibling(new TranslationTextComponent("for.gui.unknown")).appendText(">"));
			return;
		}

		// You analyzed it? Juicy tooltip coming up!
		IAlleleTreeSpecies primary = genome.getActiveAllele(TreeChromosomes.SPECIES);
		IAlleleTreeSpecies secondary = genome.getInactiveAllele(TreeChromosomes.SPECIES);

		if (!isPureBred(TreeChromosomes.SPECIES)) {
			list.add(new TranslationTextComponent("for.trees.hybrid", primary.getDisplayName(), secondary.getDisplayName()).applyTextStyle(TextFormatting.BLUE));
		}

		ITextComponent sappiness = new TranslationTextComponent("S: %1$s" + genome.getActiveAllele(TreeChromosomes.SAPPINESS)).applyTextStyle(TextFormatting.GOLD);
		ITextComponent maturation = new TranslationTextComponent("M: %1$s" + genome.getActiveAllele(TreeChromosomes.MATURATION)).applyTextStyle(TextFormatting.RED);
		ITextComponent height = new TranslationTextComponent("H: %1$s" + genome.getActiveAllele(TreeChromosomes.HEIGHT)).applyTextStyle(TextFormatting.LIGHT_PURPLE);
		ITextComponent girth = new TranslationTextComponent("G: %1$sx%2$s", genome.getActiveAllele(TreeChromosomes.GIRTH).getDisplayName(), genome.getActiveAllele(TreeChromosomes.GIRTH).getDisplayName()).applyTextStyle(TextFormatting.AQUA);
		ITextComponent saplings = new TranslationTextComponent("S: %1$s" + genome.getActiveAllele(TreeChromosomes.FERTILITY)).applyTextStyle(TextFormatting.YELLOW);
		ITextComponent yield = new TranslationTextComponent("Y: %1$s" + genome.getActiveAllele(TreeChromosomes.YIELD)).applyTextStyle(TextFormatting.WHITE);
		list.add(new TranslationTextComponent("%1$s %2$s", saplings, maturation));
		list.add(new TranslationTextComponent("%1$s %2$s", height, girth));
		list.add(new TranslationTextComponent("%1$s %2$s", yield, sappiness));

		boolean primaryFireproof = genome.getActiveValue(TreeChromosomes.FIREPROOF);
		if (primaryFireproof) {
			list.add(new TranslationTextComponent("for.gui.fireresist").applyTextStyle(TextFormatting.RED));
		}

		IAllele fruit = getGenome().getActiveAllele(TreeChromosomes.FRUITS);
		if (fruit != AlleleFruits.fruitNone) {
			String strike = "";
			if (!canBearFruit()) {
				strike = TextFormatting.STRIKETHROUGH.toString();
			}
			list.add(new StringTextComponent(strike + TextFormatting.GREEN + "F: " + genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getDescription()));
		}*/
	}

	/* REPRODUCTION */
	@Override
	public List<ITree> getSaplings(Level level, @Nullable GameProfile playerProfile, BlockPos pos, float modifier) {
		List<ITree> prod = new ArrayList<>();

		float chance = genome.getActiveValue(TreeChromosomes.SAPLINGS) * modifier;

		if (level.random.nextFloat() <= chance) {
			if (mate == null) {
				prod.add(copy());
			} else {
				SpeciesUtil.ISpeciesMutator mutator = (p1, p2) -> mutateSpecies(level, playerProfile, pos, p1, p2);
				prod.add(SpeciesUtil.createOffspring(level.random, this.genome, this.mate, mutator, Tree::new));
			}
		}

		return prod;
	}

	@Nullable
	private static ImmutableList<AllelePair<?>> mutateSpecies(Level treeLevel, @Nullable GameProfile profile, BlockPos treePos, IGenome parent1, IGenome parent2) {
		return SpeciesUtil.mutateSpecies(treeLevel, treePos, profile, parent1, parent2, TreeChromosomes.SPECIES, Mutation::getChance);
	}

	/* PRODUCTION */
	@Override
	public boolean canBearFruit() {
		//species.getDefaultGenome().getActiveAllele(TreeChromosomes.FRUITS) != ForestryAlleles.FRUIT_NONE;
		return true;
	}

	@Override
	public List<Product> getProducts() {
		return this.genome.getActiveValue(TreeChromosomes.FRUITS).getProducts();
	}

	@Override
	public List<Product> getSpecialties() {
		return this.genome.getActiveValue(TreeChromosomes.FRUITS).getSpecialty();
	}

	@Override
	public List<ItemStack> produceStacks(Level level, BlockPos pos, int ripeningTime) {
		return this.genome.getActiveValue(TreeChromosomes.FRUITS).getFruits(this.genome, level, pos, ripeningTime);
	}

	@Override
	public ItemStack copyWithStage(ILifeStage stage) {
		return null;
	}
}
