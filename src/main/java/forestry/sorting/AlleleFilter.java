package forestry.sorting;

import javax.annotation.Nullable;

import forestry.api.genetics.ISpecies;

public class AlleleFilter {
	@Nullable
	public ISpecies<?> activeSpecies;

	@Nullable
	public ISpecies<?> inactiveSpecies;

	public boolean isValid(ISpecies<?> active, ISpecies<?> inactive) {
		return (this.activeSpecies == null || active == this.activeSpecies)
			&& (this.inactiveSpecies == null || inactive == this.inactiveSpecies);
	}

	public boolean isEmpty() {
		return this.activeSpecies == null && this.inactiveSpecies == null;
	}
}
