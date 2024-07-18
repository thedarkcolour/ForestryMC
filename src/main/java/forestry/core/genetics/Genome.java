package forestry.core.genetics;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;
import forestry.api.plugin.IGenomeBuilder;

import genetics.utils.AlleleUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class Genome implements IGenome {
	final ImmutableMap<IChromosome<?>, AllelePair<?>> chromosomes;
	private final IKaryotype karyotype;

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
	public <A extends IAllele> A getActiveAllele(IChromosome<A> chromosomeType) {
		AllelePair<A> chromosome = getChromosome(chromosomeType);
		return chromosome.active();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> IValueAllele<V> getActiveAllele(IValueChromosome<V> chromosome) {
		return (IValueAllele<V>) getActiveAllele(chromosome, IValueAllele.class);
	}

	@Override
	public <V> V getActiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass) {
		IAllele allele = getActiveAllele(chromosome);
		V value = AlleleUtils.getAlleleValue(allele, valueClass);
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' has no value of the type '%s'.", allele, chromosome, valueClass));
		}
		return value;
	}

	@Override
	public <V> V getActiveValue(IValueChromosome<V> chromosome, Class<? extends V> valueClass, V fallback) {
		IAllele allele = getActiveAllele(chromosome);
		return AlleleUtils.getAlleleValue(allele, valueClass, fallback);
	}

	@Override
	public IKaryotype getKaryotype() {
		return this.karyotype;
	}

	@Override
	public int size() {
		return this.chromosomes.size();
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
