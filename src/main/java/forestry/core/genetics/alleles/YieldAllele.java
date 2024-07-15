package forestry.core.genetics.alleles;

import java.util.Locale;

import genetics.api.alleles.IAlleleData;

public enum YieldAllele implements IAlleleData<Float> {
	LOWEST(0.025f, true),
	LOWER(0.05f, true),
	LOW(0.1f, true),
	AVERAGE(0.2f, true),
	HIGH(0.3f),
	HIGHER(0.35f),
	HIGHEST(0.4f);

	private final float value;
	private final boolean dominant;

	YieldAllele(float value) {
		this(value, false);
	}

	YieldAllele(float value, boolean dominant) {
		this.value = value;
		this.dominant = dominant;
	}

	@Override
	public Float getValue() {
		return value;
	}

	@Override
	public boolean isDominant() {
		return dominant;
	}

	@Override
	public String getCategory() {
		return "yield";
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
