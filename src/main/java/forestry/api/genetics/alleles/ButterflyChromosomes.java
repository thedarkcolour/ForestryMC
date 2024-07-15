package forestry.api.genetics.alleles;

import forestry.api.apiculture.IFlowerType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.lepidopterology.genetics.IAlleleButterflyCocoon;
import forestry.api.lepidopterology.genetics.IAlleleButterflyEffect;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.core.genetics.alleles.BooleanChromosome;
import forestry.core.genetics.alleles.FloatChromosome;
import forestry.core.genetics.alleles.IntegerChromosome;
import forestry.core.genetics.alleles.SpeciesChromosome;

import deleteme.Todos;
import static forestry.api.ForestryConstants.forestry;

public class ButterflyChromosomes {
	/**
	 * Determines the species of a butterfly.
	 */
	public static final ISpeciesChromosome<IButterflySpeciesType> SPECIES = new SpeciesChromosome<>(ForestrySpeciesType.BUTTERFLY);
	/**
	 * Determines physical size of a butterfly.
	 */
	public static final IFloatChromosome SIZE = new FloatChromosome(forestry("size"));
	/**
	 * Determines how long this butterfly will live (todo change? butterflies randomly dying isn't minecraft-y)
	 */
	public static final IIntegerChromosome LIFESPAN = new IntegerChromosome(forestry("butterfly_lifespan"));
	/**
	 * Determines the rate at which caterpillars destroy leaves and influences cocoon drops.
	 */
	public static final IIntegerChromosome METABOLISM = new IntegerChromosome(forestry("metabolism"));
	public static final IValueChromosome<ToleranceType> TEMPERATURE_TOLERANCE = BeeChromosomes.TEMPERATURE_TOLERANCE;
	public static final IValueChromosome<ToleranceType> HUMIDITY_TOLERANCE = BeeChromosomes.TEMPERATURE_TOLERANCE;
	public static final IBooleanChromosome NEVER_SLEEPS = BeeChromosomes.NEVER_SLEEPS;
	public static final IBooleanChromosome TOLERATES_RAIN = BeeChromosomes.TOLERATES_RAIN;
	/**
	 * Whether this butterfly is immune to fire/lava damage.
	 */
	public static final IBooleanChromosome FIRE_RESISTANT = new BooleanChromosome(forestry("fire_resistant"));
	public static final IValueChromosome<IFlowerType> FLOWER_PROVIDER = BeeChromosomes.FLOWER_TYPE;
	public static final IValueChromosome<IAlleleButterflyEffect> EFFECT = Todos.todo();
	public static final IValueChromosome<IAlleleButterflyCocoon> COCOON = Todos.todo();
}
