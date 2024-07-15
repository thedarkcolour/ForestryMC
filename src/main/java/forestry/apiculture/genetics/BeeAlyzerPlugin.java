package forestry.apiculture.genetics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.apiculture.features.ApicultureItems;
import forestry.core.config.Config;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.TextLayoutHelper;
import forestry.core.gui.widgets.ItemStackWidget;
import forestry.core.gui.widgets.WidgetManager;

import genetics.api.GeneticHelper;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.IGenome;
import genetics.api.organism.IIndividualCapability;
import forestry.api.genetics.ILifeStage;

public enum BeeAlyzerPlugin implements IAlyzerPlugin {
	INSTANCE;

	private final Map<ResourceLocation, ItemStack> iconStacks = new HashMap<>();

	BeeAlyzerPlugin() {
		NonNullList<ItemStack> beeList = NonNullList.create();
		ApicultureItems.BEE_DRONE.item().addCreativeItems(beeList, false);
		for (ItemStack beeStack : beeList) {
			IIndividualCapability<?> organism = GeneticHelper.getOrganism(beeStack);
			if (organism.isEmpty()) {
				continue;
			}
			IAlleleBeeSpecies species = organism.getAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES, true);
			iconStacks.put(species.getId(), beeStack);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage1(PoseStack transform, Screen gui, ItemStack itemStack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IBee bee = BeeManager.beeRoot.create(itemStack);
			if (bee == null) {
				return;
			}

			ILifeStage type = BeeManager.beeRoot.getTypes().getType(itemStack);
			if (type == null) {
				return;
			}

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
			textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

			textLayout.newLine();
			textLayout.newLine();

			guiAlyzer.drawSpeciesRow(transform, Component.translatable("for.gui.species"), bee, BeeChromosomes.SPECIES, type);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.lifespan"), bee, BeeChromosomes.LIFESPAN);
			textLayout.newLine();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.speed"), bee, BeeChromosomes.SPEED);
			textLayout.newLine();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.pollination"), bee, BeeChromosomes.POLLINATION);
			textLayout.newLine();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.flowers"), bee, BeeChromosomes.FLOWER_TYPE);
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.fertility"), GuiAlyzer.COLUMN_0);
			IIntegerAllele primaryFertility = bee.getGenome().getActiveAllele(BeeChromosomes.FERTILITY);
			IIntegerAllele secondaryFertility = bee.getGenome().getInactiveAllele(BeeChromosomes.FERTILITY);
			guiAlyzer.drawFertilityInfo(transform, primaryFertility.value(), GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(primaryFertility.dominant()), 0);
			guiAlyzer.drawFertilityInfo(transform, secondaryFertility.value(), GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(secondaryFertility.dominant()), 0);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.area"), bee, BeeChromosomes.TERRITORY);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.effect"), bee, BeeChromosomes.EFFECT);
			textLayout.newLine();

