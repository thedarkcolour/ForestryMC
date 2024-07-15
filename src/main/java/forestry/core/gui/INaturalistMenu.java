package forestry.core.gui;

import forestry.api.genetics.IForestrySpeciesType;

import genetics.api.individual.IIndividual;

public interface INaturalistMenu {
	IForestrySpeciesType<IIndividual> getSpeciesRoot();

	int getCurrentPage();

	default void onFlipPage() {}
}
