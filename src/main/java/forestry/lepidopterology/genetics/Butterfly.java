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

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import forestry.api.IForestryApi;
import forestry.api.core.ForestryError;
import forestry.api.core.HumidityType;
import forestry.api.core.IError;
import forestry.api.core.TemperatureType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.core.Product;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.IEntityButterfly;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.core.genetics.IndividualLiving;
import forestry.core.genetics.mutations.Mutation;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.ModuleLepidopterology;

import org.jetbrains.annotations.NotNull;

public class Butterfly extends IndividualLiving<IButterflySpecies, IButterfly, IButterflySpeciesType> implements IButterfly {
	private static final RandomSource rand = RandomSource.create();
	public static final Codec<Butterfly> CODEC = RecordCodecBuilder.create(instance -> {
		Codec<IGenome> genomeCodec = SpeciesUtil.BUTTERFLY_TYPE.get().getKaryotype().getGenomeCodec();

		return instance.group(
				genomeCodec.fieldOf("genome").forGetter(IIndividual::getGenome),
				genomeCodec.optionalFieldOf("mate", null).forGetter(IIndividual::getMate)
		).apply(instance, Butterfly::new);
	});

	public Butterfly(IGenome genome) {
		super(genome);
	}

	public Butterfly(IGenome genome, @Nullable IGenome mate) {
		super(genome);

		this.mate = mate;
	}

	@Override
	public Component getDisplayName() {
		return this.species.getDisplayName();
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
		IButterflySpecies species = genome.getActiveValue(ButterflyChromosomes.SPECIES);
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
		IButterflySpecies species = getGenome().getActiveValue(ButterflyChromosomes.SPECIES);
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
				getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getTemperature(), getGenome().getActiveValue(ButterflyChromosomes.TEMPERATURE_TOLERANCE),
				getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getHumidity(), getGenome().getActiveValue(ButterflyChromosomes.HUMIDITY_TOLERANCE));
	}

	@Nullable
	@Override
	public IButterfly spawnCaterpillar(IButterflyNursery nursery) {
		// We need a mated queen to produce offspring.
		if (this.mate == null) {
			return null;
		}

		SpeciesUtil.ISpeciesMutator mutator = (p1, p2) -> mutateSpecies(nursery, p1, p2);
		return SpeciesUtil.createOffspring(nursery.getWorldObj().random, this.genome, this.mate, mutator, Butterfly::new);
	}

	@Nullable
	private static ImmutableList<AllelePair<?>> mutateSpecies(IButterflyNursery nursery, IGenome parent1, IGenome parent2) {
		return SpeciesUtil.mutateSpecies(nursery.getWorldObj(), nursery.getCoordinates(), null, parent1, parent2, ButterflyChromosomes.SPECIES, Mutation::getChance);
	}

	private boolean isActiveThisTime(boolean isDayTime) {
		if (getGenome().getActiveValue(ButterflyChromosomes.NEVER_SLEEPS)) {
			return true;
		}

		return isDayTime != getGenome().getActiveValue(ButterflyChromosomes.SPECIES).isNocturnal();
	}

	@Override
	protected IIntegerChromosome getLifespanChromosome() {
		return ButterflyChromosomes.LIFESPAN;
	}

	@Override
	public List<ItemStack> getLootDrop(IEntityButterfly entity, boolean playerKill, int lootLevel) {
		ArrayList<ItemStack> drop = new ArrayList<>();

		PathfinderMob creature = entity.getEntity();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		List<Product> products = getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getButterflyLoot();
		RandomSource rand = creature.level.random;

		for (Product product : products) {
			if (rand.nextFloat() < product.chance() * metabolism) {
				drop.add(product.createStack());
			}
		}

		return drop;
	}

	@Override
	public List<ItemStack> getCaterpillarDrop(IButterflyNursery nursery, boolean playerKill, int lootLevel) {
		ArrayList<ItemStack> drop = new ArrayList<>();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		List<Product> products = getGenome().getActiveValue(ButterflyChromosomes.SPECIES).getCaterpillarProducts();

		for (Product product : products) {
			if (rand.nextFloat() < product.chance() * metabolism) {
				drop.add(product.createStack());
			}
		}

		return drop;
	}

	@Override
	public List<ItemStack> getCocoonDrop(boolean includeButterfly, IButterflyCocoon cocoon) {
		ArrayList<ItemStack> drop = new ArrayList<>();
		float metabolism = (float) getGenome().getActiveValue(ButterflyChromosomes.METABOLISM) / 10;
		List<Product> products = cocoon.getProducts();

		for (Product product : products) {
			if (rand.nextFloat() < product.chance() * metabolism) {
				drop.add(product.createStack());
			}
		}

		IButterflySpeciesType butterflyType = SpeciesUtil.BUTTERFLY_TYPE.get();

		if (ModuleLepidopterology.getSerumChance() > 0) {
			if (rand.nextFloat() < ModuleLepidopterology.getSerumChance() * metabolism) {
				ItemStack stack = butterflyType.createStack(this, ButterflyLifeStage.SERUM);
				if (ModuleLepidopterology.getSecondSerumChance() > 0) {
					if (rand.nextFloat() < ModuleLepidopterology.getSecondSerumChance() * metabolism) {
						stack.setCount(2);
					}
				}
				drop.add(butterflyType.createStack(this, ButterflyLifeStage.SERUM));
			}
		}

		if (includeButterfly) {
			drop.add(butterflyType.createStack(this, ButterflyLifeStage.BUTTERFLY));
		}

		return drop;
	}
}
