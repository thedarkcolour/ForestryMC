package forestry.api.genetics.alleles;

import java.util.Set;

public non-sealed interface IBooleanAllele extends IAllele {
	Set<IBooleanAllele> VALUES = Set.of(ForestryAlleles.TRUE, ForestryAlleles.FALSE);
	Set<IBooleanAllele> ALL_VALUES = Set.of(ForestryAlleles.TRUE, ForestryAlleles.FALSE, ForestryAlleles.TRUE_RECESSIVE, ForestryAlleles.FALSE_RECESSIVE);

	boolean value();
}
