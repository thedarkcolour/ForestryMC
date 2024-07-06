/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.lepidopterology.genetics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.products.Product;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.genetics.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.config.Config;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.TextLayoutHelper;
import forestry.lepidopterology.features.LepidopterologyItems;

import genetics.api.GeneticHelper;
import genetics.api.alleles.IAlleleValue;
import genetics.api.individual.IGenome;
import genetics.api.organism.IOrganism;
import genetics.api.organism.IOrganismType;

public enum ButterflyAlyzerPlugin implements IAlyzerPlugin {
	INSTANCE;

	private final Map<ResourceLocation, ItemStack> iconStacks = new HashMap<>();

	ButterflyAlyzerPlugin() {
		NonNullList<ItemStack> butterflyList = NonNullList.create();
		LepidopterologyItems.BUTTERFLY_GE.item().addCreativeItems(butterflyList, false);
		for (ItemStack butterflyStack : butterflyList) {
			IOrganism<?> organism = GeneticHelper.getOrganism(butterflyStack);
			if (organism.isEmpty()) {
				continue;
			}
			IAlleleButterflySpecies species = organism.getAllele(ButterflyChromosomes.SPECIES, true);
			iconStacks.put(species.getRegistryName(), butterflyStack);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage1(PoseStack transform, Screen gui, ItemStack stack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IButterfly butterfly = ButterflyManager.butterflyRoot.create(stack);
			if (butterfly == null) {
				return;
			}
			IOrganismType type = ButterflyManager.butterflyRoot.getTypes().getType(stack);
			if (type == null) {
				return;
			}
			IGenome genome = butterfly.getGenome();

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
			textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

			textLayout.newLine();
			textLayout.newLine();

			guiAlyzer.drawSpeciesRow(transform, Component.translatable("for.gui.species"), butterfly, ButterflyChromosomes.SPECIES, type);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.size"), butterfly, ButterflyChromosomes.SIZE);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.lifespan"), butterfly, ButterflyChromosomes.LIFESPAN);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.speed"), butterfly, ButterflyChromosomes.SPEED);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.metabolism"), butterfly, ButterflyChromosomes.METABOLISM);
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.fertility"), GuiAlyzer.COLUMN_0);
			IAlleleValue<Integer> primaryFertility = genome.getActiveAllele(ButterflyChromosomes.FERTILITY);
			IAlleleValue<Integer> secondaryFertility = genome.getInactiveAllele(ButterflyChromosomes.FERTILITY);
			guiAlyzer.drawFertilityInfo(transform, primaryFertility.getValue(), GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(primaryFertility.isDominant()), 8);
			guiAlyzer.drawFertilityInfo(transform, secondaryFertility.getValue(), GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(secondaryFertility.isDominant()), 8);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.flowers"), butterfly, ButterflyChromosomes.FLOWER_PROVIDER);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.effect"), butterfly, ButterflyChromosomes.EFFECT);
			textLayout.newLine();

			textLayout.endPage(transform);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage2(PoseStack transform, Screen gui, ItemStack stack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IButterfly butterfly = ButterflyManager.butterflyRoot.create(stack);
			if (butterfly == null) {
				return;
			}
			IOrganismType type = ButterflyManager.butterflyRoot.getTypes().getType(stack);
			if (type == null) {
				return;
			}

			IGenome genome = butterfly.getGenome();
			IAlleleButterflySpecies primaryAllele = genome.getActiveAllele(ButterflyChromosomes.SPECIES);
			IAlleleButterflySpecies secondaryAllele = genome.getActiveAllele(ButterflyChromosomes.SPECIES);

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
			textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

			textLayout.newLine();
			textLayout.newLine();

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.climate"),
					AlleleManager.climateHelper.toDisplay(primaryAllele.getTemperature()),
					AlleleManager.climateHelper.toDisplay(secondaryAllele.getTemperature()), butterfly, ButterflyChromosomes.SPECIES);
			textLayout.newLine();

			Component indentedTolerance = Component.literal("  ").append(Component.translatable("for.gui.tolerance"));
			IAlleleValue<EnumTolerance> tempToleranceActive = genome.getActiveAllele(ButterflyChromosomes.TEMPERATURE_TOLERANCE);
			IAlleleValue<EnumTolerance> tempToleranceInactive = genome.getInactiveAllele(ButterflyChromosomes.TEMPERATURE_TOLERANCE);

			textLayout.drawLine(transform, indentedTolerance, GuiAlyzer.COLUMN_0);
			guiAlyzer.drawToleranceInfo(transform, tempToleranceActive, GuiAlyzer.COLUMN_1);
			guiAlyzer.drawToleranceInfo(transform, tempToleranceInactive, GuiAlyzer.COLUMN_2);

			textLayout.newLine();

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.humidity"),
					AlleleManager.climateHelper.toDisplay(primaryAllele.getHumidity()),
					AlleleManager.climateHelper.toDisplay(secondaryAllele.getHumidity()), butterfly, ButterflyChromosomes.SPECIES);
			textLayout.newLine();

			IAlleleValue<EnumTolerance> humidToleranceActive = genome.getActiveAllele(ButterflyChromosomes.HUMIDITY_TOLERANCE);
			IAlleleValue<EnumTolerance> humidToleranceInactive = genome.getInactiveAllele(ButterflyChromosomes.HUMIDITY_TOLERANCE);
			textLayout.drawLine(transform, indentedTolerance, GuiAlyzer.COLUMN_0);
			guiAlyzer.drawToleranceInfo(transform, humidToleranceActive, GuiAlyzer.COLUMN_1);
			guiAlyzer.drawToleranceInfo(transform, humidToleranceInactive, GuiAlyzer.COLUMN_2);

			textLayout.newLine();
			textLayout.newLine();

			Component yes = Component.translatable("for.yes");
			Component no = Component.translatable("for.no");

			Component diurnal0, diurnal1, nocturnal0, nocturnal1;
			if (genome.getActiveValue(ButterflyChromosomes.NOCTURNAL)) {
				nocturnal0 = diurnal0 = yes;
			} else {
				nocturnal0 = primaryAllele.isNocturnal() ? yes : no;
				diurnal0 = !primaryAllele.isNocturnal() ? yes : no;
			}
			if (genome.getActiveValue(ButterflyChromosomes.NOCTURNAL)) {
				nocturnal1 = diurnal1 = yes;
			} else {
				nocturnal1 = secondaryAllele.isNocturnal() ? yes : no;
				diurnal1 = !secondaryAllele.isNocturnal() ? yes : no;
			}

			textLayout.drawLine(transform, Component.translatable("for.gui.diurnal"), GuiAlyzer.COLUMN_0);
			textLayout.drawLine(transform, diurnal0, GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(false));
			textLayout.drawLine(transform, diurnal1, GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(false));
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.nocturnal"), GuiAlyzer.COLUMN_0);
			textLayout.drawLine(transform, nocturnal0, GuiAlyzer.COLUMN_1, guiAlyzer.getColorCoding(false));
			textLayout.drawLine(transform, nocturnal1, GuiAlyzer.COLUMN_2, guiAlyzer.getColorCoding(false));
			textLayout.newLine();

			Component primary = genome.getActiveValue(ButterflyChromosomes.TOLERATES_RAIN) ? yes : no;
			Component secondary = genome.getInactiveValue(ButterflyChromosomes.TOLERATES_RAIN) ? yes : no;

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.flyer"), primary, secondary, butterfly, ButterflyChromosomes.TOLERATES_RAIN);
			textLayout.newLine();

			primary = genome.getActiveValue(ButterflyChromosomes.FIRE_RESIST) ? yes : no;
			secondary = genome.getInactiveValue(ButterflyChromosomes.FIRE_RESIST) ? yes : no;

			guiAlyzer.drawRow(transform, Component.translatable("for.gui.fireresist"), primary, secondary, butterfly, ButterflyChromosomes.FIRE_RESIST);

			textLayout.endPage(transform);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawAnalyticsPage3(PoseStack transform, ItemStack itemStack, Screen gui) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			IButterfly butterfly = ButterflyManager.butterflyRoot.create(itemStack);
			if (butterfly == null) {
				return;
			}

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.loot.butterfly").append(":"), GuiAlyzer.COLUMN_0);
			textLayout.newLine();

			int guiLeft = guiAlyzer.getGuiLeft();
			int guiTop = guiAlyzer.getGuiTop();
			int x = GuiAlyzer.COLUMN_0;

			for (Product product : butterfly.getGenome().getPrimary(IAlleleButterflySpecies.class).getButterflyLoot().getPossibleProducts()) {
				itemRenderer.renderGuiItem(product.getStack(), guiLeft + x, guiTop + textLayout.getLineY());
				x += 18;
				if (x > 148) {
					x = GuiAlyzer.COLUMN_0;
					textLayout.newLine();
				}
			}

			textLayout.newLine();
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.loot.caterpillar").append(":"), GuiAlyzer.COLUMN_0);
			textLayout.newLine();

			x = GuiAlyzer.COLUMN_0;
			for (Product product : butterfly.getGenome().getPrimary(IAlleleButterflySpecies.class).getCaterpillarLoot().getPossibleProducts()) {
				itemRenderer.renderGuiItem(product.getStack(), guiLeft + x, guiTop + textLayout.getLineY());
				x += 18;
				if (x > 148) {
					x = GuiAlyzer.COLUMN_0;
					textLayout.newLine();
				}
			}

			textLayout.newLine();
			textLayout.newLine();

			textLayout.drawLine(transform, Component.translatable("for.gui.loot.cocoon").append(":"), GuiAlyzer.COLUMN_0);
			textLayout.newLine();

			x = GuiAlyzer.COLUMN_0;
			for (Product product : butterfly.getGenome().getActiveAllele(ButterflyChromosomes.COCOON).getCocoonLoot().getPossibleProducts()) {
				itemRenderer.renderGuiItem(product.getStack(), guiLeft + x, guiTop + textLayout.getLineY());
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
		return Config.hints.get("flutterlyzer");
	}
}
