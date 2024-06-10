package forestry.core.gui;

import forestry.api.genetics.IForestrySpeciesRoot;

import genetics.api.individual.IIndividual;

public interface INaturalistMenu {
	IForestrySpeciesRoot<IIndividual> getSpeciesRoot();

	int getCurrentPage();

	default void onFlipPage() {}
}
