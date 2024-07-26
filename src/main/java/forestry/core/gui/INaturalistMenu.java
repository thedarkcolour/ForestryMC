package forestry.core.gui;

import forestry.api.genetics.ISpeciesType;

public interface INaturalistMenu {
	ISpeciesType<?, ?> getSpeciesType();

	int getCurrentPage();

	default void onFlipPage() {
	}
}
