package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IIntegerChromosome;

public class IntegerChromosome extends AbstractChromosome<IIntegerAllele> implements IIntegerChromosome {
	public IntegerChromosome(ResourceLocation id) {
		super(id);
	}

	@Override
	public String getTranslationKey(IIntegerAllele allele) {
		return null;
	}
}
