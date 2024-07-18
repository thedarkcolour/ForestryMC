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
package forestry.apiculture.genetics;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.IForestryApi;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeeSpecies;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.genetics.IBeeMutation;
import forestry.api.climate.IClimateManager;
import forestry.api.core.ForestryError;
import forestry.api.core.HumidityType;
import forestry.api.core.IError;
import forestry.api.core.TemperatureType;
import forestry.api.core.ToleranceType;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.AllelePair;
import forestry.core.config.Config;
import forestry.core.config.Constants;
import forestry.core.genetics.GenericRatings;
import forestry.core.genetics.IndividualLiving;
import forestry.core.utils.GeneticsUtil;
import forestry.core.utils.VecUtil;

import genetics.api.individual.IIndividual;
import forestry.core.genetics.Genome;

public class Bee extends IndividualLiving implements IBee {
	private static final String NBT_NATURAL = "NA";
	private static final String NBT_GENERATION = "GEN";

	private int generation;
	private boolean isPristine = true;

	/* CONSTRUCTOR */
	public Bee(CompoundTag nbt) {
		super(nbt);

		if (nbt.contains(NBT_NATURAL)) {
			this.isPristine = nbt.getBoolean(NBT_NATURAL);
		}

		if (nbt.contains(NBT_GENERATION)) {
			this.generation = nbt.getInt(NBT_GENERATION);
		}
	}

	public Bee(IGenome genome) {
		this(genome, (IGenome) null);
	}


	public Bee(IGenome genome, IBee mate) {
		this(genome, mate.getGenome());
	}


	public Bee(IGenome genome, @Nullable IGenome mate) {
		this(genome, mate, true, 0);
	}

	private Bee(IGenome genome, @Nullable IGenome mate, boolean isPristine, int generation) {
		super(genome, mate, genome.getActiveValue(BeeChromosomes.LIFESPAN));
		this.isPristine = isPristine;
		this.generation = generation;
	}

	@Override
	public ISpeciesType getRoot() {
		return BeeManager.beeRoot;
	}

	@Override
	public CompoundTag write(CompoundTag compound) {

		compound = super.write(compound);

		if (!isPristine) {
			compound.putBoolean(NBT_NATURAL, false);
		}

		if (generation > 0) {
			compound.putInt(NBT_GENERATION, generation);
		}
		return compound;
	}

	@Override
	public void setIsNatural(boolean flag) {
		this.isPristine = flag;
	}

	@Override
	public boolean isPristine() {
		return this.isPristine;
	}

	@Override
	public int getGeneration() {
		return generation;
	}

	/* EFFECTS */
	@Override
	public IEffectData[] doEffect(IEffectData[] storedData, IBeeHousing housing) {
		IBeeEffect effect = genome.getActiveValue(BeeChromosomes.EFFECT);

		storedData[0] = doEffect(effect, storedData[0], housing);

		// Return here if the primary can already not be combined
		if (!effect.isCombinable()) {
			return storedData;
		}

		IBeeEffect secondary = genome.getInactiveValue(BeeChromosomes.EFFECT);
		if (!secondary.isCombinable()) {
			return storedData;
		}

		storedData[1] = doEffect(secondary, storedData[1], housing);

		return storedData;
	}

	private IEffectData doEffect(IBeeEffect effect, IEffectData storedData, IBeeHousing housing) {
		storedData = effect.validateStorage(storedData);
		return effect.doEffect(genome, storedData, housing);
	}

	@Override
	public IEffectData[] doFX(IEffectData[] storedData, IBeeHousing housing) {
		IBeeEffect effect = genome.getActiveValue(BeeChromosomes.EFFECT);

		storedData[0] = doFX(effect, storedData[0], housing);

		// Return here if the primary can already not be combined
		if (!effect.isCombinable()) {
			return storedData;
		}

		IBeeEffect secondary = genome.getInactiveValue(BeeChromosomes.EFFECT);
		if (!secondary.isCombinable()) {
			return storedData;
		}

		storedData[1] = doFX(secondary, storedData[1], housing);

		return storedData;
	}

	private IEffectData doFX(IBeeEffect effect, IEffectData storedData, IBeeHousing housing) {
		return effect.doFX(genome, storedData, housing);
	}

