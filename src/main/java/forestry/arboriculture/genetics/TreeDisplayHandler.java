package forestry.arboriculture.genetics;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.tooltips.ITextInstance;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.genetics.alyzer.IAlleleDisplayHandler;
import forestry.api.genetics.alyzer.IAlleleDisplayHelper;
import forestry.apiculture.genetics.IGeneticTooltipProvider;
import forestry.arboriculture.genetics.alleles.AlleleFruits;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IValueAllele;
import genetics.api.individual.IChromosomeAllele;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IChromosomeValue;
import forestry.api.genetics.IGenome;

public enum TreeDisplayHandler implements IAlleleDisplayHandler<ITree> {
	SAPPINESS(TreeChromosomes.SAPPINESS, ChatFormatting.GOLD, "S: %1$s"),
	MATURATION(TreeChromosomes.MATURATION, ChatFormatting.RED, "M: %1$s"),
	GROUP_0(0, 0, 1),
	HEIGHT(TreeChromosomes.HEIGHT, ChatFormatting.LIGHT_PURPLE, "H: %1$s"),
	GIRTH(TreeChromosomes.GIRTH, ChatFormatting.AQUA, "G: %1$sx%2$s"),
	GROUP_1(1, 3, 4),
	SAPLINGS(TreeChromosomes.SAPLINGS, ChatFormatting.YELLOW, "S: %1$s"),
	YIELD(TreeChromosomes.YIELD, ChatFormatting.WHITE, "Y: %1$s"),
	GROUP_2(2, 6, 7),
	FIREPROOF(TreeChromosomes.FIREPROOF, 3) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, ITree individual) {
			Boolean value = getActiveValue(genome);
			if (!value) {
				return;
			}
			toolTip.translated("for.gui.fireresist")
					.style(ChatFormatting.RED);
		}
	},
	FRUITS(TreeChromosomes.FRUITS, 4) {
		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, ITree individual) {
			IAllele fruit = getActiveAllele(genome);
			if (fruit != AlleleFruits.fruitNone) {
				ITextInstance<?, ?, ?> instance = toolTip.singleLine().text("F: ")
						.add(genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider().getDescription()).style(ChatFormatting.GREEN);
				if (!individual.canBearFruit()) {
					instance.style(ChatFormatting.STRIKETHROUGH);
				}
				instance.create();
			}
		}
	};

	final IChromosome type;
	@Nullable
	final String alyzerCaption;
	final int alyzerIndex;
	final int tooltipIndex;
	@Nullable
	final ChatFormatting color;
	final String formattingText;
	final int[] groupPair;

	TreeDisplayHandler(int tooltipIndex, int... groupPair) {
		this.type = null;
		this.alyzerCaption = "";
		this.alyzerIndex = -1;
		this.tooltipIndex = tooltipIndex;
		this.color = null;
		this.formattingText = "";
		this.groupPair = groupPair;
	}

	TreeDisplayHandler(IChromosome type, int alyzerIndex, int tooltipIndex) {
		this(type, alyzerIndex, tooltipIndex, null);
	}

	TreeDisplayHandler(IChromosome type, int alyzerIndex, @Nullable String alyzerCaption) {
		this(type, alyzerIndex, -1, alyzerCaption);
	}

	TreeDisplayHandler(IChromosome type, ChatFormatting color, String formattingText) {
		this.type = type;
		this.alyzerCaption = "";
		this.alyzerIndex = -1;
		this.tooltipIndex = -1;
		this.color = color;
		this.formattingText = formattingText;
		this.groupPair = new int[0];
	}

	TreeDisplayHandler(IChromosome type, int tooltipIndex) {
		this.type = type;
		this.alyzerCaption = "";
		this.alyzerIndex = -1;
		this.tooltipIndex = tooltipIndex;
		this.color = null;
		this.formattingText = "";
		this.groupPair = new int[0];
	}

	TreeDisplayHandler(IChromosome type, int alyzerIndex, int tooltipIndex, @Nullable String alyzerCaption) {
		this.type = type;
		this.alyzerCaption = alyzerCaption;
		this.alyzerIndex = alyzerIndex;
		this.tooltipIndex = tooltipIndex;
		this.color = null;
		this.formattingText = "";
		this.groupPair = new int[0];
	}

	public static void init(IAlleleDisplayHelper helper) {
		for (TreeDisplayHandler handler : values()) {
			int tooltipIndex = handler.tooltipIndex;
			if (tooltipIndex >= 0) {
				helper.addTooltip(handler, ForestrySpeciesType.TREE, tooltipIndex * 10);
			}
			int alyzerIndex = handler.alyzerIndex;
			if (alyzerIndex >= 0) {
				helper.addAlyzer(handler, ForestrySpeciesType.TREE, alyzerIndex * 10);
			}
		}
	}

	@Override
	public void addTooltip(ToolTip toolTip, IGenome genome, ITree individual) {
		//Default Implementation
		if (groupPair.length == 2) {
			TreeDisplayHandler first = values()[groupPair[0]];
			TreeDisplayHandler second = values()[groupPair[1]];
			//list.add(new TranslationTextComponent("%1$s %2$s", saplings, maturation));
			Object firstValue = genome.getActiveValue(first.type, Object.class);
			Object secondValue = genome.getActiveValue(second.type, Object.class);
			toolTip.singleLine()
					.add(Component.translatable(first.formattingText, firstValue
					), first.color)
					.text(" ")
					.add(Component.translatable(second.formattingText, secondValue
					), second.color)
					.create();
			//toolTip.translated("%1$s %2$s", first.formattingText, second.formattingText)
		}
	}

	<V> IValueAllele<V> getActive(IGenome genome) {
		//noinspection unchecked
		return genome.getActiveAllele((IChromosomeValue<V>) type);
	}

	<V> IValueAllele<V> getInactive(IGenome genome) {
		//noinspection unchecked
		return genome.getInactiveAllele((IChromosomeValue<V>) type);
	}

	<A extends IAllele> A getActiveAllele(IGenome genome) {
		//noinspection unchecked
		return genome.getActiveAllele((IChromosomeAllele<A>) type);
	}

	<A extends IAllele> A getInactiveAllele(IGenome genome) {
		//noinspection unchecked
		return genome.getInactiveAllele((IChromosomeAllele<A>) type);
	}

	<V> V getActiveValue(IGenome genome) {
		//noinspection unchecked
		return genome.getActiveValue((IChromosomeValue<V>) type);
	}

	<V> V getInactiveValue(IGenome genome) {
		//noinspection unchecked
		return genome.getInactiveValue((IChromosomeValue<V>) type);
	}

	private static class AllelePair implements IGeneticTooltipProvider<ITree> {
		private final IGeneticTooltipProvider<ITree> first;
		private final IGeneticTooltipProvider<ITree> second;

		private AllelePair(IGeneticTooltipProvider<ITree> first, IGeneticTooltipProvider<ITree> second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public void addTooltip(ToolTip toolTip, IGenome genome, ITree individual) {
			ToolTip cache = new ToolTip();
			first.addTooltip(cache, genome, individual);
			Component fistComp = cache.lastComponent();
			second.addTooltip(cache, genome, individual);
			Component secondComp = cache.lastComponent();
			toolTip.translated("%1$s %2$s", fistComp, secondComp);
		}
	}
}
