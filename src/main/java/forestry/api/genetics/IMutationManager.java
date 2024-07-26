package forestry.api.genetics;

import java.util.List;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Keeps track of mutations involving members of a certain species type.
 *
 * @param <S> The type of species these mutations concern.
 */
public interface IMutationManager<S extends ISpecies<?>> {
	/**
	 * @return A list of mutations that this species is one of the parents for.
	 */
	List<IMutation<S>> getMutationsFrom(S species);

	/**
	 * @return A list of mutations that this species is the result of.
	 */
	List<IMutation<S>> getMutationsInto(S species);

	/**
	 * @return A list of mutations between these two parents.
	 */
	List<IMutation<S>> getCombinations(S firstParent, S secondParent);

	/**
	 * @return All mutations within the species type of this mutation manager.
	 */
	List<IMutation<S>> getAllMutations();

	/**
	 * @return A shuffled list of all mutations within the species type of this mutation manager.
	 */
	default ObjectArrayList<? extends IMutation<S>> getAllMutations(RandomSource rand) {
		ObjectArrayList<? extends IMutation<S>> mutations = new ObjectArrayList<>(getAllMutations());
		Util.shuffle(mutations, rand);
		return mutations;
	}
}
