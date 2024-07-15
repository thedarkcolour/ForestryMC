package genetics.individual;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import java.util.Arrays;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import genetics.ApiInstance;
import genetics.api.GeneticsAPI;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.ChromosomePair;
import genetics.api.individual.IChromosomeAllele;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IChromosomeValue;
import forestry.api.genetics.IGenome;
import genetics.api.individual.IGenomeWrapper;
import genetics.api.individual.IKaryotype;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IValueChromosome;

import genetics.utils.AlleleUtils;

public final class Genome implements IGenome {
	private final ChromosomePair[] chromosomes;
	private final IKaryotype karyotype;

	private boolean calculatedTemplateMatch;
	private boolean templateMatches;

	public Genome(IKaryotype karyotype, ChromosomePair[] chromosomes) {
		this.karyotype = karyotype;
		checkChromosomes(chromosomes);
		this.chromosomes = chromosomes;
	}

	@Nullable
	public static IGenome fromNbt(IKaryotype karyotype, CompoundTag compound) {
		ChromosomePair[] chromosomes = GeneticSaveHandler.INSTANCE.readTag(karyotype, compound);
		return chromosomes == null ? null : new Genome(karyotype, chromosomes);
	}

	@SuppressWarnings("all")
	private void checkChromosomes(ChromosomePair[] chromosomes) {
		if (chromosomes.length != karyotype.getChromosomeTypes().length) {
			String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template.\n%s", karyotype.getId(), chromosomesToString(chromosomes));
			throw new IllegalArgumentException(message);
		}

		IAlleleRegistry registry = ApiInstance.INSTANCE.getAlleleRegistry();
		IChromosome[] chromosomeTypes = karyotype.getChromosomeTypes();
		for (int i = 0; i < chromosomeTypes.length; i++) {
			IChromosome chromosomeType = chromosomeTypes[i];
			ChromosomePair chromosome = chromosomes[i];
			if (chromosome == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing chromosome '%s'.\n%s", karyotype.getId(), chromosomeType.getId(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			IAllele primary = chromosome.active();
			if (primary == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing active allele for '%s'.\n%s", karyotype.getId(), chromosomeType.getId(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			IAllele secondary = chromosome.inactive();
			if (secondary == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing inactive allele for '%s'.\n%s", karyotype.getId(), chromosomeType.getId(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			if (!registry.isValidAllele(primary, chromosomeType)) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Incorrect type for active allele '%s'.\n%s.", karyotype.getId(), chromosomeType.getId(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			if (!registry.isValidAllele(secondary, chromosomeType)) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Incorrect type for inaktive allele '%s'.\n%s.", karyotype.getId(), chromosomeType.getId(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}
		}
	}

	private String chromosomesToString(ChromosomePair[] chromosomes) {
		StringBuilder stringBuilder = new StringBuilder();
		IChromosome[] chromosomeTypes = karyotype.getChromosomeTypes();
		for (int i = 0; i < chromosomes.length; i++) {
			IChromosome chromosomeType = chromosomeTypes[i];
			ChromosomePair chromosome = chromosomes[i];
			stringBuilder.append(chromosomeType.getId()).append(": ").append(chromosome).append("\n");
		}

		return stringBuilder.toString();
	}

	@Override
	public ChromosomePair[] getChromosomes() {
		return Arrays.copyOf(chromosomes, chromosomes.length);
	}

	@Override
	public <A extends IAlleleSpecies> A getPrimary(Class<? extends A> alleleClass) {
		return getActiveAllele(karyotype.getSpeciesChromosome(), alleleClass);
	}

	@Override
	public <A extends IAlleleSpecies> A getSecondary(Class<? extends A> alleleClass) {
		return getInactiveAllele(karyotype.getSpeciesChromosome(), alleleClass);
	}

	@Override
	public IAllele getActiveAllele(IChromosome chromosomeType) {
		ChromosomePair chromosome = getChromosome(chromosomeType);
		return chromosome.active();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> IValueAllele<V> getActiveAllele(IValueChromosome<V> chromosome) {
		return (IValueAllele<V>) getActiveAllele(chromosome, IValueAllele.class);
	}

	@Override
	public <V> V getActiveValue(IChromosomeValue<V> chromosomeType) {
		IAllele allele = getActiveAllele(chromosomeType);
		V value = AlleleUtils.getAlleleValue(allele, chromosomeType.getValueClass());
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' has no value.", allele, chromosomeType));
		}
		return value;
	}

	@Override
	public <V> V getActiveValue(IChromosomeValue<V> chromosomeType, V fallback) {
		IAllele allele = getActiveAllele(chromosomeType);
		return AlleleUtils.getAlleleValue(allele, chromosomeType.getValueClass(), fallback);
	}

	@Override
	public <A extends IAllele> A getActiveAllele(IChromosome chromosome, Class<? extends A> alleleClass) {
		IAllele allele = getActiveAllele(chromosome);
		if (!alleleClass.isInstance(allele)) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' is not an instance of the class '%s'.", allele, chromosome, alleleClass));
		}
		return alleleClass.cast(allele);
	}

	@Override
	public <A extends IAllele> A getActiveAllele(IChromosome chromosome, Class<? extends A> alleleClass, A fallback) {
		IAllele allele = getActiveAllele(chromosome);
		if (!alleleClass.isInstance(allele)) {
			return fallback;
		}
		return alleleClass.cast(allele);
	}

	@Override
	public IAllele getInactiveAllele(IChromosome<?> chromosomeType) {
		ChromosomePair chromosome = getChromosome(chromosomeType);
		return chromosome.inactive();
	}

	@Override
	public <A extends IAllele> A getInactiveAllele(IChromosome chromosomeType, Class<? extends A> alleleClass) {
		IAllele allele = getInactiveAllele(chromosomeType);
		if (!alleleClass.isInstance(allele)) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the inactive position of the chromosome type '%s' is not an instance of the class '%s'.", allele, chromosomeType, alleleClass));
		}
		return alleleClass.cast(allele);
	}

	@Override
	public <A extends IAllele> A getInactiveAllele(IChromosome chromosomeType, Class<? extends A> alleleClass, A fallback) {
		IAllele allele = getInactiveAllele(chromosomeType);
		if (!alleleClass.isInstance(allele)) {
			return fallback;
		}
		return alleleClass.cast(allele);
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
	public <V> V getInactiveValue(IChromosome chromosomeType, Class<? extends V> valueClass) {
		IAllele allele = getInactiveAllele(chromosomeType);
		V value = AlleleUtils.getAlleleValue(allele, valueClass);
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the inactive position of the chromosome type '%s' has no value.", allele, chromosomeType));
		}
		return value;
	}

	@Override
	public <V> V getInactiveValue(IChromosome chromosomeType, Class<? extends V> valueClass, V fallback) {
		IAllele allele = getInactiveAllele(chromosomeType);
		return AlleleUtils.getAlleleValue(allele, valueClass, fallback);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> IValueAllele<V> getInactiveAllele(IChromosomeValue<V> chromosomeType) {
		return (IValueAllele<V>) getInactiveAllele(chromosomeType, IValueAllele.class);
	}

	@Override
	public <A extends IAllele> A getInactiveAllele(IChromosomeAllele<A> chromosomeType) {
		Class<? extends A> alleleClass = chromosomeType.getAlleleType();
		IAllele allele = getInactiveAllele((IChromosome) chromosomeType);
		if (!alleleClass.isInstance(allele)) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' is not an instance of the class '%s'.", allele, chromosomeType, alleleClass));
		}
		return alleleClass.cast(allele);
	}

	@Override
	public <A extends IAllele> A getInactiveAllele(IChromosomeAllele<A> chromosomeType, A fallback) {
		Class<? extends A> alleleClass = chromosomeType.getAlleleType();
		IAllele allele = getInactiveAllele(chromosomeType);
		if (!alleleClass.isInstance(allele)) {
			return fallback;
		}
		return alleleClass.cast(allele);
	}

	@Override
	public <V> V getInactiveValue(IChromosomeValue<V> chromosomeType) {
		IAllele allele = getInactiveAllele(chromosomeType);
		V value = AlleleUtils.getAlleleValue(allele, chromosomeType.getValueClass());
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' has no value.", allele, chromosomeType));
		}
		return value;
	}

	@Override
	public <V> V getInactiveValue(IChromosomeValue<V> chromosomeType, V fallback) {
		IAllele allele = getInactiveAllele(chromosomeType);
		return AlleleUtils.getAlleleValue(allele, chromosomeType.getValueClass(), fallback);
	}

	@Override
	public ChromosomePair getChromosome(IChromosome chromosomeType) {
		return chromosomes[chromosomeType.ordinal()];
	}

	@Override
	public IAllele[][] getAlleles() {
		IAllele[][] alleles = new IAllele[chromosomes.length][2];
		for (ChromosomePair chromosome : chromosomes) {
			IChromosome iChromosomeType = chromosome.type();
			IAllele[] chromosomeAlleles = alleles[iChromosomeType.ordinal()];
			chromosomeAlleles[0] = chromosome.active();
			chromosomeAlleles[1] = chromosome.inactive();
		}
		return alleles;
	}

	@Override
	public IAllele[] getActiveAlleles() {
		IAllele[] alleles = new IAllele[chromosomes.length];
		for (ChromosomePair chromosome : chromosomes) {
			IChromosome iChromosomeType = chromosome.type();
			alleles[iChromosomeType.ordinal()] = chromosome.active();
		}
		return alleles;
	}

	@Override
	public IAllele[] getInactiveAlleles() {
		IAllele[] alleles = new IAllele[chromosomes.length];
		for (ChromosomePair chromosome : chromosomes) {
			IChromosome iChromosomeType = chromosome.type();
			alleles[iChromosomeType.ordinal()] = chromosome.inactive();
		}
		return alleles;
	}

	@Override
	public IKaryotype getKaryotype() {
		return karyotype;
	}

	@Override
	public <W extends IGenomeWrapper> W asWrapper(Class<? extends W> wrapperClass) {
		ISpeciesType<?> root = GeneticsAPI.apiInstance.getRoot(karyotype.getId());
		IGenomeWrapper wrapper = root.createWrapper(this);
		if (!wrapperClass.isInstance(wrapper)) {
			throw new IllegalStateException();
		}
		return wrapperClass.cast(wrapper);
	}

	@Override
	public CompoundTag writeToNBT(CompoundTag compound) {
		return GeneticSaveHandler.INSTANCE.writeTag(chromosomes, compound);
	}

	@Override
	public boolean equals(Object other) {
		ChromosomePair[] otherChromosomes = other.getChromosomes();
		if (chromosomes.length != otherChromosomes.length) {
			return false;
		}

		for (int i = 0; i < chromosomes.length; i++) {
			ChromosomePair chromosome = chromosomes[i];
			ChromosomePair otherChromosome = otherChromosomes[i];
			if (chromosome == null && otherChromosome == null) {
				continue;
			}
			if (chromosome == null || otherChromosome == null) {
				return false;
			}

			if (!chromosome.isGeneticEqual(otherChromosome)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean matchesTemplateGenome() {
		if (!calculatedTemplateMatch) {
			templateMatches = calculateMatchesTemplateGenome();
			calculatedTemplateMatch = true;
		}

		return templateMatches;
	}

	private boolean calculateMatchesTemplateGenome() {
		IAlleleSpecies primary = getPrimarySpecies();
		// ???
		IAllele[] template = karyotype.getSpeciesRoot().getTemplate(primary);
		ChromosomePair[] chromosomes = getChromosomes();
		for (int i = 0; i < chromosomes.length; i++) {
			ChromosomePair chromosome = chromosomes[i];
			ResourceLocation templateUid = template[i].id();
			IAllele primaryAllele = chromosome.active();
			if (!primaryAllele.id().equals(templateUid)) {
				return false;
			}
			IAllele secondaryAllele = chromosome.inactive();
			if (!secondaryAllele.id().equals(templateUid)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);
		int i = 0;
		for (ChromosomePair chromosome : chromosomes) {
			toStringHelper.add(String.valueOf(i++), chromosome);
		}
		return toStringHelper.toString();
	}
}
