package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.alleles.IValueAllele;

public class SpeciesChromosome<S extends ISpecies<?>> extends RegistryChromosome<S> implements ISpeciesChromosome<S> {
	public SpeciesChromosome(ResourceLocation id, Class<S> valueClass) {
		super(id, valueClass);
	}

	@Override
	public String getTranslationKey(IValueAllele<S> allele) {
		return allele.value().getTranslationKey();
	}
}
