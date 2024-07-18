package forestry.core.genetics;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.ISpeciesChromosome;

import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.plugin.IChromosomeBuilder;
import forestry.api.plugin.IKaryotypeBuilder;

public class Karyotype implements IKaryotype {
	private final ImmutableMap<IChromosome<?>, ImmutableSet<? extends IAllele>> chromosomes;
	private final ISpeciesChromosome<?> speciesChromosome;
	private Genome defaultGenome;
	private final Codec<Genome> genomeCodec;

	public Karyotype(ImmutableMap<IChromosome<?>, ImmutableSet<? extends IAllele>> chromosomes, Genome defaultGenome) {
		this(chromosomes);
		this.defaultGenome = defaultGenome;
	}

	// Used in Karyotype.Builder
	private Karyotype(ImmutableMap<IChromosome<?>, ImmutableSet<? extends IAllele>> chromosomes) {
		this.chromosomes = chromosomes;
		this.speciesChromosome = (ISpeciesChromosome<?>) chromosomes.keySet().asList().get(0);

		Keyable chromosomesKeyable = Keyable.forStrings(() -> this.chromosomes.keySet().stream().map(chromosome -> chromosome.id().toString()));
		this.genomeCodec = Codec.simpleMap(IChromosome.CODEC, AllelePair.CODEC, chromosomesKeyable)
				.xmap(map -> new Genome(this, map), genome -> genome.chromosomes).codec();
	}

	@Override
	public ImmutableList<IChromosome<?>> getChromosomes() {
		// asList caches the returned list, no allocations to worry about
		return this.chromosomes.keySet().asList();
	}

	@Override
	public boolean contains(IChromosome<?> chromosome) {
		return this.chromosomes.containsKey(chromosome);
	}

	@Override
	public ISpeciesChromosome<?> getSpeciesChromosome() {
		return this.speciesChromosome;
	}

	@Override
	public int size() {
		return this.chromosomes.size();
	}

	@Override
	public IGenome getDefaultGenome() {
		return this.defaultGenome;
	}

	@Override
	public <A extends IAllele> boolean isAlleleValid(IChromosome<A> chromosome, A allele) {
		ImmutableSet<? extends IAllele> validAlleles = this.chromosomes.get(chromosome);
		return validAlleles != null && validAlleles.contains(allele);
	}

	@Override
	public Codec<Genome> genomeCodec() {
		return this.genomeCodec;
	}

	public static class Builder implements IKaryotypeBuilder {
		private final LinkedHashMap<IChromosome<?>, ChromosomeBuilder<?>> chromosomes = new LinkedHashMap<>();
		@Nullable
		private ISpeciesChromosome<?> speciesChromosome;
		@Nullable
		private ResourceLocation defaultSpeciesId;

		@Override
		public void setSpecies(ISpeciesChromosome<? extends ISpecies<?>> species, ResourceLocation defaultId) {
			if (this.speciesChromosome != null && this.speciesChromosome != species) {
				throw new IllegalStateException("The species chromosome for this karyotype has already been set: " + this.speciesChromosome.id() + ", but tried setting to " + species.id());
			} else {
				this.speciesChromosome = species;
				this.defaultSpeciesId = defaultId;
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public <A extends IAllele> IChromosomeBuilder<A> get(IChromosome<A> chromosome) {
			return (IChromosomeBuilder<A>) this.chromosomes.computeIfAbsent(chromosome, key -> new ChromosomeBuilder<A>(chromosome));
		}

		public Karyotype buildKaryotype() {
			Preconditions.checkState(this.defaultSpeciesId != null && this.speciesChromosome != null, "IKaryotypeBuilder is missing a species chromosome.");

			ImmutableMap.Builder<IChromosome<?>, ImmutableSet<? extends IAllele>> map = ImmutableMap.builderWithExpectedSize(this.chromosomes.size());
			ImmutableMap.Builder<IChromosome<?>, AllelePair<?>> genome = ImmutableMap.builder();

			for (Map.Entry<IChromosome<?>, ChromosomeBuilder<?>> entry : chromosomes.entrySet()) {
				IChromosome<?> chromosome = entry.getKey();
				ChromosomeBuilder<?> builder = entry.getValue();
				map.put(chromosome, builder.alleles.build());
				genome.put(chromosome, AllelePair.both(builder.defaultAllele));
			}
			Karyotype karyotype = new Karyotype(map.build());
			karyotype.defaultGenome = new Genome(karyotype, genome);
			return karyotype;
		}
	}
}
