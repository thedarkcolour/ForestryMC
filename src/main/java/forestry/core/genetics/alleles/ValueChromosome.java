package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;

public class ValueChromosome<V> implements IValueChromosome<V> {
	private final ResourceLocation id;
	private final Class<V> valueClass;

	public ValueChromosome(ResourceLocation id, Class<V> valueClass) {
		this.id = id;
		this.valueClass = valueClass;
	}

	@Override
	public ResourceLocation id() {
		return this.id;
	}

	@Override
	public Class<?> valueClass() {
		return this.valueClass;
	}
}