package forestry.api.genetics.alleles;

import forestry.api.genetics.ISpeciesType;

/**
 * Special chromosome type used in certain method overloads to reduce boilerplate.
 */
public interface ISpeciesChromosome<S extends ISpeciesType<?>> extends IValueChromosome<S> {
}
