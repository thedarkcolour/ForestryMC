package forestry.arboriculture.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Style;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.arboriculture.EnumFruitFamily;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.arboriculture.genetics.IAlleleFruit;
import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.api.genetics.gatgets.IDatabaseTab;
import forestry.arboriculture.genetics.alleles.AlleleFruits;
import forestry.core.gui.GuiConstants;
import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.DatabaseElement;
import forestry.core.gui.elements.GuiElementFactory;

import forestry.api.genetics.alleles.IValueAllele;

@OnlyIn(Dist.CLIENT)
public class TreeDatabaseTab implements IDatabaseTab<ITree> {
	private final DatabaseMode mode;

	TreeDatabaseTab(DatabaseMode mode) {
		this.mode = mode;
	}

	@Override
	public DatabaseMode getMode() {
		return mode;
	}

	@Override
	public void createElements(DatabaseElement container, ITree tree, ItemStack itemStack) {
		IAlleleTreeSpecies primarySpecies = tree.getGenome().getActiveAllele(TreeChromosomes.SPECIES);
		IAlleleTreeSpecies species = mode == DatabaseMode.ACTIVE ? primarySpecies : tree.getGenome().getInactiveAllele(TreeChromosomes.SPECIES);
		Style speciesStyle = GuiElementFactory.INSTANCE.getStateStyle(species.isDominant());

		container.translated("for.gui.database.tab." + (mode == DatabaseMode.ACTIVE ? "active" : "inactive") + "_species.name").setStyle(GuiElementFactory.INSTANCE.databaseTitle).setAlign(Alignment.TOP_CENTER);

		container.addLine(Component.translatable("for.gui.species"), TreeChromosomes.SPECIES);

		container.addLine(Component.translatable("for.gui.saplings"), TreeChromosomes.FERTILITY);
		container.addLine(Component.translatable("for.gui.maturity"), TreeChromosomes.MATURATION);
		container.addLine(Component.translatable("for.gui.height"), TreeChromosomes.HEIGHT);

		container.addLine(Component.translatable("for.gui.girth"), (IValueAllele<Integer> girth, Boolean active) -> Component.literal(String.format("%sx%s", girth.value(), girth.value())), TreeChromosomes.GIRTH);

		container.addLine(Component.translatable("for.gui.yield"), TreeChromosomes.YIELD);
		container.addLine(Component.translatable("for.gui.sappiness"), TreeChromosomes.SAPPINESS);

		container.addLine(Component.translatable("for.gui.effect"), TreeChromosomes.EFFECT);

		container.addLine(Component.translatable("for.gui.native"), Component.translatable("for.gui." + primarySpecies.getPlantType().toString().toLowerCase(Locale.ENGLISH)), species.isDominant());

		container.label(Component.translatable("for.gui.supports"), Alignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
		List<IFruitFamily> families = new ArrayList<>(primarySpecies.getSuitableFruit());

		for (IFruitFamily fruitFamily : families) {
			container.label(fruitFamily.getName(), Alignment.TOP_CENTER, speciesStyle);
		}

		IAlleleFruit fruit = mode == DatabaseMode.ACTIVE ? tree.getGenome().getActiveAllele(TreeChromosomes.FRUITS) : tree.getGenome().getInactiveAllele(TreeChromosomes.FRUITS);
		Style textStyle = GuiElementFactory.INSTANCE.getStateStyle(tree.getGenome().getActiveAllele(TreeChromosomes.FRUITS).dominant());

		container.translated("for.gui.fruits").setStyle(GuiConstants.UNDERLINED_STYLE).setAlign(Alignment.TOP_CENTER);
		Style fruitStyle = textStyle;

		if (!species.getSuitableFruit().contains(fruit.getProvider().getFamily()) && fruit != AlleleFruits.fruitNone) {
			fruitStyle = fruitStyle.withStrikethrough(true);
		}
		container.label(fruit.getProvider().getDescription()).setStyle(fruitStyle).setAlign(Alignment.TOP_CENTER);

		IFruitFamily family = fruit.getProvider().getFamily();

		if (!family.getUID().equals(EnumFruitFamily.NONE.getUID())) {
			container.label(Component.translatable("for.gui.family"), Alignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
			container.label(family.getName(), Alignment.TOP_CENTER, textStyle);
		}

	}

	@Override
	public ItemStack getIconStack() {
        // todo revert to Cherry
		return TreeDefinition.Oak.getMemberStack(mode == DatabaseMode.ACTIVE ? TreeLifeStage.SAPLING : TreeLifeStage.POLLEN);
	}
}
