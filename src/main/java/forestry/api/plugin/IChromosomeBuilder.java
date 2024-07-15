package forestry.api.plugin;

import java.util.List;

public interface IChromosomeBuilder<A> {
	/**
	 * Add alleles as valid values for this chromosome.
	 */
	IChromosomeBuilder<A> addAlleles(List<A> values);
}
