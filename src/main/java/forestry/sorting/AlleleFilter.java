package forestry.sorting;

import javax.annotation.Nullable;

import forestry.api.genetics.alleles.IAllele;

public class AlleleFilter {
	@Nullable
	public IAllele activeAllele;

	@Nullable
	public IAllele inactiveAllele;

	public boolean isValid(String activeUID, String inactiveUID) {
		return (this.activeAllele == null || activeUID.equals(this.activeAllele.alleleId().toString()))
			&& (this.inactiveAllele == null || inactiveUID.equals(this.inactiveAllele.alleleId().toString()));
	}

	public boolean isEmpty() {
		return activeAllele == null && inactiveAllele == null;
	}
}
