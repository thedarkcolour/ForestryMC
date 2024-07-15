package forestry.core.genetics.alleles;

import java.util.Locale;

import net.minecraft.core.Vec3i;

import genetics.api.alleles.IAlleleData;

public enum TerritoryAllele implements IAlleleData<Vec3i> {
	AVERAGE(9, 6, 9),
	LARGE(11, 8, 11),
	LARGER(13, 12, 13),
	LARGEST(15, 13, 15);

	private final Vec3i area;
	private final boolean dominant;

	TerritoryAllele(int x, int y, int z) {
		this(x, y, z, false);
	}

	TerritoryAllele(int x, int y, int z, boolean dominant) {
		this.area = new Vec3i(x, y, z);
		this.dominant = dominant;
	}

	@Override
	public Vec3i getValue() {
		return area;
	}

	@Override
	public boolean isDominant() {
		return dominant;
	}

	@Override
	public String getCategory() {
		return "territory";
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
