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
package forestry.lepidopterology.genetics;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.IForestryApi;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.core.IError;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.products.IProductList;
import forestry.api.genetics.products.Product;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.IEntityButterfly;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflyMutation;
import forestry.api.core.ForestryError;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.core.genetics.GenericRatings;
import forestry.core.genetics.IndividualLiving;
import forestry.lepidopterology.ModuleLepidopterology;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.IGenome;
import genetics.api.mutation.IMutationContainer;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.components.ComponentKeys;
import genetics.individual.Genome;
import org.jetbrains.annotations.NotNull;

public class Butterfly extends IndividualLiving implements IButterfly {
	private static final RandomSource rand = RandomSource.create();

	/* CONSTRUCTOR */
	public Butterfly(CompoundTag nbt) {
		super(nbt);
	}

	public Butterfly(IGenome genome) {
		super(genome, null, genome.getActiveValue(ButterflyChromosomes.LIFESPAN));
	}

	public Butterfly(IGenome genome, @Nullable IGenome mate) {
		super(genome, mate);
	}

	@Override
	public ISpeciesType getRoot() {
		return ButterflyHelper.getRoot();
	}

	@Override
	public void addTooltip(List<Component> list) {
		ToolTip toolTip = new ToolTip();

		IButterflySpeciesType primary = genome.getActiveValue(ButterflyChromosomes.SPECIES);
		IButterflySpeciesType secondary = genome.getActiveValue(ButterflyChromosomes.SPECIES);
		if (!isPureBred(ButterflyChromosomes.SPECIES)) {
			toolTip.translated("for.butterflies.hybrid", primary.getDisplayName(), secondary.getDisplayName()).style(ChatFormatting.BLUE);
		}

		if (getMate() != null) {
			//TODO ITextComponent.toUpperCase(Locale.ENGLISH));
			toolTip.translated("for.gui.fecundated").style(ChatFormatting.RED);
		}
		toolTip.add(genome.getActiveAllele(ButterflyChromosomes.SIZE).getDisplayName().withStyle(ChatFormatting.YELLOW));
		toolTip.add(genome.getActiveAllele(ButterflyChromosomes.SPEED).getDisplayName().withStyle(ChatFormatting.DARK_GREEN));
		toolTip.singleLine().add(genome.getActiveAllele(ButterflyChromosomes.LIFESPAN).getDisplayName()).text(" ").translated("for.gui.life").style(ChatFormatting.GRAY).create();

		IValueAllele<ToleranceType> tempToleranceAllele = getGenome().getActiveAllele(ButterflyChromosomes.TEMPERATURE_TOLERANCE);
		IValueAllele<ToleranceType> humidToleranceAllele = getGenome().getActiveAllele(ButterflyChromosomes.HUMIDITY_TOLERANCE);

		toolTip.singleLine().text("T: ").add(ClimateHelper.toDisplay(primary.getTemperature())).text(" / ").add(tempToleranceAllele.getDisplayName()).style(ChatFormatting.GREEN).create();
		toolTip.singleLine().text("H: ").add(ClimateHelper.toDisplay(primary.getHumidity())).text(" / ").add(humidToleranceAllele.getDisplayName()).style(ChatFormatting.GREEN).create();

		toolTip.add(GenericRatings.rateActivityTime(genome.getActiveValue(ButterflyChromosomes.NEVER_SLEEPS), primary.isNocturnal()).withStyle(ChatFormatting.RED));

		if (genome.getActiveValue(ButterflyChromosomes.TOLERATES_RAIN)) {
			toolTip.translated("for.gui.flyer.tooltip").style(ChatFormatting.WHITE);
		}

		list.addAll(toolTip.getLines());
	}

	@Override
	public IButterfly copy() {
		CompoundTag compoundNBT = new CompoundTag();
		this.write(compoundNBT);
		return new Butterfly(compoundNBT);
	}

	@Override
	public Component getDisplayName() {
		return genome.getPrimarySpecies().getDisplayName();
	}

	@Override
	public Set<IError> getCanSpawn(IButterflyNursery nursery, @Nullable IButterflyCocoon cocoon) {
		Level world = nursery.getWorldObj();

		Set<IError> errorStates = new HashSet<>();
		// / Night or darkness requires nocturnal species
		boolean isDaytime = world.isDay();
		if (!isActiveThisTime(isDaytime)) {
			if (isDaytime) {
				errorStates.add(ForestryError.NOT_NIGHT);
			} else {
				errorStates.add(ForestryError.NOT_DAY);
			}
		}

		return addClimateErrors(nursery, errorStates);
	}

