package forestry.core.genetics.alleles;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IRegistryAllele;
import forestry.api.genetics.alleles.IRegistryAlleleValue;
import forestry.api.genetics.alleles.IRegistryChromosome;

public class RegistryAllele<V extends IRegistryAlleleValue> implements IRegistryAllele<V> {
	private final ResourceLocation id;
	private final IRegistryChromosome<V> chromosome;
	@Nullable
	private V value;

	// Do not call directly, use IAlleleManager.registryAllele
	RegistryAllele(ResourceLocation id, IRegistryChromosome<V> chromosome) {
		this.id = id;
		this.chromosome = chromosome;
		((RegistryChromosome<V>) chromosome).add(id, this);
	}

	@Override
	public ResourceLocation alleleId() {
		return this.id;
	}

	@Override
	public boolean dominant() {
		return value().isDominant();
	}

	@Override
	public V value() {
		if (this.value == null) {
			this.value = this.chromosome.get(this.id);
		}

		return this.value;
	}

	@Override
	public IRegistryChromosome<V> chromosome() {
		return this.chromosome;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + '[' + this.id + ']';
	}
}
