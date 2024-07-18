package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IFloatAllele;
import forestry.api.genetics.alleles.IFloatChromosome;

public record FloatChromosome(ResourceLocation id) implements IFloatChromosome {
	@Override
	public Class<?> valueClass() {
		return float.class;
	}
}
