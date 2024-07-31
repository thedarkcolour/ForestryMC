package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ITaxon;
import forestry.api.genetics.TaxonomicRank;
import forestry.api.genetics.alleles.IChromosome;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;

public final class Taxon implements ITaxon {
	private final String name;
	private final TaxonomicRank rank;
	@Nullable
	private final ITaxon parent;
	private final Reference2ObjectOpenHashMap<IChromosome<?>, TaxonAllele> alleles;

	// these are updated later
	@Nullable
	private List<ITaxon> children;
	@Nullable
	private List<ISpecies<?>> species;

	@ApiStatus.Internal
	public Taxon(String name, TaxonomicRank rank, @Nullable ITaxon parent, Reference2ObjectOpenHashMap<IChromosome<?>, TaxonAllele> alleles) {
		this.name = name;
		this.rank = rank;
		this.parent = parent;
		this.alleles = alleles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ITaxon taxon = (ITaxon) o;

		if (!this.name.equals(taxon.name())) {
			return false;
		}
		return this.rank == taxon.rank();
	}

	@Override
	public int hashCode() {
		int result = this.name.hashCode();
		result = 31 * result + this.rank.hashCode();
		return result;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public TaxonomicRank rank() {
		return this.rank;
	}

	@Override
	public List<ITaxon> children() {
		List<ITaxon> children = this.children;
		if (children == null) {
			throw new IllegalStateException("Children not available yet");
		}
		return children;
	}

	@ApiStatus.Internal
	public void setChildren(List<ITaxon> children) {
		if (this.children != null) {
			throw new IllegalStateException("Children already registered");
		}
		this.children = children;
	}

	@Override
	public List<ISpecies<?>> species() {
		List<ISpecies<?>> species = this.species;
		if (species == null) {
			throw new IllegalStateException("Species not available yet");
		}
		return species;
	}

	@ApiStatus.Internal
	public void setSpecies(List<ISpecies<?>> species) {
		if (this.species != null) {
			throw new IllegalStateException("Species already registered");
		}
		this.species = species;
	}

	@Nullable
	@Override
	public ITaxon parent() {
		return this.parent;
	}

	@Override
	public Map<IChromosome<?>, TaxonAllele> alleles() {
		return Collections.unmodifiableMap(this.alleles);
	}

	@Override
	public String toString() {
		return "Taxon{" +
				"name='" + name + '\'' +
				", rank=" + rank +
				'}';
	}
}
