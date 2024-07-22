package forestry.api.genetics.filter;

import genetics.api.individual.IIndividual;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

public interface IFilterData {

	/**
	 * If the root is present, returns the root,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	ISpeciesType<?> root();

	/**
	 * If the individual is present, returns the individual,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	IIndividual individual();

	/**
	 * If the type is present, returns the type,
	 * otherwise throws {@code NoSuchElementException}.
	 */
	ILifeStage type();

	/**
	 * @return True if this data contains a root, individual and type.
	 */
	boolean isPresent();
}
