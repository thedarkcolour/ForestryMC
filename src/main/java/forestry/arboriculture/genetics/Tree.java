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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import forestry.api.arboriculture.IArboristTracker;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.ILeafEffect;
import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeMutation;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.genetics.products.IProductList;
import forestry.core.config.Config;

import forestry.api.genetics.alleles.AllelePair;
import genetics.api.individual.Individual;
import genetics.api.mutation.IMutationContainer;
import genetics.api.root.components.ComponentKeys;
import forestry.core.genetics.Genome;
import forestry.core.genetics.Individual;

public class Tree extends Individual implements ITree, IPlantable {
	public Tree(IGenome genome) {
		super(genome);
	}

	public Tree(CompoundTag compoundNbt) {
		super(compoundNbt);
	}

	@Override
	public ITreeSpeciesType getRoot() {
		return TreeManager.treeRoot;
	}

	/* EFFECTS */
	@Override
	public IEffectData[] doEffect(IEffectData[] storedData, Level world, BlockPos pos) {
		ILeafEffect effect = this.genome.getActiveValue(TreeChromosomes.EFFECT);

		storedData[0] = doEffect(effect, storedData[0], world, pos);

		// Return here if the primary can already not be combined
		if (!effect.isCombinable()) {
			return storedData;
		}

		ILeafEffect secondary = this.genome.getInactiveValue(TreeChromosomes.EFFECT);
		if (!secondary.isCombinable()) {
			return storedData;
		}

		storedData[1] = doEffect(secondary, storedData[1], world, pos);

		return storedData;
	}

