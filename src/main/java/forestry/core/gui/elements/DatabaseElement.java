package forestry.core.gui.elements;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

import deleteme.Todos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import forestry.api.core.ToleranceType;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.genetics.alleles.IValueChromosome;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.core.gui.elements.layouts.ContainerElement;
import forestry.core.gui.elements.layouts.FlexLayout;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

public class DatabaseElement extends ContainerElement {
	private DatabaseMode mode = DatabaseMode.ACTIVE;
	@Nullable
	private IIndividual individual;
	private int secondColumn = 0;
	private int thirdColumn = 0;

	public DatabaseElement(int width) {
		setSize(width, UNKNOWN_HEIGHT);
		setLayout(FlexLayout.vertical(0));
		this.secondColumn = width / 2;
	}

	public void init(DatabaseMode mode, IIndividual individual, int secondColumn, int thirdColumn) {
		this.mode = mode;
		this.individual = individual;
		this.secondColumn = secondColumn;
		this.thirdColumn = thirdColumn;
	}

	@Nullable
	public IIndividual getIndividual() {
		return individual;
	}

	public IGenome getGenome() {
		Preconditions.checkNotNull(individual, "Database Element has not been initialised.");
		return individual.getGenome();
	}

	public void addFertilityLine(Component chromosomeName, IIntegerChromosome chromosome, int texOffset) {
		IGenome genome = getGenome();
		IIntegerAllele activeAllele = genome.getActiveAllele(chromosome);
		IIntegerAllele inactiveAllele = genome.getInactiveAllele(chromosome);

		if (mode == DatabaseMode.BOTH) {
			addLine(chromosomeName, GuiElementFactory.INSTANCE.createFertilityInfo(activeAllele, texOffset), GuiElementFactory.INSTANCE.createFertilityInfo(inactiveAllele, texOffset));
		} else {
			boolean active = mode == DatabaseMode.ACTIVE;
			IIntegerAllele allele = active ? activeAllele : inactiveAllele;
			addLine(chromosomeName, GuiElementFactory.INSTANCE.createFertilityInfo(allele, texOffset));
		}
	}

	public void addToleranceLine(IValueChromosome<ToleranceType> chromosome) {
		IValueAllele<ToleranceType> allele = getGenome().getActiveAllele(chromosome);
		addLine(Component.literal("  ").append(Component.translatable("for.gui.tolerance")), GuiElementFactory.INSTANCE.createToleranceInfo(chromosome, allele));
	}

	public void addMutation(int x, int y, int width, int height, IMutation<?> mutation, ISpecies<?> species, IBreedingTracker breedingTracker) {
		add(GuiElementFactory.INSTANCE.createMutation(x, y, width, height, mutation, species, breedingTracker));
	}

	public void addMutationResultant(int x, int y, int width, int height, IMutation<?> mutation, IBreedingTracker breedingTracker) {
		add(GuiElementFactory.INSTANCE.createMutationResultant(x, y, width, height, mutation, breedingTracker));
	}

	public void addLine(Component firstText, Component secondText, boolean dominant) {
		addLine(firstText, secondText, GuiElementFactory.INSTANCE.guiStyle, GuiElementFactory.INSTANCE.getStateStyle(dominant));
	}

	public void addLine(Component leftText, Function<Boolean, Component> toText, boolean dominant) {
		if (mode == DatabaseMode.BOTH) {
			addLine(leftText, toText.apply(true), toText.apply(false), dominant, dominant);
		} else {
			addLine(leftText, toText.apply(mode == DatabaseMode.ACTIVE), dominant);
		}
	}

	/*@Override
	public void addRow(String firstText, String secondText, String thirdText, IIndividual individual, IChromosomeType chromosome) {
		addRow(firstText, secondText, thirdText, GuiElementFactory.GUI_STYLE,
			GuiElementFactory.INSTANCE.getStateStyle(individual.getGenome().getActiveAllele(chromosome).isDominant()),
			GuiElementFactory.INSTANCE.getStateStyle(individual.getGenome().getInactiveAllele(chromosome).isDominant()));
	}*/

	public void addLine(Component leftText, Function<Boolean, Component> toText, IChromosome chromosome) {
		IGenome genome = getGenome();
		IAllele activeAllele = genome.getActiveAllele(chromosome);
		IAllele inactiveAllele = genome.getInactiveAllele(chromosome);
		if (mode == DatabaseMode.BOTH) {
			addLine(leftText, toText.apply(true), toText.apply(false), activeAllele.dominant(), inactiveAllele.dominant());
		} else {
			boolean active = mode == DatabaseMode.ACTIVE;
			IAllele allele = active ? activeAllele : inactiveAllele;
			addLine(leftText, toText.apply(active), allele.dominant());
		}
	}

	public void addLine(Component firstText, Component secondText, Component thirdText, boolean secondDominant, boolean thirdDominant) {
		// todo: why aren't we rendering this?
		Todos.todo();
	}

