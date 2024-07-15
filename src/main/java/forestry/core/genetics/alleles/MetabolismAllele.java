package forestry.core.genetics.alleles;

import java.util.Locale;

import genetics.api.alleles.IAlleleData;

public enum MetabolismAllele implements IAlleleData<Integer> {
	SLOWEST(1),
	SLOWER(2),
	SLOW(3),
	NORMAL(5),
	FAST(7),
	FASTER(8),
	FASTEST(10);

	private final Integer value;

	MetabolismAllele(Integer value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public boolean isDominant() {
		// According to old registration (alongside TreeChromosomes.GIRTH and ButterflyChromosomes.FERTILITY), all are dominant
		return true;
	}

	@Override
	public String getCategory() {
		return "metabolism";
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
