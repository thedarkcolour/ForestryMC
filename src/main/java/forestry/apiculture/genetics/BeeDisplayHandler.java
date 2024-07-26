package forestry.apiculture.genetics;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;

import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alyzer.IAlleleDisplayHandler;
import forestry.api.genetics.alyzer.IAlleleDisplayHelper;
import forestry.core.genetics.GenericRatings;

public enum BeeDisplayHandler implements IAlleleDisplayHandler<IBee> {
	GENERATIONS(-1) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			int generation = individual.getGeneration();
			if (generation > 0) {
				Rarity rarity;
				if (generation >= 1000) {
					rarity = Rarity.EPIC;
				} else if (generation >= 100) {
					rarity = Rarity.RARE;
				} else if (generation >= 10) {
					rarity = Rarity.UNCOMMON;
				} else {
					rarity = Rarity.COMMON;
				}
				toolTip.translated("for.gui.beealyzer.generations", generation).style(rarity.getStyleModifier());
			}
		}
	},
	SPECIES(0),
	SPEED(2, 1) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			toolTip.singleLine()
					.add(genome.getActiveName(BeeChromosomes.SPEED))
					.text(" ")
					.translated("for.gui.worker")
					.style(ChatFormatting.GRAY)
					.create();
		}
	},
	LIFESPAN(1, 0) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			toolTip.singleLine()
					.add(genome.getActiveName(BeeChromosomes.LIFESPAN))
					.text(" ")
					.translated("for.gui.life")
					.style(ChatFormatting.GRAY)
					.create();
		}
	},
	FERTILITY(5) {
	},
	TEMPERATURE_TOLERANCE(-1, 2) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			IBeeSpecies primary = genome.getActiveValue(BeeChromosomes.SPECIES);
			Component caption = ClimateHelper.toDisplay(primary.getTemperature());
			toolTip.singleLine()
					.text("T: ")
					.add(caption)
					.text(" / ")
					.add(genome.getActiveName(BeeChromosomes.TEMPERATURE_TOLERANCE))
					.style(ChatFormatting.GREEN)
					.create();
		}
	},
	HUMIDITY_TOLERANCE(-1, 3) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			IBeeSpecies primary = genome.getActiveSpecies();
			Component caption = ClimateHelper.toDisplay(primary.getHumidity());
			toolTip.singleLine()
					.text("H: ")
					.add(caption)
					.text(" / ")
					.add(genome.getActiveName(BeeChromosomes.HUMIDITY_TOLERANCE))
					.style(ChatFormatting.GREEN)
					.create();
		}
	},
	FLOWER_PROVIDER(4, 4) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			toolTip.add(genome.getActiveName(BeeChromosomes.FLOWER_TYPE), ChatFormatting.GRAY);
		}
	},
	FLOWERING(3, -1),
	NEVER_SLEEPS(-1, 5) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			if (genome.getActiveValue(BeeChromosomes.NEVER_SLEEPS)) {
				toolTip.add(GenericRatings.rateActivityTime(true, false)).style(ChatFormatting.RED);
			}
		}
	},
	TOLERATES_RAIN(-1, 6) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
			if (genome.getActiveValue(BeeChromosomes.TOLERATES_RAIN)) {
				toolTip.translated("for.gui.flyer.tooltip").style(ChatFormatting.WHITE);
			}
		}
	},
	TERRITORY(6),
	EFFECT(7);

	private final int alyzerIndex;
	private final int tooltipIndex;

	BeeDisplayHandler(int alyzerIndex) {
		this(alyzerIndex, -1);
	}

	BeeDisplayHandler(int alyzerIndex, int tooltipIndex) {
		this.alyzerIndex = alyzerIndex;
		this.tooltipIndex = tooltipIndex;
	}

	public static void init(IAlleleDisplayHelper helper) {
		for (BeeDisplayHandler handler : values()) {
			int tooltipIndex = handler.tooltipIndex;
			if (tooltipIndex >= 0) {
				helper.addTooltip(handler, ForestrySpeciesTypes.BEE, tooltipIndex * 10);
			}
			int alyzerIndex = handler.alyzerIndex;
			if (alyzerIndex >= 0) {
				helper.addAlyzer(handler, ForestrySpeciesTypes.BEE, alyzerIndex * 10);
			}
		}
	}

	@Override
	public void addTooltip(ToolTip toolTip, IGenome genome, IBee individual) {
		//Default Implementation
	}
}
