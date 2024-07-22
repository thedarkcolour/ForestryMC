package forestry.api.genetics;

import javax.annotation.Nullable;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import forestry.api.lepidopterology.genetics.ButterflyLifeStage;

public interface IIndividual {
	IGenome getGenome();

	ISpeciesType<?> getType();

	boolean mate(@Nullable IGenome mate);

	@Nullable
	IGenome getMate();

	ISpecies<?> getSpecies();

	ILifeStage getLifeStage();

	boolean isAnalyzed();

	ItemStack copyWithStage(ILifeStage stage);

	void addTooltip(List<Component> list);
}