	// / INFORMATION

	@Override
	public IBee copy() {
		CompoundTag compound = new CompoundTag();
		this.write(compound);
		return new Bee(compound);
	}

	@Override
	public boolean canSpawn() {
		return mate != null;
	}

	@Override
	public Set<IError> getCanWork(IBeeHousing housing) {
		Level world = housing.getWorldObj();

		Set<IError> errorStates = new HashSet<>();

		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

		// / Rain needs tolerant flyers
		if (housing.isRaining() && !canFlyInRain(beeModifier)) {
			errorStates.add(ForestryError.IS_RAINING);
		}

		// / Night or darkness requires nocturnal species
		if (world.isDay()) {
			if (!canWorkDuringDay()) {
				errorStates.add(ForestryError.NOT_NIGHT);
			}
		} else {
			if (!canWorkAtNight(beeModifier)) {
				errorStates.add(ForestryError.NOT_DAY);
			}
		}

		if (housing.getBlockLightValue() > Constants.APIARY_MIN_LEVEL_LIGHT) {
			if (!canWorkDuringDay()) {
				errorStates.add(ForestryError.NOT_GLOOMY);
			}
		} else {
			if (!canWorkAtNight(beeModifier)) {
				errorStates.add(ForestryError.NOT_BRIGHT);
			}
		}

		// / Check for the sky, except if in hell
		if (!world.dimensionType().hasCeiling()) {//TODO: We used 'isNether' earlier not sure if 'hasCeiling' is the right replacment method
			if (!housing.canBlockSeeTheSky() && !canWorkUnderground(beeModifier)) {
				errorStates.add(ForestryError.NO_SKY);
			}
		}

		// / And finally climate check
		IBeeSpecies species = genome.getActiveValue(BeeChromosomes.SPECIES);
		{
			TemperatureType actualTemperature = housing.temperature();
			TemperatureType beeBaseTemperature = species.getTemperature();
			ToleranceType beeToleranceTemperature = genome.getActiveValue(BeeChromosomes.TEMPERATURE_TOLERANCE);

			if (!ClimateHelper.isWithinLimits(actualTemperature, beeBaseTemperature, beeToleranceTemperature)) {
				if (beeBaseTemperature.ordinal() > actualTemperature.ordinal()) {
					errorStates.add(ForestryError.TOO_COLD);
				} else {
					errorStates.add(ForestryError.TOO_HOT);
				}
			}
		}

		{
			HumidityType actualHumidity = housing.humidity();
			HumidityType beeBaseHumidity = species.getHumidity();
			ToleranceType beeToleranceHumidity = genome.getActiveValue(BeeChromosomes.HUMIDITY_TOLERANCE);

			if (!ClimateHelper.isWithinLimits(actualHumidity, beeBaseHumidity, beeToleranceHumidity)) {
				if (beeBaseHumidity.ordinal() > actualHumidity.ordinal()) {
					errorStates.add(ForestryError.TOO_ARID);
				} else {
					errorStates.add(ForestryError.TOO_HUMID);
				}
			}
		}

		return errorStates;
	}

	private boolean canWorkAtNight(IBeeModifier beeModifier) {
		return genome.getActiveValue(BeeChromosomes.SPECIES).isNocturnal() || genome.getActiveValue(BeeChromosomes.NEVER_SLEEPS) || beeModifier.isSelfLighted();
	}

	private boolean canWorkDuringDay() {
		return !genome.getActiveValue(BeeChromosomes.SPECIES).isNocturnal() || genome.getActiveValue(BeeChromosomes.NEVER_SLEEPS);
	}

	private boolean canWorkUnderground(IBeeModifier beeModifier) {
		return genome.getActiveValue(BeeChromosomes.CAVE_DWELLING) || beeModifier.isSunlightSimulated();
	}

	private boolean canFlyInRain(IBeeModifier beeModifier) {
		return genome.getActiveValue(BeeChromosomes.TOLERATES_RAIN) || beeModifier.isSealed();
	}

