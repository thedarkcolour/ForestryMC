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

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.plugin.IChromosomeBuilder;
import forestry.api.plugin.IKaryotypeBuilder;

public class Karyotype implements IKaryotype {
	private final ImmutableMap<IChromosome<?>, ImmutableSet<? extends IAllele>> chromosomes;
	private final ISpeciesChromosome<?> speciesChromosome;
	private final ImmutableMap<IChromosome<?>, ? extends IAllele> defaultAlleles;
	private final ResourceLocation defaultSpecies;
	private final Codec<Genome> genomeCodec;

	// Used in Karyotype.Builder
	public Karyotype(ImmutableMap<IChromosome<?>, ImmutableSet<? extends IAllele>> chromosomes, ImmutableMap<IChromosome<?>, ? extends IAllele> defaultAlleles, ResourceLocation defaultSpecies) {
		this.chromosomes = chromosomes;
		this.speciesChromosome = (ISpeciesChromosome<?>) chromosomes.keySet().asList().get(0);
		this.defaultAlleles = defaultAlleles;
		this.defaultSpecies = defaultSpecies;

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
	public <A extends IAllele> boolean isAlleleValid(IChromosome<A> chromosome, A allele) {
		ImmutableSet<? extends IAllele> validAlleles = this.chromosomes.get(chromosome);
		return validAlleles != null && validAlleles.contains(allele);
	}

	@Override
	public <A extends IAllele> boolean isChromosomeValid(IChromosome<A> chromosome) {
		return this.chromosomes.containsKey(chromosome);
	}

	@Override
	public <A extends IAllele> A getDefaultAllele(IChromosome<A> chromosome) {
		A allele = (A) this.defaultAlleles.get(chromosome);
		if (allele == null) {
			throw new IllegalArgumentException("Chromosome is not valid");
		}
		return allele;
	}

	@Override
	public ImmutableMap<IChromosome<?>, ? extends IAllele> getDefaultAlleles() {
		return this.defaultAlleles;
	}

	@Override
	public ResourceLocation getDefaultSpecies() {
		return this.defaultSpecies;
	}

	@Override
	public Codec<Genome> getGenomeCodec() {
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

		public Karyotype build() {
			Preconditions.checkState(this.defaultSpeciesId != null && this.speciesChromosome != null, "IKaryotypeBuilder is missing a species chromosome.");

			ImmutableMap.Builder<IChromosome<?>, ImmutableSet<? extends IAllele>> permittedAlleles = ImmutableMap.builderWithExpectedSize(this.chromosomes.size());
			ImmutableMap.Builder<IChromosome<?>, IAllele> defaultAlleles = ImmutableMap.builderWithExpectedSize(this.chromosomes.size());

			for (Map.Entry<IChromosome<?>, ChromosomeBuilder<?>> entry : this.chromosomes.entrySet()) {
				IChromosome<?> chromosome = entry.getKey();
				ChromosomeBuilder<?> builder = entry.getValue();
				permittedAlleles.put(chromosome, builder.alleles.build());
				defaultAlleles.put(chromosome, builder.defaultAllele);
			}
			return new Karyotype(permittedAlleles.build(), defaultAlleles.build(), this.defaultSpeciesId);
		}
	}
}
