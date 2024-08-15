package forestry.core.gui.elements;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import forestry.api.core.ToleranceType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.core.gui.elements.layouts.ContainerElement;
import forestry.core.gui.elements.layouts.FlexLayout;

public class DatabaseElement extends ContainerElement {
	private DatabaseMode mode = DatabaseMode.ACTIVE;
	@Nullable
	private IIndividual individual;
	private int secondColumn;

	public DatabaseElement(int width) {
		setSize(width, UNKNOWN_HEIGHT);
		setLayout(FlexLayout.vertical(0));
		this.secondColumn = width / 2;
	}

	public void init(DatabaseMode mode, IIndividual individual, int secondColumn) {
		this.mode = mode;
		this.individual = individual;
		this.secondColumn = secondColumn;
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

		boolean active = mode == DatabaseMode.ACTIVE;
		IIntegerAllele allele = active ? activeAllele : inactiveAllele;
		addLine(chromosomeName, GuiElementFactory.INSTANCE.createFertilityInfo(allele, texOffset));
	}

	public void addToleranceLine(IValueChromosome<ToleranceType> chromosome) {
		IValueAllele<ToleranceType> allele = getGenome().getActiveAllele(chromosome);
		addLine(Component.literal("  ").append(Component.translatable("for.gui.tolerance")), GuiElementFactory.INSTANCE.createToleranceInfo(chromosome, allele));
	}

	public void addLine(Component firstText, Component secondText, boolean dominant) {
		addLine(firstText, secondText, GuiElementFactory.INSTANCE.guiStyle, GuiElementFactory.INSTANCE.getStateStyle(dominant));
	}

	public void addLine(Component leftText, Function<Boolean, Component> toText, boolean dominant) {
		addLine(leftText, toText.apply(mode == DatabaseMode.ACTIVE), dominant);
	}

	public void addLine(Component leftText, Function<Boolean, Component> toText, IChromosome chromosome) {
		IGenome genome = getGenome();
		IAllele activeAllele = genome.getActiveAllele(chromosome);
		IAllele inactiveAllele = genome.getInactiveAllele(chromosome);
		boolean active = mode == DatabaseMode.ACTIVE;
		IAllele allele = active ? activeAllele : inactiveAllele;
		addLine(leftText, toText.apply(active), allele.dominant());
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

	private <A extends IAllele> void addAlleleRow(Component chromosomeName, BiFunction<A, Boolean, Component> toString, IChromosome<A> chromosome, @Nullable Boolean dominant) {
		IGenome genome = getGenome();
		A activeAllele = genome.getActiveAllele(chromosome);
		A inactiveAllele = genome.getInactiveAllele(chromosome);
		boolean active = mode == DatabaseMode.ACTIVE;
		A allele = active ? activeAllele : inactiveAllele;
		addLine(chromosomeName, toString.apply(allele, active), dominant != null ? dominant : allele.dominant());
	}
}
