package forestry.api.genetics.alleles;

import java.util.List;

import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import forestry.api.IForestryApi;
import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ILeafEffect;
import forestry.api.core.ToleranceType;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.IButterflyEffect;
import forestry.apiculture.genetics.effects.AggressiveBeeEffect;
import forestry.apiculture.genetics.effects.Creeper;
import forestry.apiculture.genetics.effects.ExplorationBeeEffect;
import forestry.apiculture.genetics.effects.FertileBeeEffect;
import forestry.apiculture.genetics.effects.FungificationBeeEffect;
import forestry.apiculture.genetics.effects.GlacialBeeEffect;
import forestry.apiculture.genetics.effects.HeroicBeeEffect;
import forestry.apiculture.genetics.effects.IgnitionBeeEffect;
import forestry.apiculture.genetics.effects.MisanthropeBeeEffect;
import forestry.apiculture.genetics.effects.DummyBeeEffect;
import forestry.apiculture.genetics.effects.PotionBeeEffect;
import forestry.apiculture.genetics.effects.RadioactiveBeeEffect;
import forestry.apiculture.genetics.effects.RepulsionBeeEffect;
import forestry.apiculture.genetics.effects.ResurrectionBeeEffect;
import forestry.apiculture.genetics.effects.SnowingBeeEffect;
import forestry.arboriculture.DummyFruit;
import forestry.arboriculture.PodFruit;
import forestry.arboriculture.RipeningFruit;

import deleteme.Todos;
import static forestry.api.ForestryConstants.MOD_ID;

/**
 * All alleles defined by base Forestry. Although the field names might seem to suggest that they're
 * only intended for a certain chromosome, allele instances do not have any meaning besides the values
 * they contain. For example, if you need a dominant allele with the value 10, feel free to use {@link #LIFESPAN_SHORTEST}
 * or even copy it to a differently named field even if not for a lifespan-related chromosome.
 */
public class ForestryAlleles {
	public static final IAlleleManager REGISTRY = IForestryApi.INSTANCE.getAlleleManager();

	// Booleans (Most chromosomes do not permit the recessive alleles)
	public static final IBooleanAllele TRUE = REGISTRY.booleanAllele(true, true);
	public static final IBooleanAllele FALSE = REGISTRY.booleanAllele(false, true);
	public static final IBooleanAllele TRUE_RECESSIVE = REGISTRY.booleanAllele(true, false);
	public static final IBooleanAllele FALSE_RECESSIVE = REGISTRY.booleanAllele(false, false);

	public static final List<IBooleanAllele> DEFAULT_BOOLEANS = List.of(TRUE, FALSE);

	// Lifespan
	public static final IIntegerAllele LIFESPAN_SHORTEST = REGISTRY.intAllele(10, true);
	public static final IIntegerAllele LIFESPAN_SHORTER = REGISTRY.intAllele(20, true);
	public static final IIntegerAllele LIFESPAN_SHORT = REGISTRY.intAllele(30, true);
	public static final IIntegerAllele LIFESPAN_SHORTENED = REGISTRY.intAllele(35, true);
	public static final IIntegerAllele LIFESPAN_NORMAL = REGISTRY.intAllele(40);
	public static final IIntegerAllele LIFESPAN_ELONGATED = REGISTRY.intAllele(45, true);
	public static final IIntegerAllele LIFESPAN_LONG = REGISTRY.intAllele(50);
	public static final IIntegerAllele LIFESPAN_LONGER = REGISTRY.intAllele(60);
	public static final IIntegerAllele LIFESPAN_LONGEST = REGISTRY.intAllele(70);

	// Fertility
	public static final IIntegerAllele FERTILITY_1 = REGISTRY.intAllele(1, true);
	public static final IIntegerAllele FERTILITY_2 = REGISTRY.intAllele(2, true);
	public static final IIntegerAllele FERTILITY_3 = REGISTRY.intAllele(3);
	public static final IIntegerAllele FERTILITY_4 = REGISTRY.intAllele(4);
	// Fertilities above 5 are only used by Butterflies
	public static final IIntegerAllele FERTILITY_5 = REGISTRY.intAllele(5);
	public static final IIntegerAllele FERTILITY_6 = REGISTRY.intAllele(6);
	public static final IIntegerAllele FERTILITY_7 = REGISTRY.intAllele(7);
	public static final IIntegerAllele FERTILITY_8 = REGISTRY.intAllele(8);
	public static final IIntegerAllele FERTILITY_9 = REGISTRY.intAllele(9);
	public static final IIntegerAllele FERTILITY_10 = REGISTRY.intAllele(10);

