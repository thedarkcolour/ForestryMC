package forestry.core.genetics.alleles;

import java.util.Locale;

import forestry.api.genetics.alleles.IValueAllele;

public enum HeightAllele implements IValueAllele<Float> {
	SMALLEST(0.25f),
	SMALLER(0.5f),
	SMALL(0.75f),
	AVERAGE(1.0f),
	LARGE(1.25f),
	LARGER(1.5f),
	LARGEST(1.75f),
	GIGANTIC(2.0f);

	private final float value;
	private final boolean dominant;

	HeightAllele(float value) {
		this(value, false);
	}

	HeightAllele(float value, boolean dominant) {
		this.value = value;
		this.dominant = dominant;
	}

	@Override
	public Float value() {
		return value;
	}

	@Override
	public boolean dominant() {
		return dominant;
	}

	@Override
	public String getCategory() {
		return "height";
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
