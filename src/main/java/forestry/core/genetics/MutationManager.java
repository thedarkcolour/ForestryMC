package forestry.core.genetics;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import forestry.api.genetics.IMutation;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.ISpecies;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MutationManager<S extends ISpecies<?>> implements IMutationManager<S> {
	private final IdentityHashMap<S, List<IMutation<S>>> mutationsFrom = new IdentityHashMap<>();
	private final IdentityHashMap<S, List<IMutation<S>>> mutationsInto = new IdentityHashMap<>();
	private final ImmutableList<IMutation<S>> mutations;

	// Assume that if mutations contains (A, B) -> C, that it will never contain (B, A) -> C
	public MutationManager(ImmutableList<IMutation<S>> mutations) {
		this.mutations = mutations;

		for (IMutation<S> mutation : mutations) {
			// for mutation (A, B) -> C
			// mutationsFrom.get(A) will contain the mutation
			this.mutationsFrom.computeIfAbsent(mutation.getFirstParent(), k -> new ArrayList<>())
					.add(mutation);
			// mutationsFrom.get(B) will also contain the mutation
			this.mutationsFrom.computeIfAbsent(mutation.getSecondParent(), k -> new ArrayList<>())
					.add(mutation);
			// mutationsInto.get(C) will contain the mutation
			this.mutationsInto.computeIfAbsent(mutation.getResult(), k -> new ArrayList<>())
					.add(mutation);
		}
	}

	@Override
	public List<IMutation<S>> getMutationsFrom(S species) {
		return this.mutationsFrom.getOrDefault(species, List.of());
	}

	@Override
	public List<IMutation<S>> getMutationsInto(S species) {
		return this.mutationsInto.getOrDefault(species, List.of());
	}

	@Override
	public ObjectArrayList<IMutation<S>> getCombinations(S firstParent, S secondParent) {
		ObjectArrayList<IMutation<S>> mutations = new ObjectArrayList<>();

		for (IMutation<S> mutation : this.mutationsFrom.get(firstParent)) {
			if (mutation.isPartner(secondParent)) {
				mutations.add(mutation);
			}
		}

		return mutations;
	}

	@Override
	public List<IMutation<S>> getAllMutations() {
		return this.mutations;
	}

	@Override
	public List<? extends IMutation<S>> getCombinationsShuffled(S firstParent, S secondParent, RandomSource rand) {
		ObjectArrayList<? extends IMutation<S>> mutations = getCombinations(firstParent, secondParent);
		Util.shuffle(mutations, rand);
		return mutations;
	}
}
