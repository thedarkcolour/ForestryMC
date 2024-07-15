package forestry.api.genetics.alyzer;

import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;

import forestry.apiculture.genetics.IGeneticTooltipProvider;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;

public interface IAlleleDisplayHelper {
	void addTooltip(IGeneticTooltipProvider<? extends IIndividual> provider, ResourceLocation id, int orderingInfo);

	void addTooltip(IGeneticTooltipProvider<? extends IIndividual> provider, ResourceLocation id, int orderingInfo, Predicate<ILifeStage> typeFilter);

	void addAlyzer(IAlyzerDisplayProvider provider, ResourceLocation id, int orderingInfo);

}
