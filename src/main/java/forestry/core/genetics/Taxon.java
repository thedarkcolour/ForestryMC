package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import genetics.ApiInstance;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.ITaxon;
import forestry.api.genetics.TaxonomicRank;

public class Taxon implements ITaxon {
	private final TaxonomicRank rank;
	private final String id;

	private final ArrayList<IAlleleSpecies> members = new ArrayList<>();
	private final ArrayList<ITaxon> groups = new ArrayList<>();

	public Taxon(TaxonomicRank rank, String id, String scientific) {
		this.rank = rank;
		this.id = rank.name().toLowerCase(Locale.ENGLISH) + "." + id;
		this.scientific = scientific;
		ApiInstance.INSTANCE.getClassificationRegistry().registerClassification(this);
	}

	@Override
	public TaxonomicRank rank() {
		return rank;
	}

	@Override
	public String getId() {
		return id;
	}

	@Nullable
	@Override
	public ITaxon getParent() {
		return parent;
	}

	@Override
	public List<ITaxon> getSubTaxa() {
		return groups.toArray(new ITaxon[groups.size()]);
	}

	@Override
	public List<IAlleleSpecies> getMemberSpecies() {
		return members.toArray(new IAlleleSpecies[0]);
	}
}
