package forestry.core.genetics;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.alyzer.IAlleleDisplayHelper;

import forestry.api.genetics.IGenome;
import forestry.apiculture.genetics.IGeneticTooltipProvider;

public enum DefaultDisplayHandler implements IGeneticTooltipProvider<IIndividual> {
	UNKNOWN(-3) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IIndividual individual) {
			toolTip.singleLine()
					.text("<")
					.translated("for.gui.unknown")
					.text(">")
					.style(ChatFormatting.GRAY)
					.create();
		}
	}, HYBRID(-2) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IIndividual individual) {
			ISpeciesChromosome<?> speciesType = individual.getRoot().getKaryotype().getSpeciesChromosome();
			Component primary = genome.getActiveName(speciesType);
			Component secondary = genome.getActiveName(speciesType);
			if (!individual.isPureBred(speciesType)) {
				toolTip.add(Component.translatable("for.bees.hybrid", primary, secondary).withStyle(ChatFormatting.BLUE));
			}
		}
	};

	final int tooltipIndex;

	DefaultDisplayHandler(int tooltipIndex) {
		this.tooltipIndex = tooltipIndex;
	}

	public static void init(IAlleleDisplayHelper helper) {
		for (DefaultDisplayHandler handler : values()) {
			int tooltipIndex = handler.tooltipIndex;
			if (tooltipIndex >= 0) {
				helper.addTooltip(handler, ForestrySpeciesTypes.BEE, tooltipIndex * 10);
				helper.addTooltip(handler, ForestrySpeciesTypes.TREE, tooltipIndex * 10);
				helper.addTooltip(handler, ForestrySpeciesTypes.BUTTERFLY, tooltipIndex * 10);
			}
		}
	}

}
