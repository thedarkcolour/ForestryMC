package forestry.core.gui;

import forestry.api.genetics.ISpeciesType;

public interface INaturalistMenu {
	ISpeciesType<?> getSpeciesRoot();

	int getCurrentPage();

	default void onFlipPage() {
	}
}
