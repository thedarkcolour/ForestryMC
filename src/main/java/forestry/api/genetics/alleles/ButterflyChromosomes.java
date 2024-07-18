package forestry.api.genetics.alleles;

import forestry.api.apiculture.IFlowerType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.IButterflySpecies;
import forestry.api.lepidopterology.IButterflyEffect;
import forestry.core.genetics.alleles.BooleanChromosome;
import forestry.core.genetics.alleles.FloatChromosome;
import forestry.core.genetics.alleles.IntegerChromosome;
import forestry.core.genetics.alleles.SpeciesChromosome;
import forestry.core.genetics.alleles.ValueChromosome;

import static forestry.api.ForestryConstants.forestry;

public class ButterflyChromosomes {
	/**
	 * Determines the species of a butterfly.
	 */
	public static final ISpeciesChromosome<IButterflySpecies> SPECIES = new SpeciesChromosome<>(ForestrySpeciesType.BUTTERFLY, IButterflySpecies.class);
	/**
	 * Determines physical size of a butterfly.
	 */
	public static final IFloatChromosome SIZE = new FloatChromosome(forestry("size"));
	/**
	 * Determines the flight speed of a butterfly.
	 */
	public static final IFloatChromosome SPEED = BeeChromosomes.SPEED;
	/**
	 * Determines how long this butterfly will live (todo change? butterflies randomly dying isn't minecraft-y)
	 */
	public static final IIntegerChromosome LIFESPAN = new IntegerChromosome(forestry("butterfly_lifespan"));
	/**
	 * Determines the rate at which caterpillars destroy leaves and influences cocoon drops.
	 */
	public static final IIntegerChromosome METABOLISM = new IntegerChromosome(forestry("metabolism"));
	/**
	 * TODO document
	 */
	public static final IIntegerChromosome FERTILITY = BeeChromosomes.FERTILITY;
	/**
	 * Determines the acceptable range of temperatures from a butterfly's ideal temperature.
	 */
	public static final IValueChromosome<ToleranceType> TEMPERATURE_TOLERANCE = BeeChromosomes.TEMPERATURE_TOLERANCE;
	/**
	 * Determines the acceptable range of humidities from a butterfly's ideal humidity.
	 */
	public static final IValueChromosome<ToleranceType> HUMIDITY_TOLERANCE = BeeChromosomes.TEMPERATURE_TOLERANCE;
	/**
	 * Whether diurnal butterflies can work during the night, or nocturnal butterflies (moths) can work during the day.
	 */
	public static final IBooleanChromosome NEVER_SLEEPS = BeeChromosomes.NEVER_SLEEPS;
	/**
	 * TODO document
	 */
	public static final IBooleanChromosome TOLERATES_RAIN = BeeChromosomes.TOLERATES_RAIN;
	/**
	 * Whether this butterfly is immune to fire/lava damage.
	 */
	public static final IBooleanChromosome FIREPROOF = new BooleanChromosome(forestry("fireproof"));
	/**
	 * The type of flowers this butterfly likes to fly around.
	 */
	public static final IValueChromosome<IFlowerType> FLOWER_TYPE = BeeChromosomes.FLOWER_TYPE;
	/**
	 * TODO document
	 */
	public static final IValueChromosome<IButterflyEffect> EFFECT = new ValueChromosome<>(forestry("butterfly_effect"), IButterflyEffect.class);
	/**
	 * Was used for silk moths (Bombyx Mori) to place special cocoons. TODO reimplement
	 */
	public static final IValueChromosome<IButterflyCocoon> COCOON = new ValueChromosome<>(forestry("cocoon"), IButterflyCocoon.class);
}
