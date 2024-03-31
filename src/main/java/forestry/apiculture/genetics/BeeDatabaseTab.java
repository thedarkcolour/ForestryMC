package forestry.apiculture.genetics;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.genetics.BeeChromosomes;
import forestry.api.apiculture.genetics.EnumBeeType;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.api.genetics.gatgets.IDatabaseTab;
import forestry.api.lepidopterology.genetics.ButterflyChromosomes;
import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.DatabaseElement;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.utils.StringUtil;
import forestry.core.utils.Translator;

import genetics.api.organism.IOrganismType;

@OnlyIn(Dist.CLIENT)
public class BeeDatabaseTab implements IDatabaseTab<IBee> {

	private final DatabaseMode mode;

	BeeDatabaseTab(DatabaseMode mode) {
		this.mode = mode;
	}

	@Override
	public DatabaseMode getMode() {
		return mode;
	}

	@Override
	public void createElements(DatabaseElement container, IBee bee, ItemStack itemStack) {
		Optional<IOrganismType> optionalType = BeeManager.beeRoot.getTypes().getType(itemStack);
		if (!optionalType.isPresent()) {
			return;
		}
		IOrganismType type = optionalType.get();
		IAlleleBeeSpecies primarySpecies = bee.getGenome().getActiveAllele(BeeChromosomes.SPECIES);
		IAlleleBeeSpecies secondarySpecies = bee.getGenome().getInactiveAllele(BeeChromosomes.SPECIES);

		container.label(Component.translatable("for.gui.database.tab." + (mode == DatabaseMode.ACTIVE ? "active" : "inactive") + "_species"), Alignment.TOP_CENTER, GuiElementFactory.INSTANCE.databaseTitle);

		container.addLine(Component.translatable("for.gui.species"), BeeChromosomes.SPECIES);

		Function<Boolean, Component> toleranceText = a -> {
			IAlleleForestrySpecies species = a ? primarySpecies : secondarySpecies;
			return AlleleManager.climateHelper.toDisplay(species.getTemperature());
		};
		container.addLine(Component.translatable("for.gui.climate"), toleranceText, BeeChromosomes.TEMPERATURE_TOLERANCE);
		container.addToleranceLine(BeeChromosomes.TEMPERATURE_TOLERANCE);

		container.addLine(Component.translatable("for.gui.humidity"), toleranceText, BeeChromosomes.HUMIDITY_TOLERANCE);
		container.addToleranceLine(BeeChromosomes.HUMIDITY_TOLERANCE);

		container.addLine(Component.translatable("for.gui.lifespan"), BeeChromosomes.LIFESPAN);

		container.addLine(Component.translatable("for.gui.speed"), BeeChromosomes.SPEED);
		container.addLine(Component.translatable("for.gui.pollination"), BeeChromosomes.FLOWERING);
		container.addLine(Component.translatable("for.gui.flowers"), BeeChromosomes.FLOWER_PROVIDER);

		container.addFertilityLine(Component.translatable("for.gui.fertility"), BeeChromosomes.FERTILITY, 0);

		container.addLine(Component.translatable("for.gui.area"), BeeChromosomes.TERRITORY);
		container.addLine(Component.translatable("for.gui.effect"), BeeChromosomes.EFFECT);

		Component yes = Component.translatable("for.yes");
		Component no = Component.translatable("for.no");

		Component diurnal, nocturnal;
		if (mode == DatabaseMode.ACTIVE) {
			if (bee.getGenome().getActiveValue(BeeChromosomes.NEVER_SLEEPS)) {
				nocturnal = diurnal = yes;
			} else {
				nocturnal = primarySpecies.isNocturnal() ? yes : no;
				diurnal = !primarySpecies.isNocturnal() ? yes : no;
			}
		} else {
			if (bee.getGenome().getInactiveValue(ButterflyChromosomes.NOCTURNAL)) {
				nocturnal = diurnal = yes;
			} else {
				nocturnal = secondarySpecies.isNocturnal() ? yes : no;
				diurnal = !secondarySpecies.isNocturnal() ? yes : no;
			}
		}

		container.addLine(Component.translatable("for.gui.diurnal"), diurnal, false);

		container.addLine(Component.translatable("for.gui.nocturnal"), nocturnal, false);

		Function<Boolean, Component> flyer = active -> {
			boolean toleratesRain = active ? bee.getGenome().getActiveValue(BeeChromosomes.TOLERATES_RAIN) : bee.getGenome().getInactiveValue(BeeChromosomes.TOLERATES_RAIN);
			return toleratesRain ? yes : no;
        };
		container.addLine(Component.translatable("for.gui.flyer"), flyer, BeeChromosomes.TOLERATES_RAIN);

		Function<Boolean, Component> cave = active -> {
			boolean caveDwelling = active ? bee.getGenome().getActiveValue(BeeChromosomes.CAVE_DWELLING) : bee.getGenome().getInactiveValue(BeeChromosomes.CAVE_DWELLING);
            return caveDwelling ? yes : no;
        };
		container.addLine(Component.translatable("for.gui.cave"), cave, BeeChromosomes.CAVE_DWELLING);

		if (type == EnumBeeType.PRINCESS || type == EnumBeeType.QUEEN) {
			Component displayTextKey = Component.translatable("for.bees.stock.pristine");
			if (!bee.isNatural()) {
				displayTextKey = Component.translatable("for.bees.stock.ignoble");
			}
			container.label(displayTextKey, Alignment.TOP_CENTER, GuiElementFactory.INSTANCE.binomial);
		}
	}

	@Override
	public ItemStack getIconStack() {
		return BeeDefinition.MEADOWS.getMemberStack(mode == DatabaseMode.ACTIVE ? EnumBeeType.PRINCESS : EnumBeeType.DRONE);
	}
}