	public static final List<IIntegerAllele> BEE_FERTILITIES = List.of(FERTILITY_1, FERTILITY_2, FERTILITY_3, FERTILITY_4);
	public static final List<IIntegerAllele> BUTTERFLY_FERTILITIES = List.of(FERTILITY_1, FERTILITY_2, FERTILITY_3, FERTILITY_4, FERTILITY_5, FERTILITY_6, FERTILITY_7, FERTILITY_8, FERTILITY_9, FERTILITY_10);

	// Pollination
	public static final IIntegerAllele POLLINATION_SLOWEST = REGISTRY.intAllele(5, true);
	public static final IIntegerAllele POLLINATION_SLOWER = REGISTRY.intAllele(10);
	public static final IIntegerAllele POLLINATION_SLOW = REGISTRY.intAllele(15);
	public static final IIntegerAllele POLLINATION_AVERAGE = REGISTRY.intAllele(20);
	public static final IIntegerAllele POLLINATION_FAST = REGISTRY.intAllele(25);
	public static final IIntegerAllele POLLINATION_FASTER = REGISTRY.intAllele(30);
	public static final IIntegerAllele POLLINATION_FASTEST = REGISTRY.intAllele(35);
	public static final IIntegerAllele POLLINATION_MAXIMUM = REGISTRY.intAllele(99, true);

	public static final List<IIntegerAllele> DEFAULT_POLLINATIONS = List.of(POLLINATION_SLOWEST, POLLINATION_SLOWER, POLLINATION_SLOW, POLLINATION_AVERAGE, POLLINATION_FAST, POLLINATION_FASTER, POLLINATION_FASTEST, POLLINATION_MAXIMUM);

	// Tolerance
	public static final IValueAllele<ToleranceType> TOLERANCE_NONE = REGISTRY.valueAllele(ToleranceType.NONE);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_1 = REGISTRY.valueAllele(ToleranceType.BOTH_1, true);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_2 = REGISTRY.valueAllele(ToleranceType.BOTH_2);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_3 = REGISTRY.valueAllele(ToleranceType.BOTH_3);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_4 = REGISTRY.valueAllele(ToleranceType.BOTH_4);
	public static final IValueAllele<ToleranceType> TOLERANCE_BOTH_5 = REGISTRY.valueAllele(ToleranceType.BOTH_5);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_1 = REGISTRY.valueAllele(ToleranceType.UP_1, true);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_2 = REGISTRY.valueAllele(ToleranceType.UP_2);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_3 = REGISTRY.valueAllele(ToleranceType.UP_3);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_4 = REGISTRY.valueAllele(ToleranceType.UP_4);
	public static final IValueAllele<ToleranceType> TOLERANCE_UP_5 = REGISTRY.valueAllele(ToleranceType.UP_5);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_1 = REGISTRY.valueAllele(ToleranceType.DOWN_1, true);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_2 = REGISTRY.valueAllele(ToleranceType.DOWN_2);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_3 = REGISTRY.valueAllele(ToleranceType.DOWN_3);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_4 = REGISTRY.valueAllele(ToleranceType.DOWN_4);
	public static final IValueAllele<ToleranceType> TOLERANCE_DOWN_5 = REGISTRY.valueAllele(ToleranceType.DOWN_5);
	
	public static final List<IValueAllele<ToleranceType>> DEFAULT_TEMPERATURE_TOLERANCES = List.of(TOLERANCE_NONE, TOLERANCE_BOTH_1, TOLERANCE_BOTH_2, TOLERANCE_BOTH_3, TOLERANCE_BOTH_4, TOLERANCE_BOTH_5, TOLERANCE_UP_1, TOLERANCE_UP_2, TOLERANCE_UP_3, TOLERANCE_UP_4, TOLERANCE_UP_5, TOLERANCE_DOWN_1, TOLERANCE_DOWN_2, TOLERANCE_DOWN_3, TOLERANCE_DOWN_4, TOLERANCE_DOWN_5);
	public static final List<IValueAllele<ToleranceType>> DEFAULT_HUMIDITY_TOLERANCES = List.of(TOLERANCE_NONE, TOLERANCE_BOTH_1, TOLERANCE_BOTH_2, TOLERANCE_UP_1, TOLERANCE_UP_2, TOLERANCE_DOWN_1, TOLERANCE_DOWN_2);

