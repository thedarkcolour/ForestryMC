package forestry.api.genetics.alleles;

import net.minecraftforge.common.PlantType;

import forestry.api.arboriculture.IFruitProvider;
import forestry.api.arboriculture.genetics.IAlleleFruit;
import forestry.api.arboriculture.genetics.IAlleleLeafEffect;
import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.IFruitFamily;
import forestry.core.genetics.alleles.BooleanChromosome;
import forestry.core.genetics.alleles.FloatChromosome;
import forestry.core.genetics.alleles.IntegerChromosome;
import forestry.core.genetics.alleles.SpeciesChromosome;
import forestry.core.genetics.alleles.ValueChromosome;

import static forestry.api.ForestryConstants.forestry;

public class TreeChromosomes {
	/**
	 * Determines the following: - WorldGen, including the used wood blocks - {@link IFruitFamily}s supported. Limits which {@link IFruitProvider}
	 * will actually yield fruit with this species. - Native {@link PlantType} for this tree. Combines with the PLANT chromosome.
	 */
	public static final IValueChromosome<ITreeSpeciesType> SPECIES = new SpeciesChromosome<>(forestry("species"));
	/**
	 * A float modifying the height of the tree. Taken into account at worldgen.
	 */
	public static final IFloatChromosome HEIGHT = new FloatChromosome(forestry("height"));
	/**
	 * Chance for saplings.
	 */
	public static final IFloatChromosome FERTILITY = new FloatChromosome(forestry("fertility"));
	/**
	 * {@link IFruitProvider}, determines if and what fruits are grown on the tree. Limited by the {@link IFruitFamily}s the species supports.
	 */
	public static final IValueChromosome<IAlleleFruit> FRUITS = new ValueChromosome<>(forestry("fruits"));
	/**
	 * Chance for fruit leaves and/or drops.
	 */
	public static final IFloatChromosome YIELD = new FloatChromosome(forestry("yield"));
	/**
	 * Determines the speed at which fruit will ripen on this tree.
	 */
	public static final IFloatChromosome SAPPINESS = new FloatChromosome(forestry("sappiness"));
	/**
	 * Leaf effect. Unused.
	 */
	public static final IValueChromosome<IAlleleLeafEffect> EFFECT = new ValueChromosome<>(forestry("effect"));
	/**
	 * Amount of random ticks which need to elapse before a sapling will grow into a tree.
	 */
	public static final IIntegerChromosome MATURATION = new IntegerChromosome(forestry("maturation"));
	public static final IIntegerChromosome GIRTH = new IntegerChromosome(forestry("girth"));
	/**
	 * Determines if the tree can burn.
	 */
	public static final IBooleanChromosome FIREPROOF = new BooleanChromosome(forestry("fireproof"));
}
