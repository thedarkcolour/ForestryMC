package forestry.api.genetics.alleles;

import net.minecraft.core.Vec3i;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.core.genetics.alleles.BooleanChromosome;
import forestry.core.genetics.alleles.FloatChromosome;
import forestry.core.genetics.alleles.IntegerChromosome;
import forestry.core.genetics.alleles.SpeciesChromosome;
import forestry.core.genetics.alleles.ValueChromosome;

import static forestry.api.ForestryConstants.forestry;

/**
 * All chromosomes of the Forestry bee species.
 */
public class BeeChromosomes {
	/**
	 * The species of a bee.
	 */
	public static final ISpeciesChromosome<IBeeSpeciesType> SPECIES = new SpeciesChromosome<>(ForestrySpeciesType.BEE);
	/**
	 * Determines a queen's production speed.
	 */
	public static final IFloatChromosome SPEED = new FloatChromosome(forestry("bee_speed"));
	/**
	 * Determines a queen's lifespan.
	 */
	public static final IIntegerChromosome LIFESPAN = new IntegerChromosome(forestry("bee_lifespan"));
	/**
	 * The number of drones given when a queen dies.
	 */
	public static final IIntegerChromosome FERTILITY = new IntegerChromosome(forestry("bee_fertility"));
	/**
	 * Determines the acceptable range of temperatures from a bee's ideal temperature. Reused by trees and butterflies.
	 */
	public static final IValueChromosome<ToleranceType> TEMPERATURE_TOLERANCE = new ValueChromosome<>(forestry("temperature_tolerance"));
	/**
	 * Determines the acceptable range of humidities from a bee's ideal humidity. Reused by trees and butterflies.
	 */
	public static final IValueChromosome<ToleranceType> HUMIDITY_TOLERANCE = new ValueChromosome<>(forestry("humidity_tolerance"));
	/**
	 * Whether diurnal bees can work during the night, or nocturnal bees can work during the day.
	 */
	public static final IBooleanChromosome NEVER_SLEEPS = new BooleanChromosome(forestry("never_sleeps"));
	// Bee-specific chromosomes
	public static final IBooleanChromosome CAVE_DWELLING = new BooleanChromosome(forestry("cave_dwelling"));
	public static final IBooleanChromosome TOLERATES_RAIN = new BooleanChromosome(forestry("tolerates_rain"));
	public static final IValueChromosome<IFlowerType> FLOWER_TYPE = new ValueChromosome<>(forestry("flower_type"));
	/**
	 * Determines the effect of a bee species. Its range is determined by {@link #TERRITORY}.
	 */
	public static final IValueChromosome<IBeeEffect> EFFECT = new ValueChromosome<>(forestry("bee_effect"));
	/**
	 * Determines how fast the hive can pollinate trees and plant flowers. Range is determined by {@link #TERRITORY}.
	 */
	public static final IIntegerChromosome POLLINATION = new IntegerChromosome(forestry("pollination"));
	/**
	 * Determines the area in which a bee can pollinate trees, grow flowers, and use its special effect.
	 */
	public static final IValueChromosome<Vec3i> TERRITORY = new ValueChromosome<>(forestry("territory"));
}
