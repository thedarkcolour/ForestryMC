package forestry.core.genetics.alleles;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiFunction;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleManager;
import forestry.api.genetics.alleles.IAlleleNaming;
import forestry.api.genetics.alleles.IBooleanAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IFloatAllele;
import forestry.api.genetics.alleles.IFloatChromosome;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.genetics.alleles.IRegistryAllele;
import forestry.api.genetics.alleles.IRegistryAlleleValue;
import forestry.api.genetics.alleles.IRegistryChromosome;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;

import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class AlleleManager implements IAlleleManager {
	// primitive values to primitive allele
	private final Float2ObjectOpenHashMap<IFloatAllele> dominantFloatAlleles = new Float2ObjectOpenHashMap<>();
	private final Float2ObjectOpenHashMap<IFloatAllele> floatAlleles = new Float2ObjectOpenHashMap<>();
	private final Int2ObjectOpenHashMap<IIntegerAllele> dominantIntAlleles = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectOpenHashMap<IIntegerAllele> intAlleles = new Int2ObjectOpenHashMap<>();
	private final IBooleanAllele[] booleanAlleles = new BooleanAllele[4];
	// value to allele
	private final HashMap<?, IValueAllele<?>> dominantValueAlleles = new HashMap<>();
	private final HashMap<?, IValueAllele<?>> valueAlleles = new HashMap<>();
	// id to allele
	private final LinkedHashMap<ResourceLocation, IAllele> allelesByName = new LinkedHashMap<>();
	private final HashMap<ResourceLocation, IChromosome<?>> chromosomes = new HashMap<>();
	private final Codec<IAllele> alleleCodec;
	private final Codec<IChromosome<?>> chromosomeCodec;

	public AlleleManager() {
		// the flat* codec methods allow for error handling
		this.alleleCodec = ResourceLocation.CODEC.flatXmap(id -> {
			IAllele allele = getAllele(id);
			if (allele != null) {
				return DataResult.success(allele);
			} else {
				return DataResult.error("Unknown allele: " + id);
			}
		}, allele -> DataResult.success(allele.alleleId()));

		this.chromosomeCodec = ResourceLocation.CODEC.flatXmap(id -> {
			IChromosome<?> chromosome = getChromosome(id);
			if (chromosome != null) {
				return DataResult.success(chromosome);
			} else {
				return DataResult.error("Unknown chromosome: " + id);
			}
		}, chromosome -> DataResult.success(chromosome.id()));
	}

	@Override
	public IBooleanAllele booleanAllele(boolean value, boolean dominant) {
		// 0 = FF, 1 = FT, 2 = TF, 3 = TT
		int index = (value ? (dominant ? 3 : 2) : (dominant ? 1 : 0));
		IBooleanAllele allele = booleanAlleles[index];
		if (allele == null) {
			allele = new BooleanAllele(value, dominant);
			this.booleanAlleles[index] = allele;
			this.allelesByName.put(allele.alleleId(), allele);
		}
		return allele;
	}

	@Override
	public IIntegerAllele intAllele(int value, boolean dominant) {
		return (dominant ? this.dominantIntAlleles : this.intAlleles).computeIfAbsent(value, v -> {
			IntegerAllele allele = new IntegerAllele(v, dominant);
			this.allelesByName.put(allele.alleleId(), allele);
			return allele;
		});
	}

	@Override
	public IFloatAllele floatAllele(float value, boolean dominant) {
		return (dominant ? this.dominantFloatAlleles : this.floatAlleles).computeIfAbsent(value, v -> new FloatAllele(v, dominant));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <V> IValueAllele<V> valueAllele(V value, boolean dominant, IAlleleNaming<V> naming) {
		Preconditions.checkNotNull(value, "Allele value must not be null.");

		// Try to get existing by value first
		HashMap<?, IValueAllele<?>> valueToAlleleMap = (dominant ? this.dominantValueAlleles : this.valueAlleles);
		IValueAllele<?> valueExisting = valueToAlleleMap.get(value);

		if (valueExisting != null) {
			return (IValueAllele<V>) valueExisting;
		} else {
			// Then try to get existing by name
			ResourceLocation name = naming.getName(value, dominant);
			IAllele byName = this.allelesByName.get(name);

			if (byName != null) {
				if (byName instanceof IValueAllele<?> allele) {
					if (allele.value() != value) {
						throw new IllegalStateException("Tried to register two values with the same value allele ID: " + allele.value() + " and " + value + " under ID " + name);
					} else {
						return (IValueAllele<V>) allele;
					}
				} else {
					throw new IllegalStateException("Tried to register a value allele with ID " + name + " but an allele was already registered with type " + byName.getClass());
				}
			} else {
				// Create new allele
				IValueAllele<V> allele = new ValueAllele<>(name, value, dominant);
				((HashMap) valueToAlleleMap).put(value, allele);
				this.allelesByName.put(name, allele);

				return allele;
			}
		}
	}

	@Override
	public Codec<IAllele> alleleCodec() {
		// should we offer a compressed codec as well? it would only be used in JsonOps.COMPRESSED
		return this.alleleCodec;
	}

	@Override
	public Codec<IChromosome<?>> chromosomeCodec() {
		return this.chromosomeCodec;
	}

	@Override
	@Nullable
	public IAllele getAllele(ResourceLocation id) {
		return this.allelesByName.get(id);
	}

	@Nullable
	private IChromosome<?> getChromosome(ResourceLocation id) {
		return this.chromosomes.get(id);
	}

	@Override
	public IFloatChromosome floatChromosome(ResourceLocation id) {
		return (IFloatChromosome) this.chromosomes.computeIfAbsent(id, FloatChromosome::new);
	}

	@Override
	public IIntegerChromosome intChromosome(ResourceLocation id) {
		return (IIntegerChromosome) this.chromosomes.computeIfAbsent(id, IntegerChromosome::new);
	}

	@Override
	public IBooleanChromosome booleanChromosome(ResourceLocation id) {
		return (IBooleanChromosome) this.chromosomes.computeIfAbsent(id, BooleanChromosome::new);
	}

	@Override
	public <V> IValueChromosome<V> valueChromosome(ResourceLocation id, Class<V> valueClass) {
		return registerValueChromosome(id, valueClass, ValueChromosome::new);
	}

	@Override
	public <S extends ISpecies<?>> ISpeciesChromosome<S> speciesChromosome(ResourceLocation id, Class<S> speciesClass) {
		return registerValueChromosome(id, speciesClass, SpeciesChromosome::new);
	}

	@Override
	public <V extends IRegistryAlleleValue> IRegistryChromosome<V> registryChromosome(ResourceLocation id, Class<V> valueClass) {
		return registerValueChromosome(id, valueClass, RegistryChromosome::new);
	}

	@Override
	public <V extends IRegistryAlleleValue> IRegistryAllele<V> registryAllele(ResourceLocation id, IRegistryChromosome<V> chromosome) {
		return null;
	}

	private <V, C extends IValueChromosome<V>> C registerValueChromosome(ResourceLocation id, Class<V> valueClass, BiFunction<ResourceLocation, Class<V>, C> factory) {
		@SuppressWarnings("unchecked")
		C existing = (C) this.chromosomes.get(id);

		if (existing == null) {
			// Create new chromosome
			C chromosome = factory.apply(id, valueClass);
			this.chromosomes.put(id, chromosome);
			return chromosome;
		} else if (existing.valueClass().equals(valueClass)) {
			// Return existing equivalent chromosome
			return existing;
		} else {
			// Crash in case of conflicting chromosome.
			throw new IllegalStateException("A chromosome is already registered with ID " + id + " with a different value type: " + existing.valueClass() + " was registered, but tried register again with valueClass: " + valueClass);
		}
	}
}