	public <A extends IAllele> void addLine(Component chromosomeName, IChromosome<A> chromosome) {
		addLine(chromosomeName, (allele, b) -> chromosome.getDisplayName(allele), chromosome);
	}

	public void addLine(Component firstText, Component secondText, Style firstStyle, Style secondStyle) {
		ContainerElement first = addSplitText(preferredSize.width, firstText, firstStyle);
		ContainerElement second = addSplitText(preferredSize.width, secondText, secondStyle);
		addLine(first, second);
	}

	private ContainerElement addSplitText(int width, Component text, Style style) {
		Font fontRenderer = Minecraft.getInstance().font;
		ContainerElement vertical = GuiElementFactory.vertical(width, 0);
		fontRenderer.getSplitter().splitLines(text, width, style, (contents, contentsStyle) -> {
			vertical.label(contents.getString()).setStyle(style);
		});
		return vertical;
	}

	private void addLine(Component chromosomeName, GuiElement right) {
		int center = preferredSize.width / 2;
		GuiElement first = addSplitText(center, chromosomeName, GuiElementFactory.INSTANCE.guiStyle);
		addLine(first, right);
	}

	private void addLine(Component chromosomeName, GuiElement second, GuiElement third) {
		int center = preferredSize.width / 2;
		GuiElement first = addSplitText(center, chromosomeName, GuiElementFactory.INSTANCE.guiStyle);
		addLine(first, second, third);
	}

	private void addLine(GuiElement first, GuiElement second, GuiElement third) {
		ContainerElement panel = pane(preferredSize.width, UNKNOWN_HEIGHT);
		first.setAlign(Alignment.MIDDLE_LEFT);
		second.setAlign(Alignment.MIDDLE_LEFT);
		third.setAlign(Alignment.MIDDLE_LEFT);
		panel.add(first);
		panel.add(second);
		panel.add(third);
		second.setXPosition(secondColumn);
		third.setXPosition(thirdColumn);
	}

	private void addLine(GuiElement first, GuiElement second) {
		ContainerElement panel = pane(preferredSize.width, UNKNOWN_HEIGHT);
		first.setAlign(Alignment.MIDDLE_LEFT);
		second.setAlign(Alignment.MIDDLE_LEFT);
		panel.add(first);
		panel.add(second);
		second.setXPosition(secondColumn);
	}

	public <A extends IAllele> void addLine(Component chromosomeName, BiFunction<A, Boolean, Component> toText, IChromosome<A> chromosome) {
		addAlleleRow(chromosomeName, toText, chromosome, null);
	}

	public <A extends IAllele> void addLine(Component chromosomeName, BiFunction<A, Boolean, Component> toText, IChromosome<A> chromosome, boolean dominant) {
		addAlleleRow(chromosomeName, toText, chromosome, dominant);
	}

	private <A extends IAllele> void addAlleleRow(Component chromosomeName, BiFunction<A, Boolean, Component> toString, IChromosome<A> chromosome, @Nullable Boolean dominant) {
		IGenome genome = getGenome();
		A activeAllele = genome.getActiveAllele(chromosome);
		A inactiveAllele = genome.getInactiveAllele(chromosome);
		if (mode == DatabaseMode.BOTH) {
			addLine(chromosomeName, toString.apply(activeAllele, true), toString.apply(inactiveAllele, false), dominant != null ? dominant : activeAllele.dominant(), dominant != null ? dominant : inactiveAllele.dominant());
		} else {
			boolean active = mode == DatabaseMode.ACTIVE;
			A allele = active ? activeAllele : inactiveAllele;
			addLine(chromosomeName, toString.apply(allele, active), dominant != null ? dominant : allele.dominant());
		}
	}

	public void addSpeciesLine(String firstText, @Nullable String secondText, IChromosome chromosome) {
		/*ISpecies<?> primary = individual.getGenome().getPrimary();
		ISpecies<?> secondary = individual.getGenome().getSecondary();

		textLayout.drawLine(text0, textLayout.column0);
		int columnwidth = textLayout.column2 - textLayout.column1 - 2;

		Map<String, ItemStack> iconStacks = chromosome.getSpeciesRoot().getAlyzerPlugin().getIconStacks();

		GuiUtil.drawItemStack(this, iconStacks.get(primary.getIdString()), guiLeft + textLayout.column1 + columnwidth - 20, guiTop + 10);
		GuiUtil.drawItemStack(this, iconStacks.get(secondary.getIdString()), guiLeft + textLayout.column2 + columnwidth - 20, guiTop + 10);

		String primaryName = customPrimaryName == null ? primary.getAlleleName() : customPrimaryName;
		String secondaryName = customSecondaryName == null ? secondary.getAlleleName() : customSecondaryName;

		drawSplitLine(primaryName, textLayout.column1, columnwidth, individual, chromosome, false);
		drawSplitLine(secondaryName, textLayout.column2, columnwidth, individual, chromosome, true);

		textLayout.newLine();*/
	}
}
