package forestry.lepidopterology.genetics;

import java.util.function.Function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.api.genetics.gatgets.IDatabaseTab;
import forestry.api.lepidopterology.genetics.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.EnumFlutterType;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.DatabaseElement;
import forestry.core.gui.elements.GuiElementFactory;

@OnlyIn(Dist.CLIENT)
public class ButterflyDatabaseTab implements IDatabaseTab<IButterfly> {
	private final DatabaseMode mode;

	ButterflyDatabaseTab(DatabaseMode mode) {
		this.mode = mode;
	}

	@Override
	public DatabaseMode getMode() {
		return mode;
	}

	@Override
	public void createElements(DatabaseElement database, IButterfly butterfly, ItemStack itemStack) {
		IAlleleButterflySpecies primarySpecies = butterfly.getGenome().getActiveAllele(ButterflyChromosomes.SPECIES);
		IAlleleButterflySpecies secondarySpecies = butterfly.getGenome().getInactiveAllele(ButterflyChromosomes.SPECIES);

		database.label(Component.translatable("for.gui.database.tab." + (mode == DatabaseMode.ACTIVE ? "active" : "inactive") + "_species.name"), Alignment.TOP_CENTER, GuiElementFactory.INSTANCE.databaseTitle);

		database.addLine(Component.translatable("for.gui.species"), ButterflyChromosomes.SPECIES);

		database.addLine(Component.translatable("for.gui.size"), ButterflyChromosomes.SIZE);

		database.addLine(Component.translatable("for.gui.lifespan"), ButterflyChromosomes.LIFESPAN);

		database.addLine(Component.translatable("for.gui.speed"), ButterflyChromosomes.SPEED);

		database.addLine(Component.translatable("for.gui.metabolism"), ButterflyChromosomes.METABOLISM);

		database.addFertilityLine(Component.translatable("for.gui.fertility"), ButterflyChromosomes.FERTILITY, 8);

		database.addLine(Component.translatable("for.gui.flowers"), ButterflyChromosomes.FLOWER_PROVIDER);
		database.addLine(Component.translatable("for.gui.effect"), ButterflyChromosomes.EFFECT);

		Function<Boolean, Component> toleranceText = a -> {
			IAlleleForestrySpecies species = a ? primarySpecies : secondarySpecies;
			return AlleleManager.climateHelper.toDisplay(species.getTemperature());
		};
		database.addLine(Component.translatable("for.gui.climate"), toleranceText, ButterflyChromosomes.TEMPERATURE_TOLERANCE);
		database.addToleranceLine(ButterflyChromosomes.TEMPERATURE_TOLERANCE);

		database.addLine(Component.translatable("for.gui.humidity"), toleranceText, ButterflyChromosomes.HUMIDITY_TOLERANCE);
		database.addToleranceLine(ButterflyChromosomes.HUMIDITY_TOLERANCE);

		Component yes = Component.translatable("for.yes");
		Component no = Component.translatable("for.no");

		{
			Component diurnalFirst;
			Component diurnalSecond;
			Component nocturnalFirst;
			Component nocturnalSecond;
			if (butterfly.getGenome().getActiveValue(ButterflyChromosomes.NOCTURNAL)) {
				nocturnalFirst = diurnalFirst = yes;
			} else {
				nocturnalFirst = primarySpecies.isNocturnal() ? yes : no;
				diurnalFirst = !primarySpecies.isNocturnal() ? yes : no;
			}
			if (butterfly.getGenome().getInactiveValue(ButterflyChromosomes.NOCTURNAL)) {
				nocturnalSecond = diurnalSecond = yes;
			} else {
				nocturnalSecond = secondarySpecies.isNocturnal() ? yes : no;
				diurnalSecond = !secondarySpecies.isNocturnal() ? yes : no;
			}

			database.addLine(Component.translatable("for.gui.diurnal"), (Boolean a) -> a ? diurnalFirst : diurnalSecond, false);
			database.addLine(Component.translatable("for.gui.nocturnal"), (Boolean a) -> a ? nocturnalFirst : nocturnalSecond, false);
		}

		Function<Boolean, Component> flyer = active -> {
            boolean tolerantFlyer = active ? butterfly.getGenome().getActiveValue(ButterflyChromosomes.TOLERATES_RAIN) : butterfly.getGenome().getInactiveValue(ButterflyChromosomes.TOLERATES_RAIN);
			return tolerantFlyer ? yes : no;
        };
		database.addLine(Component.translatable("for.gui.flyer"), flyer, ButterflyChromosomes.TOLERATES_RAIN);

		Function<Boolean, Component> fireResist = active -> {
            boolean fireResistant = active ? butterfly.getGenome().getActiveValue(ButterflyChromosomes.FIRE_RESIST) : butterfly.getGenome().getInactiveValue(ButterflyChromosomes.FIRE_RESIST);
			return fireResistant ? yes : no;
        };
		database.addLine(Component.translatable("for.gui.fireresist"), fireResist, ButterflyChromosomes.FIRE_RESIST);
	}

	@Override
	public ItemStack getIconStack() {
		return ButterflyDefinition.BlueWing.getMemberStack(mode == DatabaseMode.ACTIVE ? EnumFlutterType.BUTTERFLY : EnumFlutterType.CATERPILLAR);
	}
}
