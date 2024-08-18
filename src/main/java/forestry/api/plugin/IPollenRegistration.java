package forestry.api.plugin;

import forestry.api.genetics.pollen.IPollenType;

public interface IPollenRegistration {
	void registerPollenType(IPollenType<?> pollenType);
}
