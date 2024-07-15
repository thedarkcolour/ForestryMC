package forestry.core.genetics.alleles;

import java.util.Locale;

import genetics.api.alleles.IAlleleData;

public enum MaturationAllele implements IAlleleData<Integer> {
	SLOWEST(10, true),
	SLOWER(7),
	SLOW(5, true),
	AVERAGE(4),
	FAST(3),
	FASTER(2),
	FASTEST(1);

	private final Integer value;
	private final boolean dominant;

	MaturationAllele(Integer value) {
		this(value, false);
	}

	MaturationAllele(Integer value, boolean dominant) {
		this.value = value;
		this.dominant = dominant;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public boolean isDominant() {
		return dominant;
	}

	@Override
	public String getCategory() {
		return "maturation";
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