	private boolean isSuitableBiome(Holder<Biome> biome) {
		IClimateManager manager = IForestryApi.INSTANCE.getClimateManager();
		TemperatureType temperature = manager.getTemperature(biome);
		HumidityType humidity = manager.getHumidity(biome);
		return isSuitableClimate(temperature, humidity);
	}

	private boolean isSuitableClimate(TemperatureType temperature, HumidityType humidity) {
		return ClimateHelper.isWithinLimits(temperature, humidity,
				this.genome.getActiveValue(BeeChromosomes.SPECIES).getTemperature(), this.genome.getActiveValue(BeeChromosomes.TEMPERATURE_TOLERANCE),
				this.genome.getActiveValue(BeeChromosomes.SPECIES).getHumidity(), this.genome.getActiveValue(BeeChromosomes.HUMIDITY_TOLERANCE)
		);
	}

	// todo this can be optimized in a cache somewhere
	@Override
	public List<Holder.Reference<Biome>> getSuitableBiomes(Registry<Biome> registry) {
		return registry.holders().filter(this::isSuitableBiome).toList();
	}

	@Override
	public void addTooltip(List<Component> list) {
		ToolTip toolTip = new ToolTip();

		// No info 4 u!
		if (!isAnalyzed) {
			toolTip.singleLine().text("<").translated("for.gui.unknown").text(">").style(ChatFormatting.GRAY).create();
			return;
		}

		// You analyzed it? Juicy tooltip coming up!
		if (!isPureBred(BeeChromosomes.SPECIES)) {
			Component active = this.genome.getActiveName(BeeChromosomes.SPECIES);
			Component inactive = this.genome.getInactiveName(BeeChromosomes.SPECIES);

			toolTip.add(Component.translatable("for.bees.hybrid", active, inactive).withStyle(ChatFormatting.BLUE));
		}

		if (generation > 0) {
			Rarity rarity;
			if (generation >= 1000) {
				rarity = Rarity.EPIC;
			} else if (generation >= 100) {
				rarity = Rarity.RARE;
			} else if (generation >= 10) {
				rarity = Rarity.UNCOMMON;
			} else {
				rarity = Rarity.COMMON;
			}
			toolTip.translated("for.gui.beealyzer.generations", generation).style(rarity.color);
		}

		toolTip.singleLine()
				.add(genome.getActiveName(BeeChromosomes.LIFESPAN))
				.text(" ")
				.translated("for.gui.life")
				.style(ChatFormatting.GRAY)
				.create();

		toolTip.singleLine()
				.add(genome.getActiveName(BeeChromosomes.SPEED))
				.text(" ")
				.translated("for.gui.worker")
				.style(ChatFormatting.GRAY)
				.create();

		Component tempToleranceAllele = this.genome.getActiveName(BeeChromosomes.TEMPERATURE_TOLERANCE);
		Component humidToleranceAllele = this.genome.getActiveName(BeeChromosomes.HUMIDITY_TOLERANCE);
		IBeeSpecies active = this.genome.getActiveValue(BeeChromosomes.SPECIES);

		toolTip.singleLine().text("T: ").add(ClimateHelper.toDisplay(active.getTemperature())).text(" / ").add(tempToleranceAllele).style(ChatFormatting.GREEN).create();
		toolTip.singleLine().text("H: ").add(ClimateHelper.toDisplay(active.getHumidity())).text(" / ").add(humidToleranceAllele).style(ChatFormatting.GREEN).create();


		toolTip.add(genome.getActiveName(BeeChromosomes.FLOWER_TYPE), ChatFormatting.GRAY);

		if (genome.getActiveValue(BeeChromosomes.NEVER_SLEEPS)) {
			toolTip.add(GenericRatings.rateActivityTime(true, false)).style(ChatFormatting.RED);
		}

		if (genome.getActiveValue(BeeChromosomes.TOLERATES_RAIN)) {
			toolTip.translated("for.gui.flyer.tooltip").style(ChatFormatting.WHITE);
		}
		list.addAll(toolTip.getLines());
	}
/*
	@Override
	public void age(Level world, float housingLifespanModifier) {
		IBeekeepingMode mode = BeeManager.beeRoot.getBeekeepingMode(world);
		IBeeModifier beeModifier = mode.getBeeModifier();
		float finalModifier = housingLifespanModifier * beeModifier.getLifespanModifier(genome, mate, housingLifespanModifier);

		super.age(world, finalModifier);
	}*/

