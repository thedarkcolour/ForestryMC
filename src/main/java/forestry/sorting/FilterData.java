package forestry.sorting;

import javax.annotation.Nullable;
import java.util.Objects;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

import forestry.api.genetics.filter.IFilterData;

public record FilterData(@Nullable ISpeciesType<?> root, @Nullable IIndividual individual,
						 @Nullable ILifeStage type) implements IFilterData {

	@Override
	public ISpeciesType<?> root() {
		return Objects.requireNonNull(this.root, "No root present");
	}

	@Override
	public IIndividual individual() {
		return Objects.requireNonNull(this.individual, "No individual present");
	}

	@Override
	public ILifeStage type() {
		return Objects.requireNonNull(this.type, "No type present");
	}

	@Override
	public boolean isPresent() {
		return this.root != null && this.individual != null && this.type != null;
	}
}
