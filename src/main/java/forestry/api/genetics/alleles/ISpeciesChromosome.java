package forestry.api.genetics.alleles;

import forestry.api.genetics.ISpecies;

/**
 * Special chromosome type used in certain method overloads to reduce boilerplate.
 */
public interface ISpeciesChromosome<S extends ISpecies<?>> extends IValueChromosome<S> {
}
