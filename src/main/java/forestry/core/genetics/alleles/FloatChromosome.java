package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IFloatAllele;
import forestry.api.genetics.alleles.IFloatChromosome;

public class FloatChromosome extends AbstractChromosome<IFloatAllele> implements IFloatChromosome {
	public FloatChromosome(ResourceLocation id) {
		super(id);
	}
}
