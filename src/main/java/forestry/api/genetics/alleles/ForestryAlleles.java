package forestry.api.genetics.alleles;

import java.util.Locale;
import java.util.function.Function;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;

import forestry.api.IForestryApi;
import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.arboriculture.IFruitProvider;
import forestry.api.core.ToleranceType;
import forestry.apiculture.genetics.alleles.AggressiveBeeEffect;
import forestry.apiculture.genetics.alleles.Creeper;
import forestry.apiculture.genetics.alleles.ExplorationBeeEffect;
import forestry.apiculture.genetics.alleles.FertileBeeEffect;
import forestry.apiculture.genetics.alleles.FungificationBeeEffect;
import forestry.apiculture.genetics.alleles.GlacialBeeEffect;
import forestry.apiculture.genetics.alleles.HeroicBeeEffect;
import forestry.apiculture.genetics.alleles.IgnitionBeeEffect;
import forestry.apiculture.genetics.alleles.MisanthropeBeeEffect;
import forestry.apiculture.genetics.alleles.NoneBeeEffect;
import forestry.apiculture.genetics.alleles.PotionBeeEffect;
import forestry.apiculture.genetics.alleles.RadioactiveBeeEffect;
import forestry.apiculture.genetics.alleles.RepulsionBeeEffect;
import forestry.apiculture.genetics.alleles.ResurrectionBeeEffect;
import forestry.apiculture.genetics.alleles.SnowingBeeEffect;
import forestry.arboriculture.FruitProviderNone;
import forestry.arboriculture.genetics.alleles.AlleleFruit;

import deleteme.Todos;
import static forestry.api.genetics.alleles.ForestryChromosomes.forestry;

/**
 * All alleles defined by base Forestry. Although the field names might seem to suggest that they're
 * only intended for a certain chromosome, allele instances do not have any meaning besides the values
 * they contain. For example, if you need a dominant allele with the value 10, feel free to use {@link #LIFESPAN_SHORTEST}
 * or even copy it to a differently named field even if not for a lifespan-related chromosome.
 */
public class ForestryAlleles {
	public static final IAlleleRegistry FACTORY = IForestryApi.INSTANCE.getAlleleFactory();

	private static final Function<ToleranceType, ResourceLocation> TOLERANCE_NAMING = enumValue -> forestry("tolerance_" + enumValue.name().toLowerCase(Locale.ROOT));

	// Booleans (Most chromosomes do not permit the recessive alleles)
	public static final IBooleanAllele TRUE = FACTORY.booleanAllele(true, true);
	public static final IBooleanAllele FALSE = FACTORY.booleanAllele(false, true);
	public static final IBooleanAllele TRUE_RECESSIVE = FACTORY.booleanAllele(true, false);
	public static final IBooleanAllele FALSE_RECESSIVE = FACTORY.booleanAllele(false, false);

	// Lifespan
	public static final IIntegerAllele LIFESPAN_SHORTEST = FACTORY.intAllele(10, true);
	public static final IIntegerAllele LIFESPAN_SHORTER = FACTORY.intAllele(20, true);
	public static final IIntegerAllele LIFESPAN_SHORT = FACTORY.intAllele(30, true);
	public static final IIntegerAllele LIFESPAN_SHORTENED = FACTORY.intAllele(35, true);
	public static final IIntegerAllele LIFESPAN_NORMAL = FACTORY.intAllele(40);
	public static final IIntegerAllele LIFESPAN_ELONGATED = FACTORY.intAllele(45, true);
	public static final IIntegerAllele LIFESPAN_LONG = FACTORY.intAllele(50);
	public static final IIntegerAllele LIFESPAN_LONGER = FACTORY.intAllele(60);
	public static final IIntegerAllele LIFESPAN_LONGEST = FACTORY.intAllele(70);

	// Fertility
	public static final IIntegerAllele FERTILITY_1 = FACTORY.intAllele(1, true);
	public static final IIntegerAllele FERTILITY_2 = FACTORY.intAllele(2, true);
	public static final IIntegerAllele FERTILITY_3 = FACTORY.intAllele(3);
	public static final IIntegerAllele FERTILITY_4 = FACTORY.intAllele(4);

