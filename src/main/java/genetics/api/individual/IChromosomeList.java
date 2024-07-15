package genetics.api.individual;

import java.util.Collection;

import forestry.api.genetics.alleles.IChromosome;

import forestry.api.genetics.ISpeciesType;

public interface IChromosomeList extends Iterable<IChromosome> {
	IChromosomeTypeBuilder builder();

	Collection<IChromosome> types();

	int size();

	String getUID();

	ISpeciesType<?> getRoot();

	IChromosome[] typesArray();
}
