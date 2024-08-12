package forestry.arboriculture.blocks;

import java.util.Locale;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.core.IBlockSubtype;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IValueAllele;

// todo add coconut, was planned
public enum ForestryPodType implements IBlockSubtype {
	COCOA(ForestryAlleles.FRUIT_COCOA),
	DATES(ForestryAlleles.FRUIT_DATES),
	PAPAYA(ForestryAlleles.FRUIT_PAPAYA);

	private final IValueAllele<IFruit> allele;

	ForestryPodType(IValueAllele<IFruit> allele) {
		this.allele = allele;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public IFruit getFruit() {
		return this.allele.value();
	}
}