	// / PRODUCTION
	@Override
	public NonNullList<ItemStack> getProduceList() {
		HashSet<ItemStack> products = new HashSet<>();

		IBeeSpecies primary = genome.getActiveValue(BeeChromosomes.SPECIES);
		IBeeSpecies secondary = genome.getInactiveValue(BeeChromosomes.SPECIES);

		products.addAll(primary.getProducts().getPossibleStacks());

		NonNullList<ItemStack> secondaryProducts = secondary.getProducts().getPossibleStacks();
		// Remove duplicates
		for (ItemStack second : secondaryProducts) {
			boolean skip = false;

			for (ItemStack compare : products) {
				if (second.sameItem(compare)) {
					skip = true;
					break;
				}
			}

			if (!skip) {
				products.add(second);
			}

		}

		return products;
	}

	@Override
	public NonNullList<ItemStack> getSpecialtyList() {
		return genome.getActiveAllele(BeeChromosomes.SPECIES).getSpecialties().getPossibleStacks();
	}

	@Override
	public NonNullList<ItemStack> produceStacks(IBeeHousing housing) {
		Level world = housing.getWorldObj();
		//IBeekeepingMode mode = BeeManager.beeRoot.getBeekeepingMode(world);

		NonNullList<ItemStack> products = NonNullList.create();

		IBeeSpecies primary = genome.getActiveValue(BeeChromosomes.SPECIES);
		IBeeSpecies secondary = genome.getInactiveValue(BeeChromosomes.SPECIES);

		IBeeModifier beeHousingModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
		//IBeeModifier beeModeModifier = mode.getBeeModifier();

		// Bee genetic speed * beehousing * beekeeping mode
		float speed = genome.getActiveValue(BeeChromosomes.SPEED) * beeHousingModifier.getProductionModifier(genome, 1f)/* * beeModeModifier.getProductionModifier(genome, 1f)*/;

		// / Primary Products
		primary.getProducts().addProducts(world, housing.getCoordinates(), products, (product) -> product.getChance() * speed, world.random);
		// / Secondary Products
		secondary.getProducts().addProducts(world, housing.getCoordinates(), products, (product) -> Math.round(product.getChance() / 2) * speed, world.random);

		// / Specialty products
		if (primary.isJubilant(genome, housing) && secondary.isJubilant(genome, housing)) {
			primary.getSpecialties().addProducts(world, housing.getCoordinates(), products, (product) -> product.getChance() * speed, world.random);
		}

		BlockPos housingCoordinates = housing.getCoordinates();
		return genome.getActiveValue(BeeChromosomes.FLOWER_TYPE).affectProducts(world, this, housingCoordinates, products);
	}

	/* REPRODUCTION */
	@Override
	@Nullable
	public IBee spawnPrincess(IBeeHousing housing) {
		// We need a mated queen to produce offspring.
		if (mate == null) {
			return null;
		}

		// todo config for ignoble stock
		// Fatigued queens do not produce princesses.
		/*if (BeeManager.beeRoot.getBeekeepingMode(housing.getWorldObj()).isFatigued(this, housing)) {
			return null;
		}*/

		return createOffspring(housing, mate, getGeneration() + 1);
	}

	@Override
	public List<IBee> spawnDrones(IBeeHousing housing) {

		Level world = housing.getWorldObj();

		// We need a mated queen to produce offspring.
		if (mate == null) {
			return Collections.emptyList();
		}

		List<IBee> bees = new ArrayList<>();

		BlockPos housingPos = housing.getCoordinates();
		int toCreate = BeeManager.beeRoot.getBeekeepingMode(world).getFinalFertility(this, world, housingPos);

		if (toCreate <= 0) {
			toCreate = 1;
		}

		for (int i = 0; i < toCreate; i++) {
			IBee offspring = createOffspring(housing, mate, 0);
			offspring.setIsNatural(true);
			bees.add(offspring);
		}

		return bees;
	}

