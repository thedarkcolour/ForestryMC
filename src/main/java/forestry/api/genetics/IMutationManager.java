package forestry.api.genetics;

import java.util.List;

/**
 * Keeps track of mutations involving members of a certain species type.
 *
 * @param <S> The type of species these mutations concern.
 */
public interface IMutationManager<S extends ISpecies<?>> {
	/**
	 * @return A list of mutations that this species is one of the parents for.
	 */
	List<? extends IMutation<S>> getMutationsFrom(S species);

	/**
	 * @return A list of mutations that this species is the result of.
	 */
	List<? extends IMutation<S>> getMutationsInto(S species);

	/**
	 * @return A list of mutations between these two parents.
	 */
	List<? extends IMutation<S>> getCombinations(S firstParent, S secondParent);

	/**
	 * @return All mutations within the species type of this mutation manager.
	 */
	List<? extends IMutation<S>> getAllMutations();
}