	// Territory
	public static final IValueAllele<Vec3i> TERRITORY_AVERAGE = REGISTRY.valueAllele(new Vec3i(9, 6, 9), IAlleleNaming.VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGE = REGISTRY.valueAllele(new Vec3i(11, 8, 11), IAlleleNaming.VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGER = REGISTRY.valueAllele(new Vec3i(13, 12, 13), IAlleleNaming.VEC3I_NAMING);
	public static final IValueAllele<Vec3i> TERRITORY_LARGEST = REGISTRY.valueAllele(new Vec3i(15, 13, 15), IAlleleNaming.VEC3I_NAMING);
	
	public static final List<IValueAllele<Vec3i>> DEFAULT_TERRITORIES = List.of(TERRITORY_AVERAGE, TERRITORY_LARGE, TERRITORY_LARGER, TERRITORY_LARGEST);

	// Flower Type
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_VANILLA = REGISTRY.valueAllele(ForestryFlowerType.VANILLA, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_NETHER = REGISTRY.valueAllele(ForestryFlowerType.NETHER);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_CACTI = REGISTRY.valueAllele(ForestryFlowerType.CACTI);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_MUSHROOMS = REGISTRY.valueAllele(ForestryFlowerType.MUSHROOMS);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_END = REGISTRY.valueAllele(ForestryFlowerType.END);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_JUNGLE = REGISTRY.valueAllele(ForestryFlowerType.JUNGLE);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_SNOW = REGISTRY.valueAllele(ForestryFlowerType.SNOW, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_WHEAT = REGISTRY.valueAllele(ForestryFlowerType.WHEAT, true);
	public static final IValueAllele<IFlowerType> FLOWER_TYPE_GOURD = REGISTRY.valueAllele(ForestryFlowerType.GOURD, true);

	// Bee Effect
	public static final IValueAllele<IBeeEffect> EFFECT_NONE = REGISTRY.valueAllele(new DummyBeeEffect(MOD_ID, "none"), true);
	public static final IValueAllele<IBeeEffect> EFFECT_AGGRESSIVE = REGISTRY.valueAllele(new AggressiveBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_HEROIC = REGISTRY.valueAllele(new HeroicBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_BEATIFIC = REGISTRY.valueAllele(new PotionBeeEffect(MOD_ID, "beatific", MobEffects.REGENERATION, 100));
	public static final IValueAllele<IBeeEffect> EFFECT_MIASMIC = REGISTRY.valueAllele(new PotionBeeEffect(MOD_ID, "miasmic", MobEffects.POISON, 600, 100, 0.1f));
	public static final IValueAllele<IBeeEffect> EFFECT_MISANTHROPE = REGISTRY.valueAllele(new MisanthropeBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_GLACIAL = REGISTRY.valueAllele(new GlacialBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_RADIOACTIVE = REGISTRY.valueAllele(new RadioactiveBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_CREEPER = REGISTRY.valueAllele(new Creeper());
	public static final IValueAllele<IBeeEffect> EFFECT_IGNITION = REGISTRY.valueAllele(new IgnitionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_EXPLORATION = REGISTRY.valueAllele(new ExplorationBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_FESTIVE_EASTER = REGISTRY.valueAllele(new DummyBeeEffect(MOD_ID, "easter"), true);
	public static final IValueAllele<IBeeEffect> EFFECT_SNOWING = REGISTRY.valueAllele(new SnowingBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_DRUNKARD = REGISTRY.valueAllele(new PotionBeeEffect(MOD_ID, "drunkard", MobEffects.CONFUSION, 100));
	public static final IValueAllele<IBeeEffect> EFFECT_REANIMATION = REGISTRY.valueAllele(new ResurrectionBeeEffect("reanimation", ResurrectionBeeEffect.getReanimationList()));
	public static final IValueAllele<IBeeEffect> EFFECT_RESURRECTION = REGISTRY.valueAllele(new ResurrectionBeeEffect("resurrection", ResurrectionBeeEffect.getResurrectionList()));
	public static final IValueAllele<IBeeEffect> EFFECT_REPULSION = REGISTRY.valueAllele(new RepulsionBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_FERTILE = REGISTRY.valueAllele(new FertileBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_MYCOPHILIC = REGISTRY.valueAllele(new FungificationBeeEffect());
	public static final IValueAllele<IBeeEffect> EFFECT_PATRIOTIC = Todos.todo();

	// Speed
	public static final IFloatAllele SPEED_SLOWEST = REGISTRY.floatAllele(0.3f, true);
	public static final IFloatAllele SPEED_SLOWER = REGISTRY.floatAllele(0.6f, true);
	public static final IFloatAllele SPEED_SLOW = REGISTRY.floatAllele(0.8f, true);
	public static final IFloatAllele SPEED_NORMAL = REGISTRY.floatAllele(1.0f);
	public static final IFloatAllele SPEED_FAST = REGISTRY.floatAllele(1.2f, true);
	public static final IFloatAllele SPEED_FASTER = REGISTRY.floatAllele(1.4f);
	public static final IFloatAllele SPEED_FASTEST = REGISTRY.floatAllele(1.7f);

	// Size
	public static final IFloatAllele SIZE_SMALLEST = REGISTRY.floatAllele(0.3f);
	public static final IFloatAllele SIZE_SMALLER = REGISTRY.floatAllele(0.4f);
	public static final IFloatAllele SIZE_SMALL = REGISTRY.floatAllele(0.5f);
	public static final IFloatAllele SIZE_AVERAGE = REGISTRY.floatAllele(0.6f);
	public static final IFloatAllele SIZE_LARGE = REGISTRY.floatAllele(0.75f);
	public static final IFloatAllele SIZE_LARGER = REGISTRY.floatAllele(0.9f);
	public static final IFloatAllele SIZE_LARGEST = REGISTRY.floatAllele(1.0f);

	// Metabolism
	public static final IIntegerAllele METABOLISM_SLOWEST = REGISTRY.intAllele(1);
	public static final IIntegerAllele METABOLISM_SLOWER = REGISTRY.intAllele(2);
	public static final IIntegerAllele METABOLISM_SLOW = REGISTRY.intAllele(3);
	public static final IIntegerAllele METABOLISM_NORMAL = REGISTRY.intAllele(5);
	public static final IIntegerAllele METABOLISM_FAST = REGISTRY.intAllele(7);
	public static final IIntegerAllele METABOLISM_FASTER = REGISTRY.intAllele(8);
	public static final IIntegerAllele METABOLISM_FASTEST = REGISTRY.intAllele(10);

	// Butterfly Effect
	public static final IValueAllele<IButterflyEffect> BUTTERFLY_EFFECT_NONE = REGISTRY.valueAllele(new ButterflyEffectNone());
	// Butterfly Cocoon
	public static final IValueAllele<IButterflyCocoon> REGULAR_COCOON = REGISTRY.valueAllele(new ButterflyCocoon("default"));

	// Sappiness
	public static final IFloatAllele SAPPINESS_LOWEST = REGISTRY.floatAllele(0.1f, true);
	public static final IFloatAllele SAPPINESS_LOWER = REGISTRY.floatAllele(0.2f, true);
	public static final IFloatAllele SAPPINESS_LOW = REGISTRY.floatAllele(0.3f, true);
	public static final IFloatAllele SAPPINESS_AVERAGE = REGISTRY.floatAllele(0.4f, true);
	public static final IFloatAllele SAPPINESS_HIGH = REGISTRY.floatAllele(0.6f, true);
	public static final IFloatAllele SAPPINESS_HIGHER = REGISTRY.floatAllele(0.8f);
	public static final IFloatAllele SAPPINESS_HIGHEST = REGISTRY.floatAllele(1.0f);

	// Saplings
	public static final IFloatAllele SAPLINGS_LOWEST = REGISTRY.floatAllele(0.01f, true);
	public static final IFloatAllele SAPLINGS_LOWER = REGISTRY.floatAllele(0.025f, true);
	public static final IFloatAllele SAPLINGS_LOW = REGISTRY.floatAllele(0.035f, true);
	public static final IFloatAllele SAPLINGS_AVERAGE = REGISTRY.floatAllele(0.05f, true);
	public static final IFloatAllele SAPLINGS_HIGH = REGISTRY.floatAllele(0.1f, true);
	public static final IFloatAllele SAPLINGS_HIGHER = REGISTRY.floatAllele(0.2f, true);
	public static final IFloatAllele SAPLINGS_HIGHEST = REGISTRY.floatAllele(0.3f, true);

	// Fruits
	public static final IValueAllele<IFruit> FRUIT_NONE = REGISTRY.valueAllele(new DummyFruit(MOD_ID, "none"));
	public static final IValueAllele<IFruit> FRUIT_APPLE = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "apple", () -> new ItemStack(Items.APPLE)));
	public static final IValueAllele<IFruit> FRUIT_COCOA = REGISTRY.valueAllele(new PodFruit(MOD_ID, "cocoa"));
	public static final IValueAllele<IFruit> FRUIT_CHESTNUT = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "chestnut"));
	public static final IValueAllele<IFruit> FRUIT_WALNUT = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "walnut"));
	public static final IValueAllele<IFruit> FRUIT_CHERRY = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "cherry"));
	public static final IValueAllele<IFruit> FRUIT_DATES = REGISTRY.valueAllele(new PodFruit(MOD_ID, "dates"));
	public static final IValueAllele<IFruit> FRUIT_PAPAYA = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "papaya"));
	public static final IValueAllele<IFruit> FRUIT_LEMON = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "lemon"));
	public static final IValueAllele<IFruit> FRUIT_PLUM = REGISTRY.valueAllele(new RipeningFruit(MOD_ID, "plum"));

	// Tree Effect
	public static final IValueAllele<ILeafEffect> TREE_EFFECT_NONE = REGISTRY.valueAllele();

	// Maturation
	public static final IIntegerAllele MATURATION_SLOWEST = REGISTRY.intAllele(10, true);
	public static final IIntegerAllele MATURATION_SLOWER = REGISTRY.intAllele(7);
	public static final IIntegerAllele MATURATION_SLOW = REGISTRY.intAllele(5, true);
	public static final IIntegerAllele MATURATION_AVERAGE = REGISTRY.intAllele(4);
	public static final IIntegerAllele MATURATION_FAST = REGISTRY.intAllele(3);
	public static final IIntegerAllele MATURATION_FASTER = REGISTRY.intAllele(2);
	public static final IIntegerAllele MATURATION_FASTEST = REGISTRY.intAllele(1);

	// Yield
	public static final IFloatAllele YIELD_LOWEST = REGISTRY.floatAllele(0.025f, true);
	public static final IFloatAllele YIELD_LOWER = REGISTRY.floatAllele(0.05f, true);
	public static final IFloatAllele YIELD_LOW = REGISTRY.floatAllele(0.1f, true);
	public static final IFloatAllele YIELD_AVERAGE = REGISTRY.floatAllele(0.2f, true);
	public static final IFloatAllele YIELD_HIGH = REGISTRY.floatAllele(0.3f);
	public static final IFloatAllele YIELD_HIGHER = REGISTRY.floatAllele(0.35f);
	public static final IFloatAllele YIELD_HIGHEST = REGISTRY.floatAllele(0.4f);

	// Height todo rename to shortest/tallest
	public static final IFloatAllele HEIGHT_SMALLEST = REGISTRY.floatAllele(0.25f);
	public static final IFloatAllele HEIGHT_SMALLER = REGISTRY.floatAllele(0.5f);
	public static final IFloatAllele HEIGHT_SMALL = REGISTRY.floatAllele(0.75f);
	public static final IFloatAllele HEIGHT_AVERAGE = REGISTRY.floatAllele(1.0f);
	public static final IFloatAllele HEIGHT_LARGE = REGISTRY.floatAllele(1.25f);
	public static final IFloatAllele HEIGHT_LARGER = REGISTRY.floatAllele(1.5f);
	public static final IFloatAllele HEIGHT_LARGEST = REGISTRY.floatAllele(1.75f);
	public static final IFloatAllele HEIGHT_GIGANTIC = REGISTRY.floatAllele(2.0f);

	// Girth
	public static final IIntegerAllele GIRTH_1 = REGISTRY.intAllele(1);
	public static final IIntegerAllele GIRTH_2 = REGISTRY.intAllele(2);
	public static final IIntegerAllele GIRTH_3 = REGISTRY.intAllele(3);
	public static final IIntegerAllele GIRTH_4 = REGISTRY.intAllele(4);
	public static final IIntegerAllele GIRTH_5 = REGISTRY.intAllele(5);
	public static final IIntegerAllele GIRTH_6 = REGISTRY.intAllele(6);
	public static final IIntegerAllele GIRTH_7 = REGISTRY.intAllele(7);
	public static final IIntegerAllele GIRTH_8 = REGISTRY.intAllele(8);
	public static final IIntegerAllele GIRTH_9 = REGISTRY.intAllele(9);
	public static final IIntegerAllele GIRTH_10 = REGISTRY.intAllele(10);
}