			textLayout.endPage(transform);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage2(PoseStack transform, Screen gui, ItemStack itemStack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IBee bee = BeeManager.beeRoot.create(itemStack);
			if (bee == null) {
				return;
			}

			ILifeStage type = BeeManager.beeRoot.getTypes().getType(itemStack);
			if (type == null) {
				return;
			}

			IGenome genome = bee.getGenome();
			IAlleleBeeSpecies primaryAllele = genome.getActiveAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES);
			IAlleleBeeSpecies secondaryAllele = genome.getActiveAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES);

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
			textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

			textLayout.newLine();

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.climate"), ClimateHelper.toDisplay(primaryAllele.getTemperature()),
					ClimateHelper.toDisplay(secondaryAllele.getTemperature()), bee, BeeChromosomes.SPECIES);

			textLayout.newLine();

			IValueAllele<ToleranceType> tempToleranceActive = bee.getGenome().getActiveAllele(BeeChromosomes.TEMPERATURE_TOLERANCE);
			IValueAllele<ToleranceType> tempToleranceInactive = bee.getGenome().getInactiveAllele(BeeChromosomes.TEMPERATURE_TOLERANCE);
			textLayout.drawLine(transform, Component.literal("  ").append(Component.translatable("for.gui.tolerance")), GuiAlyzer.COLUMN_0);
			guiAlyzer.drawToleranceInfo(transform, tempToleranceActive, GuiAlyzer.COLUMN_1);
			guiAlyzer.drawToleranceInfo(transform, tempToleranceInactive, GuiAlyzer.COLUMN_2);

			textLayout.newLine(16);

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.humidity"), ClimateHelper.toDisplay(primaryAllele.getHumidity()),
					ClimateHelper.toDisplay(secondaryAllele.getHumidity()), bee, BeeChromosomes.SPECIES);

			textLayout.newLine();

			IValueAllele<ToleranceType> humidToleranceActive = bee.getGenome().getActiveAllele(BeeChromosomes.HUMIDITY_TOLERANCE);
			IValueAllele<ToleranceType> humidToleranceInactive = bee.getGenome().getInactiveAllele(BeeChromosomes.HUMIDITY_TOLERANCE);
			textLayout.drawLine(transform, Component.literal("  ").append(Component.translatable("for.gui.tolerance")), GuiAlyzer.COLUMN_0);
			guiAlyzer.drawToleranceInfo(transform, humidToleranceActive, GuiAlyzer.COLUMN_1);
			guiAlyzer.drawToleranceInfo(transform, humidToleranceInactive, GuiAlyzer.COLUMN_2);

			textLayout.newLine(16);

			Component yes = Component.translatable("for.yes");
			Component no = Component.translatable("for.no");

			Component diurnal0, diurnal1, nocturnal0, nocturnal1;
			if (genome.getActiveValue(BeeChromosomes.NEVER_SLEEPS)) {
				nocturnal0 = diurnal0 = yes;
			} else {
				nocturnal0 = primaryAllele.isNocturnal() ? yes : no;
				diurnal0 = !primaryAllele.isNocturnal() ? yes : no;
			}
			if (genome.getInactiveValue(BeeChromosomes.NEVER_SLEEPS)) {
				nocturnal1 = diurnal1 = yes;
			} else {
				nocturnal1 = secondaryAllele.isNocturnal() ? yes : no;
				diurnal1 = !secondaryAllele.isNocturnal() ? yes : no;
			}

			textLayout.drawLine(transform, Component.translatable("for.gui.diurnal"), GuiAlyzer.COLUMN_0);
			textLayout.drawLine(transform, diurnal0, GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(false));
			textLayout.drawLine(transform, diurnal1, GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(false));
			textLayout.newLineCompressed();

			textLayout.drawLine(transform, Component.translatable("for.gui.nocturnal"), GuiAlyzer.COLUMN_0);
			textLayout.drawLine(transform, nocturnal0, GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(false));
			textLayout.drawLine(transform, nocturnal1, GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(false));
			textLayout.newLineCompressed();

			Component primary = genome.getActiveValue(BeeChromosomes.TOLERATES_RAIN) ? yes : no;
			Component secondary = genome.getInactiveValue(BeeChromosomes.TOLERATES_RAIN) ? yes : no;

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.flyer"), primary, secondary, bee, BeeChromosomes.TOLERATES_RAIN);

			textLayout.newLineCompressed();

			primary = genome.getActiveValue(BeeChromosomes.CAVE_DWELLING) ? yes : no;
			secondary = genome.getInactiveValue(BeeChromosomes.CAVE_DWELLING) ? yes : no;

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.cave"), primary, secondary, bee, BeeChromosomes.CAVE_DWELLING);

			textLayout.newLine();

			if (type == BeeLifeStage.PRINCESS || type == BeeLifeStage.QUEEN) {
				String displayTextKey = "for.bees.stock.pristine";
				if (!bee.isNatural()) {
					displayTextKey = "for.bees.stock.ignoble";
				}
				Component displayText = Component.translatable(displayTextKey);
				textLayout.drawCenteredLine(transform, displayText, 8, 208, guiAlyzer.getFontColor().get("gui.beealyzer.binomial"));
			}

			if (bee.getGeneration() >= 0) {
				textLayout.newLineCompressed();
				Component displayText = Component.translatable("for.gui.beealyzer.generations", bee.getGeneration());
				textLayout.drawCenteredLine(transform, displayText, 8, 208, guiAlyzer.getFontColor().get("gui.beealyzer.binomial"));
			}

			textLayout.endPage(transform);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage3(PoseStack transform, ItemStack itemStack, Screen gui) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IBee bee = BeeManager.beeRoot.create(itemStack);
			if (bee == null) {
				return;
			}

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();
			WidgetManager widgetManager = guiAlyzer.getWidgetManager();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.beealyzer.produce").append(":"), GuiAlyzer.COLUMN_0);

			textLayout.newLine();

			int x = GuiAlyzer.COLUMN_0;
			for (ItemStack stack : bee.getProduceList()) {
				widgetManager.add(new ItemStackWidget(widgetManager, x, textLayout.getLineY(), stack));

				x += 18;
				if (x > 148) {
					x = GuiAlyzer.COLUMN_0;
					textLayout.newLine();
				}
			}

			textLayout.newLine();
			textLayout.newLine();
			textLayout.newLine();
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.beealyzer.specialty").append(":"), GuiAlyzer.COLUMN_0);
			textLayout.newLine();

			x = GuiAlyzer.COLUMN_0;
			for (ItemStack stack : bee.getSpecialtyList()) {
				widgetManager.add(new ItemStackWidget(widgetManager, x, textLayout.getLineY(), stack));

				x += 18;
				if (x > 148) {
					x = GuiAlyzer.COLUMN_0;
					textLayout.newLine();
				}
			}

			textLayout.endPage(transform);
		}
	}

	@Override
	public Map<ResourceLocation, ItemStack> getIconStacks() {
		return iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("beealyzer");
	}

}
