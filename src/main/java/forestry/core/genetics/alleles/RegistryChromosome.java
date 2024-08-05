package forestry.core.genetics.alleles;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IRegistryAlleleValue;
import forestry.api.genetics.alleles.IRegistryChromosome;

public class RegistryChromosome<V extends IRegistryAlleleValue> extends ValueChromosome<V> implements IRegistryChromosome<V> {
	@Nullable
	private ImmutableMap<ResourceLocation, V> registry;
	@Nullable
	private IdentityHashMap<V, ResourceLocation> reverseLookup;

	public RegistryChromosome(ResourceLocation id, Class<V> valueClass) {
		super(id, valueClass);
	}

	@Override
	public boolean isValidAllele(IAllele allele) {
		Preconditions.checkState(this.registry != null, "Registry not yet populated");
		return this.registry.containsKey(allele.alleleId());
	}

	@Override
	public V get(ResourceLocation id) {
		Preconditions.checkState(this.registry != null, "Registry not yet populated");
		V value = this.registry.get(id);
		if (value == null) {
			throw new RuntimeException("No allele registered for chromosome " + this.id + " with ID: " + id);
		}
		return Objects.requireNonNull(value, "No ");
	}

	@Override
	public Collection<V> values() {
		Preconditions.checkState(this.registry != null, "Registry not yet populated");

		return this.registry.values();
	}

	@Override
	public ResourceLocation getId(V value) {
		Preconditions.checkState(this.reverseLookup != null, "Registry not yet populated");

		return this.reverseLookup.get(value);
	}

	@Override
	public void populate(ImmutableMap<ResourceLocation, V> registry) {
		Preconditions.checkState(this.registry == null, "Registry has already been populated");

		this.registry = registry;
		this.reverseLookup = new IdentityHashMap<>(registry.size());

		for (Map.Entry<ResourceLocation, V> entry : registry.entrySet()) {
			this.reverseLookup.put(entry.getValue(), entry.getKey());
		}
	}

	@Override
	public boolean isPopulated() {
		return this.registry != null;
	}
}
