package forestry.apiculture.genetics;

import forestry.api.core.tooltips.ToolTip;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;

public interface IGeneticTooltipProvider<I extends IIndividual> {
	/**
	 * Adds the handled allele to the tooltip of the individual.
	 *
	 * @param toolTip The instance of the tooltip helper class.
	 * @param genome  The genome of the individual
	 */
	void addTooltip(ToolTip toolTip, IGenome genome, I individual);
}
