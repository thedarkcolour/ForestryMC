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
package forestry.arboriculture.genetics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.ForestryCapabilities;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.core.config.Config;
import forestry.core.genetics.ItemGE;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.TextLayoutHelper;
import forestry.core.gui.widgets.ItemStackWidget;
import forestry.core.gui.widgets.WidgetManager;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.ILifeStage;
import forestry.core.utils.SpeciesUtil;

public enum TreeAlyzerPlugin implements IAlyzerPlugin {
	INSTANCE;

	private final Map<ResourceLocation, ItemStack> iconStacks = new HashMap<>();

	TreeAlyzerPlugin() {
		NonNullList<ItemStack> treeList = NonNullList.create();
		ItemGE.addCreativeItems(TreeLifeStage.SAPLING, treeList, false, SpeciesUtil.TREE_TYPE.get());
		for (ItemStack treeStack : treeList) {
			IIndividualCapability<?> organism = GeneticHelper.getOrganism(treeStack);
			if (organism.isEmpty()) {
				continue;
			}
			IAlleleTreeSpecies species = organism.getAllele(TreeChromosomes.SPECIES, true);
			iconStacks.put(species.getId(), treeStack);
		}
	}

	@Override
	public void drawAnalyticsPage1(PoseStack transform, Screen gui, ItemStack stack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			ITree tree = SpeciesUtil.TREE_TYPE.get().create(stack);
			if (tree == null) {
				return;
			}
			ILifeStage type = SpeciesUtil.TREE_TYPE.get().getTypes().getType(stack);
			if (type == null) {
				return;
			}
			IGenome genome = tree.getGenome();

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
			textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

			textLayout.newLine();
			textLayout.newLine();

			guiAlyzer.drawSpeciesRow(transform, Component.translatable("for.gui.species"), tree, TreeChromosomes.SPECIES, type);
			textLayout.newLine();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.saplings"), tree, TreeChromosomes.SAPLINGS);
			textLayout.newLineCompressed();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.maturity"), tree, TreeChromosomes.MATURATION);
			textLayout.newLineCompressed();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.height"), tree, TreeChromosomes.HEIGHT);
			textLayout.newLineCompressed();

			int activeGirth = genome.getActiveValue(TreeChromosomes.GIRTH);
			int inactiveGirth = genome.getInactiveValue(TreeChromosomes.GIRTH);
			textLayout.drawLine(transform, Component.translatable("for.gui.girth"), GuiAlyzer.COLUMN_0);
			guiAlyzer.drawLine(transform, String.format("%sx%s", activeGirth, activeGirth), GuiAlyzer.COLUMN_1, tree, TreeChromosomes.GIRTH, false);
			guiAlyzer.drawLine(transform, String.format("%sx%s", inactiveGirth, inactiveGirth), GuiAlyzer.COLUMN_2, tree, TreeChromosomes.GIRTH, true);

			textLayout.newLineCompressed();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.yield"), tree, TreeChromosomes.YIELD);
			textLayout.newLineCompressed();
			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.sappiness"), tree, TreeChromosomes.SAPPINESS);
			textLayout.newLineCompressed();

			guiAlyzer.drawChromosomeRow(transform, Component.translatable("for.gui.effect"), tree, TreeChromosomes.EFFECT);

			textLayout.endPage(transform);
		}
	}

	@Override
	public void drawAnalyticsPage2(PoseStack transform, Screen gui, ItemStack stack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			stack.getCapability(ForestryCapabilities.INDIVIDUAL).ifPresent(individual -> {
				if (individual instanceof ITree tree) {
					IGenome genome = tree.getGenome();
					ITreeSpecies primary = genome.getActiveValue(TreeChromosomes.SPECIES);
					ITreeSpecies secondary = genome.getInactiveValue(TreeChromosomes.SPECIES);
					IValueAllele<IFruit> activeFruit = genome.getActiveAllele(TreeChromosomes.FRUITS);
					IValueAllele<IFruit> inactiveFruit = genome.getInactiveAllele(TreeChromosomes.FRUITS);

					TextLayoutHelper textLayout = guiAlyzer.getTextLayout();

					textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

					int speciesDominance0 = guiAlyzer.getColorCoding(primary.isDominant());
					int speciesDominance1 = guiAlyzer.getColorCoding(secondary.isDominant());

					textLayout.drawLine(transform, Component.translatable("for.gui.active"), GuiAlyzer.COLUMN_1);
					textLayout.drawLine(transform, Component.translatable("for.gui.inactive"), GuiAlyzer.COLUMN_2);

					textLayout.newLine();
					textLayout.newLine();

					Component yes = Component.translatable("for.yes");
					Component no = Component.translatable("for.no");

					Component fireproofActive = genome.getActiveValue(TreeChromosomes.FIREPROOF) ? yes : no;
					Component fireproofInactive = genome.getInactiveValue(TreeChromosomes.FIREPROOF) ? yes : no;

					guiAlyzer.drawRow(transform, Component.translatable("for.gui.fireproof"), fireproofActive, fireproofInactive, tree, TreeChromosomes.FIREPROOF);

					textLayout.newLine();
/*
					// todo this was PlantType, displayed as "soil". should there be soil types for trees?
					textLayout.drawLine(transform, Component.translatable("for.gui.native"), GuiAlyzer.COLUMN_0);
					textLayout.drawLine(transform, Component.translatable("for.gui." + primary.getPlantType().getName()), GuiAlyzer.COLUMN_1,
							speciesDominance0);
					textLayout.drawLine(transform, Component.translatable("for.gui." + secondary.getPlantType().getName()), GuiAlyzer.COLUMN_2,
							speciesDominance1);

					textLayout.newLine();

					 FRUIT FAMILIES
					textLayout.drawLine(transform, Component.translatable("for.gui.supports"), GuiAlyzer.COLUMN_0);
					List<IFruitFamily> families0 = new ArrayList<>(primary.getSuitableFruit());
					List<IFruitFamily> families1 = new ArrayList<>(secondary.getSuitableFruit());

					int max = Math.max(families0.size(), families1.size());
					for (int i = 0; i < max; i++) {
						if (i > 0) {
							textLayout.newLineCompressed();
						}

						if (families0.size() > i) {
							textLayout.drawLine(transform, families0.get(i).getName(), GuiAlyzer.COLUMN_1, speciesDominance0);
						}
						if (families1.size() > i) {
							textLayout.drawLine(transform, families1.get(i).getName(), GuiAlyzer.COLUMN_2, speciesDominance1);
						}

					}

					textLayout.newLine();

					int fruitDominance0 = guiAlyzer.getColorCoding(activeFruit.dominant());
					int fruitDominance1 = guiAlyzer.getColorCoding(inactiveFruit.dominant());

					textLayout.drawLine(transform, Component.translatable("for.gui.fruits"), GuiAlyzer.COLUMN_0);
					ChatFormatting strike = ChatFormatting.RESET;
					if (!tree.canBearFruit() && activeFruit != AlleleFruits.fruitNone) {
						strike = ChatFormatting.STRIKETHROUGH;
					}
					textLayout.drawLine(transform, activeFruit.getProvider().getDescription().withStyle(strike), GuiAlyzer.COLUMN_1, fruitDominance0);

					strike = ChatFormatting.RESET;
					if (!secondary.getSuitableFruit().contains(inactiveFruit.getProvider().getFamily()) && inactiveFruit != AlleleFruits.fruitNone) {
						strike = ChatFormatting.STRIKETHROUGH;
					}
					textLayout.drawLine(transform, inactiveFruit.getProvider().getDescription().withStyle(strike), GuiAlyzer.COLUMN_2, fruitDominance1);

					textLayout.newLine();

					textLayout.drawLine(transform, Component.translatable("for.gui.family"), GuiAlyzer.COLUMN_0);

					if (!primaryFamily.getUID().equals(EnumFruitFamily.NONE.getUID())) {
						textLayout.drawLine(transform, primaryFamily.getName(), GuiAlyzer.COLUMN_1, fruitDominance0);
					}
					if (!secondaryFamily.getUID().equals(EnumFruitFamily.NONE.getUID())) {
						textLayout.drawLine(transform, secondaryFamily.getName(), GuiAlyzer.COLUMN_2, fruitDominance1);
					}*/

					textLayout.endPage(transform);
				}
			});
		}
	}

	@Override
	public void drawAnalyticsPage3(PoseStack transform, Screen gui, ItemStack itemStack) {
		if (gui instanceof GuiAlyzer guiAlyzer) {
			ITree tree = SpeciesUtil.TREE_TYPE.get().create(itemStack);
			if (tree == null) {
				return;
			}

			TextLayoutHelper textLayout = guiAlyzer.getTextLayout();
			WidgetManager widgetManager = guiAlyzer.getWidgetManager();

			textLayout.startPage(transform, GuiAlyzer.COLUMN_0, GuiAlyzer.COLUMN_1, GuiAlyzer.COLUMN_2);

			textLayout.drawLine(transform, Component.translatable("for.gui.beealyzer.produce").append(":"), GuiAlyzer.COLUMN_0);
			textLayout.newLine();

			int x = GuiAlyzer.COLUMN_0;
			for (ItemStack stack : tree.getProducts().getPossibleStacks()) {
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
			for (ItemStack stack : tree.getSpecialties().getPossibleStacks()) {
				Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, guiAlyzer.getGuiLeft() + x, guiAlyzer.getGuiTop() + textLayout.getLineY());
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
		return Config.hints.get("treealyzer");
	}

}
