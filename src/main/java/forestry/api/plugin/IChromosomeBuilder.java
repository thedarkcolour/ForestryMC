package forestry.api.plugin;

import java.util.List;

import forestry.api.genetics.alleles.IAllele;

/**
 * Interface for customizing a default chromosome in a karyotype.
 * @param <A>
 */
public interface IChromosomeBuilder<A extends IAllele> {
	/**
	 * Add alleles as valid values for this chromosome.
	 */
	IChromosomeBuilder<A> addAlleles(List<A> alleles);

	/**
	 * Override the default value of this chromosome that was previously set in {@link IKaryotypeBuilder#set}.
	 */
	IChromosomeBuilder<A> setDefault(A allele);
}
