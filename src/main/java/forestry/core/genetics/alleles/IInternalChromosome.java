package forestry.core.genetics.alleles;

import java.util.Set;

import forestry.api.genetics.alleles.IAllele;

/**
 * Used by genetic registration to set up allowed values of a chromosome, including additions by other plugins.
 */
interface IInternalChromosome<A extends IAllele> {
	void setAllowedAlleles(Set<A> alleles);
}