	private IBee createOffspring(IBeeHousing housing, IGenome mate, int generation) {
		Level level = housing.getWorldObj();

		Genome.Builder genome = new Genome.Builder(this.genome.getKaryotype());
		AllelePair[] parent1 = this.genome.getAllelePairs();
		AllelePair[] parent2 = mate.getAllelePairs();

		// Check for mutation. Replace one of the parents with the mutation
		// template if mutation occurred.
		AllelePair[] mutated1 = mutateSpecies(housing, this.genome, mate);
		if (mutated1 != null) {
			parent1 = mutated1;
		}
		AllelePair[] mutated2 = mutateSpecies(housing, mate, this.genome);
		if (mutated2 != null) {
			parent2 = mutated2;
		}

		for (int i = 0; i < parent1.length; i++) {
			if (parent1[i] != null && parent2[i] != null) {
				genome[i] = parent1[i].inheritChromosome(level.random, parent2[i]);
			}
		}

		IBeekeepingMode mode = BeeManager.beeRoot.getBeekeepingMode(level);
		return new Bee(new Genome(BeeManager.beeRoot.getKaryotype(), genome), null, mode.isOffspringPristine(this), generation);
	}

	@Nullable
	private static AllelePair[] mutateSpecies(IBeeHousing housing, IGenome genomeOne, IGenome genomeTwo) {

		Level world = housing.getWorldObj();

		AllelePair[] parent1 = genomeOne.getAllelePairs();
		AllelePair[] parent2 = genomeTwo.getAllelePairs();

		IGenome genome0;
		IGenome genome1;

		IAlleleBeeSpecies allele0;
		IAlleleBeeSpecies allele1;

		if (world.random.nextBoolean()) {
			allele0 = (IAlleleBeeSpecies) parent1[(BeeChromosomes.SPECIES).ordinal()].active();
			allele1 = (IAlleleBeeSpecies) parent2[(BeeChromosomes.SPECIES).ordinal()].inactive();

			genome0 = genomeOne;
			genome1 = genomeTwo;
		} else {
			allele0 = (IAlleleBeeSpecies) parent2[(BeeChromosomes.SPECIES).ordinal()].active();
			allele1 = (IAlleleBeeSpecies) parent1[(BeeChromosomes.SPECIES).ordinal()].inactive();

			genome0 = genomeTwo;
			genome1 = genomeOne;
		}

		GameProfile playerProfile = housing.getOwner();
		IApiaristTracker breedingTracker = BeeManager.beeRoot.getBreedingTracker(world, playerProfile);

		IMutationContainer<IBee, ? extends IMutation> container = ForestrySpeciesType.BEE;
		List<? extends IMutation> combinations = container.getCombinations(allele0, allele1, true);
		for (IMutation mutation : combinations) {
			IBeeMutation beeMutation = (IBeeMutation) mutation;

			float chance = beeMutation.getChance(housing, allele0, allele1, genome0, genome1);
			if (chance <= 0) {
				continue;
			}

			// boost chance for researched mutations
			if (breedingTracker.isResearched(beeMutation)) {
				float mutationBoost = chance * (Config.researchMutationBoostMultiplier - 1.0f);
				mutationBoost = Math.min(Config.maxResearchMutationBoostPercent, mutationBoost);
				chance += mutationBoost;
			}

			if (chance > world.random.nextFloat() * 100) {
				breedingTracker.registerMutation(mutation);
				return BeeManager.beeRoot.getKaryotype().templateAsChromosomes(mutation.getTemplate());
			}
		}

		return null;
	}

	/* FLOWERS */
	@Nullable
	@Override
	public IIndividual retrievePollen(IBeeHousing housing) {
		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

		int chance = Math.round(genome.getActiveValue(BeeChromosomes.POLLINATION) * beeModifier.getFloweringModifier(getGenome(), 1f));

		Level level = housing.getWorldObj();
		RandomSource random = level.random;

		// Correct speed
		if (random.nextInt(100) >= chance) {
			return null;
		}

		Vec3i area = getArea(genome, beeModifier);
		Vec3i offset = new Vec3i(-area.getX() / 2, -area.getY() / 4, -area.getZ() / 2);
		BlockPos housingPos = housing.getCoordinates();

		for (int i = 0; i < 20; i++) {
			BlockPos randomPos = VecUtil.sum(housingPos, VecUtil.getRandomPositionInArea(random, area), offset);

			if (level.hasChunkAt(randomPos)) {
				if (genome.getActiveValue(BeeChromosomes.FLOWER_TYPE).isAcceptableFlower(level, randomPos)) {
					return GeneticsUtil.getPollen(level, randomPos);
				}
			}
		}

		return null;
	}

