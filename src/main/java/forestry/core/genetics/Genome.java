package forestry.core.genetics;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.mutable.MutableBoolean;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.plugin.IGenomeBuilder;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class Genome implements IGenome {
	final ImmutableMap<IChromosome<?>, AllelePair<?>> chromosomes;
	private final IKaryotype karyotype;

	private boolean isDefaultGenome;
	private boolean hasCachedDefaultGenome;

	public Genome(IKaryotype karyotype, Map<IChromosome<?>, AllelePair<?>> chromosomes) {
		this(karyotype, ImmutableMap.copyOf(chromosomes));
	}

	public Genome(IKaryotype karyotype, ImmutableMap<IChromosome<?>, AllelePair<?>> chromosomes) {
		this.karyotype = karyotype;
		this.chromosomes = chromosomes;
	}

	@Override
	public ImmutableList<AllelePair<?>> getAllelePairs() {
		return this.chromosomes.values().asList();
	}

	@Override
	public IKaryotype getKaryotype() {
		return this.karyotype;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A extends IAllele> AllelePair<A> getAllelePair(IChromosome<A> chromosomeType) {
		return (AllelePair<A>) Objects.requireNonNull(this.chromosomes.get(chromosomeType));
	}

	@Override
	public boolean isDefaultGenome() {
		if (!this.hasCachedDefaultGenome) {
			Genome defaultGenome = (Genome) getActiveValue(this.karyotype.getSpeciesChromosome()).getDefaultGenome();

			this.isDefaultGenome = this == defaultGenome || (isSameAlleles(defaultGenome));
			this.hasCachedDefaultGenome = true;
		}

		return this.isDefaultGenome;
	}

	@Override
	public ImmutableMap<IChromosome<?>, AllelePair<?>> getChromosomes() {
		return this.chromosomes;
	}

	@Override
	public IGenome copyWith(Map<IChromosome<?>, IAllele> alleles) {
		IGenome genome;
		if (alleles.isEmpty()) {
			genome = this;
		} else {
			Genome.Builder builder = new Genome.Builder(getKaryotype());
			// avoid duplicate instances of default genome
			MutableBoolean isDefault = new MutableBoolean(true);

			// copy over allele map or default allele
			this.chromosomes.forEach((chromosome, pair) -> {
				IAllele allele = alleles.get(chromosome);

				if (allele == null || allele.equals(pair.active())) {
					// copy default allele if missing or equal
					builder.setUnchecked(chromosome, pair);
				} else {
					// mark not default when there are non-default alleles
					builder.setUnchecked(chromosome, new AllelePair<>(allele, allele));
					isDefault.setFalse();
				}
			});

			if (isDefault.isTrue()) {
				genome = this;
			} else {
				genome = builder.build();
			}
		}

		return genome;
	}

	@Override
	public boolean isSameAlleles(IGenome other) {
		return other.getKaryotype() == this.karyotype && this.chromosomes.equals(other.getChromosomes());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Genome genome = (Genome) o;

		if (!chromosomes.equals(genome.chromosomes)) {
			return false;
		}
		return karyotype.equals(genome.karyotype);
	}

	@Override
	public int hashCode() {
		int result = chromosomes.hashCode();
		result = 31 * result + karyotype.hashCode();
		return result;
	}

	public static class Builder implements IGenomeBuilder {
		private final IKaryotype karyotype;
		private final Object2ObjectOpenHashMap<IChromosome<?>, IAllele> active = new Object2ObjectOpenHashMap<>();
		private final Object2ObjectOpenHashMap<IChromosome<?>, IAllele> inactive = new Object2ObjectOpenHashMap<>();

		public Builder(IKaryotype karyotype) {
			Preconditions.checkNotNull(karyotype);

			this.karyotype = karyotype;
		}

		@Override
		public <A extends IAllele> void set(IChromosome<A> chromosome, A allele) {
			if (this.karyotype.isAlleleValid(chromosome, allele)) {
				this.active.put(chromosome, allele);
				this.inactive.put(chromosome, allele);
			}
		}

		@Override
		public <A extends IAllele> void setActive(IChromosome<A> chromosome, A allele) {
			if (this.karyotype.isAlleleValid(chromosome, allele)) {
				this.active.put(chromosome, allele);
			}
		}

		@Override
		public <A extends IAllele> void setInactive(IChromosome<A> chromosome, A allele) {
			if (this.karyotype.isAlleleValid(chromosome, allele)) {
				this.inactive.put(chromosome, allele);
			}
		}

		@Override
		public boolean isEmpty() {
			return this.inactive.isEmpty() && this.active.isEmpty();
		}

		@Override
		public IGenome build() {
			// Check for missing chromosomes and display which ones are missing
			if (this.karyotype.size() != this.active.size()) {
				StringBuilder msg = new StringBuilder("Tried to build genome, but the following chromosomes are missing from the karyotype: { ");

				for (IChromosome<?> chromosome : this.karyotype.getChromosomes()) {
					if (!this.active.containsKey(chromosome)) {
						msg.append(chromosome.id());
						msg.append(' ');
					}
				}
				msg.append('}');

				throw new IllegalStateException(msg.toString());
			} else {
				ImmutableMap.Builder<IChromosome<?>, AllelePair<?>> genome = new ImmutableMap.Builder<>();

				for (IChromosome<?> chromosome : this.karyotype.getChromosomes()) {
					IAllele active = this.active.get(chromosome);
					IAllele inactive = this.inactive.get(chromosome);

					// Check for incomplete pairs
					if (active == null || inactive == null) {
						throw new IllegalStateException("Tried to build genome, but the allele pair was incomplete for the following chromosome: " + chromosome.id());
					} else {
						genome.put(chromosome, new AllelePair<>(active, inactive));
					}
				}

				return new Genome(this.karyotype, genome.build());
			}
		}
	}
}
