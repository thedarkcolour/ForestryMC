package forestry.api.genetics;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import forestry.api.plugin.IGenomeBuilder;

import genetics.api.individual.IIndividual;

public interface ISpecies<I extends IIndividual> {
	String getTranslationKey();

	default MutableComponent getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	IGenome getDefaultGenome();
}
