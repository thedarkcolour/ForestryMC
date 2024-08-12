package forestry.api.plugin;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IKaryotype;

public interface ISpeciesTypeFactory {
	ISpeciesType<?, ?> create(IKaryotype karyotype, ISpeciesTypeBuilder builder);
}
