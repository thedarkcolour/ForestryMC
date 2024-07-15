package genetics.root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IIndividual;
import genetics.api.individual.IKaryotype;
import forestry.api.genetics.IMutation;
import genetics.api.mutation.IMutationContainer;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.ComponentKeys;

import genetics.utils.AlleleUtils;

public class MutationContainer<I extends IIndividual, M extends IMutation> implements IMutationContainer<I, M> {

	private final List<M> mutations = new LinkedList<>();
	private final ISpeciesType<I> root;

	public MutationContainer(ISpeciesType<I> root) {
		this.root = root;
	}

	@Override
	public ISpeciesType<I> getRoot() {
		return root;
	}

	@Override
	public boolean registerMutation(M mutation) {
		IChromosome speciesType = root.getKaryotype().getSpeciesChromosome();
		IAllele firstParent = mutation.getFirstParent();
		IAllele secondParent = mutation.getSecondParent();
		IAllele resultSpecies = mutation.getTemplate()[speciesType.ordinal()];
		if (AlleleUtils.isBlacklisted(resultSpecies)
			|| AlleleUtils.isBlacklisted(firstParent)
			|| AlleleUtils.isBlacklisted(secondParent)) {
			return false;
		}
		mutations.add(mutation);
		return true;
	}

	@Override
	public List<M> getMutations(boolean shuffle) {
		if (shuffle) {
			Collections.shuffle(mutations);
		}
		return mutations;
	}

	@Override
	public List<M> getCombinations(IAllele other) {
		List<M> combinations = new ArrayList<>();
		for (M mutation : getMutations(false)) {
			if (mutation.isPartner(other)) {
				combinations.add(mutation);
			}
		}

		return combinations;
	}

	@Override
	public List<M> getResultantMutations(IAllele other) {
		IKaryotype karyotype = root.getKaryotype();
		List<M> resultants = new ArrayList<>();
		IChromosome iChromosomeType = karyotype.getSpeciesChromosome();
		int speciesIndex = iChromosomeType.ordinal();
		for (M mutation : getMutations(false)) {
			IAllele[] template = mutation.getTemplate();
			if (template.length <= speciesIndex) {
				continue;
			}
			IAllele speciesAllele = template[speciesIndex];
			if (speciesAllele == other) {
				resultants.add(mutation);
			}
		}

		return resultants;
	}

	@Override
	public List<M> getCombinations(IAlleleSpecies parentFirst, IAlleleSpecies parentSecond, boolean shuffle) {
		List<M> combinations = new ArrayList<>();

		ResourceLocation parentSpecies = parentSecond.getId();
		for (M mutation : getMutations(shuffle)) {
			if (mutation.isPartner(parentFirst)) {
				IAllele partner = mutation.getPartner(parentFirst);
				if (partner.id().equals(parentSpecies)) {
					combinations.add(mutation);
				}
			}
		}

		return combinations;
	}

	@Override
	public Collection<M> getPaths(IAllele result, IChromosome chromosomeType) {
		ArrayList<M> paths = new ArrayList<>();
		for (M mutation : getMutations(false)) {
			IAllele[] template = mutation.getTemplate();
			IAllele mutationResult = template[chromosomeType.ordinal()];
			if (mutationResult == result) {
				paths.add(mutation);
			}
		}

		return paths;
	}

	@Override
	public ComponentKey<IMutationContainer> getKey() {
		return ComponentKeys.MUTATIONS;
	}
}
