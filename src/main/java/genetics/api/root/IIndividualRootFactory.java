package genetics.api.root;

import forestry.api.genetics.ISpeciesType;

import genetics.api.individual.IIndividual;

public interface IIndividualRootFactory<I extends IIndividual, R extends ISpeciesType<I>> {
	/**
	 * Creates a new root.
	 * <p>
	 * Used by {@link IIndividualRootBuilder} to create the root object.
	 */
	R createRoot(IRootContext<I> context);
}