	private IEffectData doEffect(ILeafEffect effect, IEffectData storedData, Level world, BlockPos pos) {
		storedData = effect.validateStorage(storedData);
		return effect.doEffect(getGenome(), storedData, world, pos);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IEffectData[] doFX(IEffectData[] storedData, Level world, BlockPos pos) {
		return storedData;
	}

	/* GROWTH */
	@Override
	public Feature<NoneFeatureConfiguration> getTreeGenerator(WorldGenLevel world, BlockPos pos, boolean wasBonemealed) {
		return genome.getActiveValue(TreeChromosomes.SPECIES).getGenerator().getTreeFeature(this);
	}

	@Override
	public boolean canStay(BlockGetter world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = world.getBlockState(blockPos);

		Block block = blockState.getBlock();
		return block.canSustainPlant(blockState, world, blockPos, Direction.UP, this);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return genome.getActiveAllele(TreeChromosomes.SPECIES).getPlantType();
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	@Nullable
	public BlockPos canGrow(LevelAccessor world, BlockPos pos, int expectedGirth, int expectedHeight) {
		return TreeGrowthHelper.canGrow(world, genome, pos, expectedGirth, expectedHeight);
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
		return genome.getActiveValue(TreeChromosomes.HEIGHT);
	}

	@Override
	public boolean setLeaves(LevelAccessor world, @Nullable GameProfile owner, BlockPos pos, RandomSource rand) {
		return genome.getActiveAllele(TreeChromosomes.SPECIES).getGenerator().setLeaves(genome, world, owner, pos, rand);
	}

	@Override
	public boolean setLogBlock(LevelAccessor world, BlockPos pos, Direction facing) {
		return genome.getActiveAllele(TreeChromosomes.SPECIES).getGenerator().setLogBlock(genome, world, pos, facing);
	}

	@Override
	public boolean allowsFruitBlocks() {
		IFruit provider = getGenome().getActiveAllele(TreeChromosomes.FRUITS).getProvider();
		if (!provider.requiresFruitBlocks()) {
			return false;
		}

		Collection<IFruitFamily> suitable = genome.getActiveAllele(TreeChromosomes.SPECIES).getSuitableFruit();
		return suitable.contains(provider.getFamily());
	}

	@Override
	public boolean trySpawnFruitBlock(LevelAccessor world, RandomSource rand, BlockPos pos) {
		IFruit provider = getGenome().getActiveAllele(TreeChromosomes.FRUITS).getProvider();
		Collection<IFruitFamily> suitable = genome.getActiveAllele(TreeChromosomes.SPECIES).getSuitableFruit();
		return suitable.contains(provider.getFamily()) &&
				provider.trySpawnFruitBlock(getGenome(), world, rand, pos);
	}

	@Override
	public ITree copy() {
		CompoundTag compound = new CompoundTag();
		this.write(compound);
		return new Tree(compound);
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
	public List<ITree> getSaplings(Level world, @Nullable GameProfile playerProfile, BlockPos pos, float modifier) {
		List<ITree> prod = new ArrayList<>();

		float chance = genome.getActiveValue(TreeChromosomes.SAPLINGS) * modifier;

		if (world.random.nextFloat() <= chance) {
			if (mate == null) {
				prod.add(TreeManager.treeRoot.getTree(world, new Genome(TreeManager.treeRoot.getKaryotype(), genome.getAllelePairs())));
			} else {
				prod.add(createOffspring(world, mate, playerProfile, pos));
			}
		}

		return prod;
	}

	private ITree createOffspring(Level world, IGenome mate, @Nullable GameProfile playerProfile, BlockPos pos) {
		AllelePair[] chromosomes = new AllelePair[genome.getAllelePairs().length];
		AllelePair[] parent1 = genome.getAllelePairs();
		AllelePair[] parent2 = mate.getAllelePairs();

		// Check for mutation. Replace one of the parents with the mutation
		// template if mutation occured.
		AllelePair[] mutated = mutateSpecies(world, playerProfile, pos, genome, mate);
		if (mutated == null) {
			mutated = mutateSpecies(world, playerProfile, pos, mate, genome);
		}

		if (mutated != null) {
			return new Tree(new Genome(TreeManager.treeRoot.getKaryotype(), mutated));
		}

		for (int i = 0; i < parent1.length; i++) {
			if (parent1[i] != null && parent2[i] != null) {
				chromosomes[i] = parent1[i].inheritChromosome(world.random, parent2[i]);
			}
		}

		return new Tree(new Genome(TreeManager.treeRoot.getKaryotype(), chromosomes));
	}

	@Nullable
	private static AllelePair[] mutateSpecies(Level world, @Nullable GameProfile playerProfile, BlockPos pos, IGenome genomeOne, IGenome genomeTwo) {
		AllelePair[] parent1 = genomeOne.getAllelePairs();
		AllelePair[] parent2 = genomeTwo.getAllelePairs();

		IGenome genome0;
		IGenome genome1;
		IAlleleTreeSpecies allele0;
		IAlleleTreeSpecies allele1;

		if (world.random.nextBoolean()) {
			allele0 = (IAlleleTreeSpecies) parent1[TreeChromosomes.SPECIES.ordinal()].active();
			allele1 = (IAlleleTreeSpecies) parent2[TreeChromosomes.SPECIES.ordinal()].inactive();

			genome0 = genomeOne;
			genome1 = genomeTwo;
		} else {
			allele0 = (IAlleleTreeSpecies) parent2[TreeChromosomes.SPECIES.ordinal()].active();
			allele1 = (IAlleleTreeSpecies) parent1[TreeChromosomes.SPECIES.ordinal()].inactive();

			genome0 = genomeTwo;
			genome1 = genomeOne;
		}

		IArboristTracker breedingTracker = null;
		if (playerProfile != null) {
			breedingTracker = TreeManager.treeRoot.getBreedingTracker(world, playerProfile);
		}

		IMutationContainer<ITree, ? extends IMutation> container = TreeManager.treeRoot.getComponent(ComponentKeys.MUTATIONS);
		List<? extends IMutation> combinations = container.getCombinations(allele0, allele1, true);
		for (IMutation mutation : combinations) {
			ITreeMutation treeMutation = (ITreeMutation) mutation;
			// Stop blacklisted species.
			// if (BeeManager.breedingManager.isBlacklisted(mutation.getTemplate()[0].getIdString())) {
			// continue;
			// }

			float chance = treeMutation.getChance(world, pos, allele0, allele1, genome0, genome1);
			if (chance <= 0) {
				continue;
			}

			// boost chance for researched mutations
			if (breedingTracker != null && breedingTracker.isResearched(treeMutation)) {
				float mutationBoost = chance * (Config.researchMutationBoostMultiplier - 1.0f);
				mutationBoost = Math.min(Config.maxResearchMutationBoostPercent, mutationBoost);
				chance += mutationBoost;
			}

			if (chance > world.random.nextFloat() * 100) {
				return TreeManager.treeRoot.getKaryotype().templateAsChromosomes(treeMutation.getTemplate());
			}
		}

		return null;
	}

	/* PRODUCTION */
	@Override
	public boolean canBearFruit() {
		return genome.getActiveAllele(TreeChromosomes.SPECIES).getSuitableFruit().contains(genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getFamily());
	}

	@Override
	public IProductList getProducts() {
		return genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getProducts();
	}

	@Override
	public IProductList getSpecialties() {
		return genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getSpecialty();
	}

	@Override
	public NonNullList<ItemStack> produceStacks(Level world, BlockPos pos, int ripeningTime) {
		return genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getFruits(genome, world, pos, ripeningTime);
	}
}
