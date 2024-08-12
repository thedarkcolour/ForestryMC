package forestry.api.genetics.filter;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

public record FilterData(ISpeciesType<?, ?> type, IIndividual individual, ILifeStage stage) {
	public FilterData(IIndividual individual, ILifeStage stage) {
		this(individual.getType(), individual, stage);
	}
}