	@Override
	public Set<IError> getCanGrow(IButterflyNursery nursery, @Nullable IButterflyCocoon cocoon) {
		Set<IError> errorStates = new HashSet<>();

		return addClimateErrors(nursery, errorStates);
	}

	@NotNull
	private Set<IError> addClimateErrors(IButterflyNursery nursery, Set<IError> errorStates) {
		IAlleleButterflySpecies species = genome.getActiveAllele(ButterflyChromosomes.SPECIES);
		TemperatureType actualTemperature = nursery.temperature();
		TemperatureType baseTemperature = species.getTemperature();
		ToleranceType toleranceTemperature = genome.getActiveValue(ButterflyChromosomes.TEMPERATURE_TOLERANCE);
		HumidityType actualHumidity = nursery.humidity();
		HumidityType baseHumidity = species.getHumidity();
		ToleranceType toleranceHumidity = genome.getActiveValue(ButterflyChromosomes.HUMIDITY_TOLERANCE);
		ClimateHelper.addClimateErrorStates(actualTemperature, actualHumidity, baseTemperature, toleranceTemperature, baseHumidity, toleranceHumidity, errorStates);

		return errorStates;
	}

	@Override
	public boolean canSpawn(Level level, double x, double y, double z) {
		if (!canFly(level)) {
			return false;
		}

		BlockPos pos = new BlockPos(x, y, z);
		Holder<Biome> biome = level.getBiome(pos);
		IAlleleButterflySpecies species = getGenome().getActiveAllele(ButterflyChromosomes.SPECIES);
		return biome.is(species.getSpawnBiomes()) && isAcceptedEnvironment(level, pos);
	}

	@Override
	public boolean canTakeFlight(Level level, double x, double y, double z) {
		return canFly(level) && isAcceptedEnvironment(level, x, y, z);
	}

	private boolean canFly(Level world) {
		return (!world.isRaining() || getGenome().getActiveValue(ButterflyChromosomes.TOLERATES_RAIN)) && isActiveThisTime(world.isDay());
	}

	@Override
	public boolean isAcceptedEnvironment(Level world, BlockPos pos) {
		Holder<Biome> biome = world.getBiome(pos);
		TemperatureType biomeTemperature = IForestryApi.INSTANCE.getClimateManager().getTemperature(biome);
		HumidityType biomeHumidity = IForestryApi.INSTANCE.getClimateManager().getHumidity(biome);
		return ClimateHelper.isWithinLimits(biomeTemperature, biomeHumidity,
				getGenome().getActiveAllele(ButterflyChromosomes.SPECIES).getTemperature(), getGenome().getActiveValue(ButterflyChromosomes.TEMPERATURE_TOLERANCE),
				getGenome().getActiveAllele(ButterflyChromosomes.SPECIES).getHumidity(), getGenome().getActiveValue(ButterflyChromosomes.HUMIDITY_TOLERANCE));
	}

	@Override
	@Nullable
	public IButterfly spawnCaterpillar(Level level, IButterflyNursery nursery) {
		// We need a mated queen to produce offspring.
		if (mate == null) {
			return null;
		}

		ChromosomePair[] chromosomes = new ChromosomePair[genome.getChromosomes().length];
		ChromosomePair[] parent1 = genome.getChromosomes();
		ChromosomePair[] parent2 = mate.getChromosomes();

		// Check for mutation. Replace one of the parents with the mutation
		// template if mutation occured.
		ChromosomePair[] mutated1 = mutateSpecies(level, nursery, genome, mate);
		if (mutated1 != null) {
			parent1 = mutated1;
		}
		ChromosomePair[] mutated2 = mutateSpecies(level, nursery, mate, genome);
		if (mutated2 != null) {
			parent2 = mutated2;
		}

		for (int i = 0; i < parent1.length; i++) {
			if (parent1[i] != null && parent2[i] != null) {
				chromosomes[i] = parent1[i].inheritChromosome(rand, parent2[i]);
			}
		}

		return new Butterfly(new Genome(ButterflyHelper.getRoot().getKaryotype(), chromosomes));
	}

