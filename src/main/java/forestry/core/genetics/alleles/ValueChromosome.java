package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;

public class ValueChromosome<V> extends AbstractChromosome<IValueAllele<V>> implements IValueChromosome<V> {
	public ValueChromosome(ResourceLocation id) {
		super(id);
	}
}