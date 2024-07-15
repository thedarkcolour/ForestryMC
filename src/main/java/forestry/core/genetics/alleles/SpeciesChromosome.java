package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.alleles.IValueAllele;

public class SpeciesChromosome<S extends ISpeciesType<?>> extends ValueChromosome<S> implements ISpeciesChromosome<S> {
	public SpeciesChromosome(ResourceLocation id) {
		super(id);
	}

	@Override
	public String getTranslationKey(IValueAllele<S> allele) {
		return allele.value().getTranslationKey();
	}
}
