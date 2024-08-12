package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IBooleanAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;

public record BooleanChromosome(ResourceLocation id) implements IBooleanChromosome {
	@Override
	public String getTranslationKey(IBooleanAllele allele) {
		return allele.value() ? "allele.forestry.true" : "allele.forestry.false";
	}

	@Override
	public Class<?> valueClass() {
		return boolean.class;
	}
}