	@Nullable
	private static ChromosomePair[] mutateSpecies(Level world, IButterflyNursery nursery, IGenome genomeOne, IGenome genomeTwo) {

		ChromosomePair[] parent1 = genomeOne.getChromosomes();
		ChromosomePair[] parent2 = genomeTwo.getChromosomes();

		IGenome genome0;
		IGenome genome1;
		IAllele allele0;
		IAllele allele1;

		if (rand.nextBoolean()) {
			allele0 = parent1[(ButterflyChromosomes.SPECIES).ordinal()].active();
			allele1 = parent2[(ButterflyChromosomes.SPECIES).ordinal()].inactive();

			genome0 = genomeOne;
			genome1 = genomeTwo;
		} else {
			allele0 = parent2[(ButterflyChromosomes.SPECIES).ordinal()].active();
			allele1 = parent1[(ButterflyChromosomes.SPECIES).ordinal()].inactive();

			genome0 = genomeTwo;
			genome1 = genomeOne;
		}

		IMutationContainer<IButterfly, IButterflyMutation> container = ButterflyHelper.getRoot().getComponent(ComponentKeys.MUTATIONS);
		for (IButterflyMutation mutation : container.getMutations(true)) {
			float chance = mutation.getChance(world, nursery, allele0, allele1, genome0, genome1);
			if (chance > rand.nextFloat() * 100) {
				return ButterflyManager.butterflyRoot.getKaryotype().templateAsChromosomes(mutation.getTemplate());
			}
		}

		return null;
	}

	private boolean isActiveThisTime(boolean isDayTime) {
		if (getGenome().getActiveValue(ButterflyChromosomes.NEVER_SLEEPS)) {
			return true;
		}

		return isDayTime != getGenome().getActiveAllele(ButterflyChromosomes.SPECIES).isNocturnal();
	}

	@Override
	public float getSize() {
		return getGenome().getActiveValue(ButterflyChromosomes.SIZE);
	}

	@Override
	public NonNullList<ItemStack> getLootDrop(IEntityButterfly entity, boolean playerKill, int lootLevel) {
		NonNullList<ItemStack> drop = NonNullList.create();

		PathfinderMob creature = entity.getEntity();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		IProductList products = getGenome().getActiveAllele(ButterflyChromosomes.SPECIES).getButterflyLoot();

		for (Product product : products.getPossibleProducts()) {
			if (creature.level.random.nextFloat() < product.getChance() * metabolism) {
				drop.add(product.copyStack());
			}
		}

		return drop;
	}

	@Override
	public NonNullList<ItemStack> getCaterpillarDrop(IButterflyNursery nursery, boolean playerKill, int lootLevel) {
		NonNullList<ItemStack> drop = NonNullList.create();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		IProductList products = getGenome().getActiveAllele(ButterflyChromosomes.SPECIES).getCaterpillarLoot();
		for (Product product : products.getPossibleProducts()) {
			if (rand.nextFloat() < product.getChance() * metabolism) {
				drop.add(product.copyStack());
			}
		}

		return drop;
	}

	@Override
	public NonNullList<ItemStack> getCocoonDrop(IButterflyCocoon cocoon) {
		NonNullList<ItemStack> drop = NonNullList.create();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		IProductList products = getGenome().getActiveAllele(ButterflyChromosomes.COCOON).getCocoonLoot();

		for (Product product : products.getPossibleProducts()) {
			if (rand.nextFloat() < product.getChance() * metabolism) {
				drop.add(product.copyStack());
			}
		}

		if (ModuleLepidopterology.getSerumChance() > 0) {
			if (rand.nextFloat() < ModuleLepidopterology.getSerumChance() * metabolism) {
				ItemStack stack = ButterflyManager.butterflyRoot.getTypes().createStack(this, ButterflyLifeStage.SERUM);
				if (ModuleLepidopterology.getSecondSerumChance() > 0) {
					if (rand.nextFloat() < ModuleLepidopterology.getSecondSerumChance() * metabolism) {
						stack.setCount(2);
					}
				}
				drop.add(ButterflyManager.butterflyRoot.getTypes().createStack(this, ButterflyLifeStage.SERUM));
			}
		}

		if (cocoon.isSolid()) {
			drop.add(ButterflyManager.butterflyRoot.getTypes().createStack(this, ButterflyLifeStage.BUTTERFLY));
		}

		return drop;
	}

}