	// Pollination
	public static final IIntegerAllele POLLINATION_SLOWEST = FACTORY.intAllele(5, true);
	public static final IIntegerAllele POLLINATION_SLOWER = FACTORY.intAllele(10);
	public static final IIntegerAllele POLLINATION_SLOW = FACTORY.intAllele(15);
	public static final IIntegerAllele POLLINATION_AVERAGE = FACTORY.intAllele(20);
	public static final IIntegerAllele POLLINATION_FAST = FACTORY.intAllele(25);
	public static final IIntegerAllele POLLINATION_FASTER = FACTORY.intAllele(30);
	public static final IIntegerAllele POLLINATION_FASTEST = FACTORY.intAllele(35);
	public static final IIntegerAllele POLLINATION_MAXIMUM = FACTORY.intAllele(99, true);

	// Tolerance
	public static final IValueAllele<ToleranceType> TOLERANCE_NONE = FACTORY.valueAllele(ToleranceType.NONE, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_1 = FACTORY.valueAllele(ToleranceType.BOTH_1, true, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_2 = FACTORY.valueAllele(ToleranceType.BOTH_2, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_3 = FACTORY.valueAllele(ToleranceType.BOTH_3, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_4 = FACTORY.valueAllele(ToleranceType.BOTH_4, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_5 = FACTORY.valueAllele(ToleranceType.BOTH_5, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_1 = FACTORY.valueAllele(ToleranceType.UP_1, true, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_2 = FACTORY.valueAllele(ToleranceType.UP_2, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_3 = FACTORY.valueAllele(ToleranceType.UP_3, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_4 = FACTORY.valueAllele(ToleranceType.UP_4, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_5 = FACTORY.valueAllele(ToleranceType.UP_5, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_1 = FACTORY.valueAllele(ToleranceType.DOWN_1, true, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_2 = FACTORY.valueAllele(ToleranceType.DOWN_2, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_3 = FACTORY.valueAllele(ToleranceType.DOWN_3, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_4 = FACTORY.valueAllele(ToleranceType.DOWN_4, TOLERANCE_NAMING);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_5 = FACTORY.valueAllele(ToleranceType.DOWN_5, TOLERANCE_NAMING);

	// Territory
	public static final IValueAllele<Vec3i> TERRITORY_AVERAGE = FACTORY.valueAllele(new Vec3i(9, 6, 9), VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGE = FACTORY.valueAllele(new Vec3i(11, 8, 11), VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGER = FACTORY.valueAllele(new Vec3i(13, 12, 13), VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGEST = FACTORY.valueAllele(new Vec3i(15, 13, 15), VEC3I_NAMING);

	// Flower Type
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_VANILLA = FACTORY.valueAllele(ForestryFlowerType.VANILLA, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_NETHER = FACTORY.valueAllele(ForestryFlowerType.NETHER);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_CACTI = FACTORY.valueAllele(ForestryFlowerType.CACTI);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_MUSHROOMS = FACTORY.valueAllele(ForestryFlowerType.MUSHROOMS);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_END = FACTORY.valueAllele(ForestryFlowerType.END);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_JUNGLE = FACTORY.valueAllele(ForestryFlowerType.JUNGLE);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_SNOW = FACTORY.valueAllele(ForestryFlowerType.SNOW, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_WHEAT = FACTORY.valueAllele(ForestryFlowerType.WHEAT, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_GOURD = FACTORY.valueAllele(ForestryFlowerType.GOURD, true);

	// Bee Effect
	public static final IValueAllele<IBeeEffect> EFFECT_NONE = FACTORY.valueAllele(new NoneBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_AGGRESSIVE = FACTORY.valueAllele(new AggressiveBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_HEROIC = FACTORY.valueAllele(new HeroicBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_BEATIFIC = FACTORY.valueAllele(new PotionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_MIASMIC = FACTORY.valueAllele(new PotionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_MISANTHROPE = FACTORY.valueAllele(new MisanthropeBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_GLACIAL = FACTORY.valueAllele(new GlacialBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_RADIOACTIVE = FACTORY.valueAllele(new RadioactiveBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_CREEPER = FACTORY.valueAllele(new Creeper());
	public static final IValueAllele<IBeeEffect> EFFECT_IGNITION = FACTORY.valueAllele(new IgnitionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_EXPLORATION = FACTORY.valueAllele(new ExplorationBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_FESTIVE_EASTER = FACTORY.valueAllele(new NoneBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_SNOWING = FACTORY.valueAllele(new SnowingBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_DRUNKARD = FACTORY.valueAllele(new PotionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_REANIMATION = FACTORY.valueAllele(new ResurrectionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_RESURRECTION = FACTORY.valueAllele(new ResurrectionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_REPULSION = FACTORY.valueAllele(new RepulsionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_FERTILE = FACTORY.valueAllele(new FertileBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_MYCOPHILIC = FACTORY.valueAllele(new FungificationBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_PATRIOTIC = Todos.todo();

	// Speed
	public static final IFloatAllele SPEED_SLOWEST = FACTORY.floatAllele(0.3f, true);
	public static final IFloatAllele SPEED_SLOWER = FACTORY.floatAllele(0.6f, true);
	public static final IFloatAllele SPEED_SLOW = FACTORY.floatAllele(0.8f, true);
	public static final IFloatAllele SPEED_NORMAL = FACTORY.floatAllele(1.0f);
	public static final IFloatAllele SPEED_FAST = FACTORY.floatAllele(1.2f, true);
	public static final IFloatAllele SPEED_FASTER = FACTORY.floatAllele(1.4f);
	public static final IFloatAllele SPEED_FASTEST = FACTORY.floatAllele(1.7f);

	// Size
	public static final IFloatAllele SIZE_SMALLEST = FACTORY.floatAllele(0.3f);
	public static final IFloatAllele SIZE_SMALLER = FACTORY.floatAllele(0.4f);
	public static final IFloatAllele SIZE_SMALL = FACTORY.floatAllele(0.5f);
	public static final IFloatAllele SIZE_AVERAGE = FACTORY.floatAllele(0.6f);
	public static final IFloatAllele SIZE_LARGE = FACTORY.floatAllele(0.75f);
	public static final IFloatAllele SIZE_LARGER = FACTORY.floatAllele(0.9f);
	public static final IFloatAllele SIZE_LARGEST = FACTORY.floatAllele(1.0f);

	// Sappiness
	public static final IFloatAllele SAPPINESS_LOWEST = FACTORY.floatAllele(0.1f, true);
	public static final IFloatAllele SAPPINESS_LOWER = FACTORY.floatAllele(0.2f, true);
	public static final IFloatAllele SAPPINESS_LOW = FACTORY.floatAllele(0.3f, true);
	public static final IFloatAllele SAPPINESS_AVERAGE = FACTORY.floatAllele(0.4f, true);
	public static final IFloatAllele SAPPINESS_HIGH = FACTORY.floatAllele(0.6f, true);
	public static final IFloatAllele SAPPINESS_HIGHER = FACTORY.floatAllele(0.8f);
	public static final IFloatAllele SAPPINESS_HIGHEST = FACTORY.floatAllele(1.0f);

	// Saplings
	public static final IFloatAllele SAPLINGS_LOWEST = FACTORY.floatAllele(0.01f, true);
	public static final IFloatAllele SAPLINGS_LOWER = FACTORY.floatAllele(0.025f, true);
	public static final IFloatAllele SAPLINGS_LOW = FACTORY.floatAllele(0.035f, true);
	public static final IFloatAllele SAPLINGS_AVERAGE = FACTORY.floatAllele(0.05f, true);
	public static final IFloatAllele SAPLINGS_HIGH = FACTORY.floatAllele(0.1f, true);
	public static final IFloatAllele SAPLINGS_HIGHER = FACTORY.floatAllele(0.2f, true);
	public static final IFloatAllele SAPLINGS_HIGHEST = FACTORY.floatAllele(0.3f, true);

	// Fruits
	public static final IValueAllele<IFruitProvider> FRUIT_NONE = FACTORY.valueAllele(new FruitProviderNone());
	public static final IValueAllele<IFruitProvider> FRUIT_APPLE = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_COCOA = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_CHESTNUT = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_WALNUT = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_CHERRY = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_DATES = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_PAPAYA = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_LEMON = FACTORY.valueAllele();
	public static final IValueAllele<IFruitProvider> FRUIT_PLUM = FACTORY.valueAllele();
}