	@Override
	public boolean pollinateRandom(IBeeHousing housing, IIndividual pollen) {

		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

		int chance = (int) (genome.getActiveValue(BeeChromosomes.POLLINATION) * beeModifier.getFloweringModifier(getGenome(), 1f));

		Level world = housing.getWorldObj();
		RandomSource random = world.random;

		// Correct speed
		if (random.nextInt(100) >= chance) {
			return false;
		}

		Vec3i area = getArea(genome, beeModifier);
		Vec3i offset = new Vec3i(-area.getX() / 2, -area.getY() / 4, -area.getZ() / 2);
		BlockPos housingPos = housing.getCoordinates();

		for (int i = 0; i < 30; i++) {

			BlockPos randomPos = VecUtil.getRandomPositionInArea(random, area);
			BlockPos posBlock = VecUtil.sum(housingPos, randomPos, offset);

			ICheckPollinatable checkPollinatable = GeneticsUtil.getCheckPollinatable(world, posBlock);
			if (checkPollinatable == null) {
				continue;
			}

			if (!genome.getActiveValue(BeeChromosomes.FLOWER_TYPE).isAcceptableFlower(world, posBlock)) {
				continue;
			}
			if (!checkPollinatable.canMateWith(pollen)) {
				continue;
			}

			IPollinatable realPollinatable = GeneticsUtil.getOrCreatePollinatable(housing.getOwner(), world, posBlock, Config.pollinateVanillaTrees);

			if (realPollinatable != null) {
				realPollinatable.mateWith(pollen);
				return true;
			}
		}

		return false;
	}

	@Nullable
	@Override
	public BlockPos plantFlowerRandom(IBeeHousing housing, List<BlockState> potentialFlowers) {
		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

		int chance = Math.round(genome.getActiveValue(BeeChromosomes.POLLINATION) * beeModifier.getFloweringModifier(getGenome(), 1f));

		Level level = housing.getWorldObj();
		RandomSource random = level.random;

		// Correct speed
		if (random.nextInt(100) >= chance) {
			return null;
		}
		// Gather required info
		IFlowerType flowerType = genome.getActiveValue(BeeChromosomes.FLOWER_TYPE);
		Vec3i area = getArea(genome, beeModifier);
		Vec3i offset = new Vec3i(-area.getX() / 2, -area.getY() / 4, -area.getZ() / 2);
		BlockPos housingPos = housing.getCoordinates();

		for (int i = 0; i < 10; i++) {
			BlockPos randomPos = VecUtil.getRandomPositionInArea(random, area);
			BlockPos posBlock = VecUtil.sum(housingPos, randomPos, offset);

			if (flowerType.plantRandomFlower(level, posBlock, potentialFlowers)) {
				return posBlock;
			}
		}
		return null;
	}

	@Override
	public Iterator<BlockPos.MutableBlockPos> getAreaIterator(IBeeHousing housing) {
		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
		Vec3i area = getArea(this.genome, beeModifier);
		BlockPos housingPos = housing.getCoordinates();
		BlockPos minPos = housingPos.offset(-area.getX() / 2, -area.getY() / 2, -area.getZ() / 2);
		BlockPos maxPos = minPos.offset(area);
		Level level = housing.getWorldObj();
		return VecUtil.getAllInBoxFromCenterMutable(level, minPos, housingPos, maxPos);
	}

	private static Vec3i getArea(IGenome genome, IBeeModifier beeModifier) {
		Vec3i genomeTerritory = genome.getActiveValue(BeeChromosomes.TERRITORY);
		float housingModifier = beeModifier.getTerritoryModifier(genome, 1f);
		return VecUtil.scale(genomeTerritory, housingModifier * 3.0f);
	}
}
