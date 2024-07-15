package forestry.core.genetics.alleles;

import java.util.Set;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IBooleanAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;

public class BooleanChromosome extends AbstractChromosome<IBooleanAllele> implements IBooleanChromosome {
	public BooleanChromosome(ResourceLocation id) {
		this(id, false);
	}

	public BooleanChromosome(ResourceLocation id, boolean allowRecessive) {
		super(id);

		this.allowedAlleles = allowRecessive ? IBooleanAllele.ALL_VALUES : IBooleanAllele.VALUES;
	}

	@Override
	public void setAllowedAlleles(Set<IBooleanAllele> alleles) {
		// NO OP
	}

	@Override
	public String getTranslationKey(IBooleanAllele allele) {
		return allele.value() ? "allele.forestry.true" : "allele.forestry.false";
	}
}
