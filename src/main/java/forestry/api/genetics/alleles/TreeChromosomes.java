package forestry.api.genetics.alleles;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ILeafEffect;
import forestry.core.genetics.alleles.FloatChromosome;
import forestry.core.genetics.alleles.IntegerChromosome;
import forestry.core.genetics.alleles.SpeciesChromosome;
import forestry.core.genetics.alleles.ValueChromosome;

import static forestry.api.ForestryConstants.forestry;

public class TreeChromosomes {
	/**
	 * The species of a tree.
	 */
	public static final ISpeciesChromosome<ITreeSpecies> SPECIES = new SpeciesChromosome<>(forestry("species"), ITreeSpecies.class);
	/**
	 * Modifies the height of a tree.
	 */
	public static final IFloatChromosome HEIGHT = new FloatChromosome(forestry("height"));
	/**
	 * Chance for saplings.
	 */
	public static final IFloatChromosome SAPLINGS = new FloatChromosome(forestry("saplings"));
	/**
	 * Determines what fruits are grown on the tree.
	 */
	public static final IValueChromosome<IFruit> FRUITS = new ValueChromosome<>(forestry("fruits"), IFruit.class);
	/**
	 * Chance for fruit leaves and/or drops.
	 */
	public static final IFloatChromosome YIELD = new FloatChromosome(forestry("yield"));
	/**
	 * Determines the speed at which fruit will ripen on this tree.
	 */
	public static final IFloatChromosome SAPPINESS = new FloatChromosome(forestry("sappiness"));
	/**
	 * Leaf effect. All trees added by base Forestry have {@link ForestryAlleles#TREE_EFFECT_NONE}.
	 */
	public static final IValueChromosome<ILeafEffect> EFFECT = new ValueChromosome<>(forestry("effect"), ILeafEffect.class);
	/**
	 * Amount of random ticks which need to elapse before a sapling will grow into a tree.
	 */
	public static final IIntegerChromosome MATURATION = new IntegerChromosome(forestry("maturation"));
	/**
	 * The diameter of the tree. If the allele is 2, then the tree trunk is a 2x2 and requires four saplings to grow.
	 */
	public static final IIntegerChromosome GIRTH = new IntegerChromosome(forestry("girth"));
	/**
	 * Determines if the tree can burn.
	 */
	public static final IBooleanChromosome FIREPROOF = ButterflyChromosomes.FIREPROOF;
}
