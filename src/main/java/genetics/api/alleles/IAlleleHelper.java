package genetics.api.alleles;

import javax.annotation.Nullable;

import genetics.api.individual.IChromosomeType;

public interface IAlleleHelper {
	@Nullable
	<V> IAlleleValue<V> getAllele(IChromosomeType chromosomeType, V value);

	@Nullable
	<V> IAlleleValue<V> getAllele(IAlleleData<V> alleleData);
}
