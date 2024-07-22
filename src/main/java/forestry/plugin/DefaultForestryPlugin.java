package forestry.plugin;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.hives.HiveType;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.lepidopterology.ForestryButterflySpecies;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IArboricultureRegistration;
import forestry.api.plugin.IForestryPlugin;
import forestry.api.plugin.IGeneticRegistration;
import forestry.api.plugin.ILepidopterologyRegistration;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.genetics.BeeSpeciesType;
import forestry.apiculture.hives.HiveDefinition;
import forestry.apiculture.items.EnumHoneyComb;
import forestry.plugin.species.DefaultBeeSpecies;
import forestry.plugin.species.DefaultButterflySpecies;
import forestry.plugin.species.DefaultTreeSpecies;
import forestry.plugin.taxonomy.BeeTaxonomy;
import forestry.plugin.taxonomy.ButterflyTaxonomy;
import forestry.plugin.taxonomy.TreeTaxonomy;

public class DefaultForestryPlugin implements IForestryPlugin {
	public static final ResourceLocation ID = ForestryConstants.forestry("default");

	@Override
	public void registerGenetics(IGeneticRegistration genetics) {
		// Bee type
		genetics.registerSpeciesType(ForestrySpeciesTypes.BEE, BeeSpeciesType::new, karyotype -> {
			// TODO Allowed alleles are added later by IApicultureRegistration
			karyotype.setSpecies(BeeChromosomes.SPECIES, ForestryBeeSpecies.FOREST);
			karyotype.set(BeeChromosomes.SPEED, ForestryAlleles.SPEED_SLOWEST)
					.addAlleles(List.of(ForestryAlleles.SPEED_SLOWEST, ForestryAlleles.SPEED_SLOWER, ForestryAlleles.SPEED_SLOW, ForestryAlleles.SPEED_NORMAL, ForestryAlleles.SPEED_FAST, ForestryAlleles.SPEED_FASTER, ForestryAlleles.SPEED_FASTEST));
			karyotype.set(BeeChromosomes.LIFESPAN, ForestryAlleles.LIFESPAN_SHORTER)
					.addAlleles(List.of(ForestryAlleles.LIFESPAN_SHORTEST, ForestryAlleles.LIFESPAN_SHORTER, ForestryAlleles.LIFESPAN_SHORT, ForestryAlleles.LIFESPAN_SHORTENED, ForestryAlleles.LIFESPAN_NORMAL, ForestryAlleles.LIFESPAN_ELONGATED, ForestryAlleles.LIFESPAN_LONG, ForestryAlleles.LIFESPAN_LONGER, ForestryAlleles.LIFESPAN_LONGEST));
			karyotype.set(BeeChromosomes.FERTILITY, ForestryAlleles.FERTILITY_2)
					.addAlleles(List.of(ForestryAlleles.FERTILITY_1, ForestryAlleles.FERTILITY_2, ForestryAlleles.FERTILITY_3, ForestryAlleles.FERTILITY_4));
			karyotype.set(BeeChromosomes.TEMPERATURE_TOLERANCE, ForestryAlleles.TOLERANCE_NONE)
					.addAlleles(ForestryAlleles.DEFAULT_TEMPERATURE_TOLERANCES);
			karyotype.set(BeeChromosomes.HUMIDITY_TOLERANCE, ForestryAlleles.TOLERANCE_NONE)
					.addAlleles(ForestryAlleles.DEFAULT_HUMIDITY_TOLERANCES);
			karyotype.set(BeeChromosomes.NEVER_SLEEPS, false);
			karyotype.set(BeeChromosomes.CAVE_DWELLING, false);
			karyotype.set(BeeChromosomes.TOLERATES_RAIN, false);
			// TODO Allowed alleles are added later by IApicultureRegistration
			karyotype.set(BeeChromosomes.FLOWER_TYPE, ForestryAlleles.FLOWER_TYPE_VANILLA)
					.addAlleles(List.of(ForestryAlleles.FLOWER_TYPE_VANILLA, ForestryAlleles.FLOWER_TYPE_NETHER, ForestryAlleles.FLOWER_TYPE_CACTI, ForestryAlleles.FLOWER_TYPE_MUSHROOMS, ForestryAlleles.FLOWER_TYPE_END, ForestryAlleles.FLOWER_TYPE_JUNGLE, ForestryAlleles.FLOWER_TYPE_SNOW, ForestryAlleles.FLOWER_TYPE_WHEAT, ForestryAlleles.FLOWER_TYPE_GOURD));
			karyotype.set(BeeChromosomes.TERRITORY, ForestryAlleles.TERRITORY_AVERAGE)
					.addAlleles(ForestryAlleles.DEFAULT_TERRITORIES);
			// TODO Allowed alleles are added later by IApicultureRegistration
			karyotype.set(BeeChromosomes.EFFECT, ForestryAlleles.EFFECT_NONE);
			karyotype.set(BeeChromosomes.POLLINATION, ForestryAlleles.POLLINATION_SLOWEST)
					.addAlleles(ForestryAlleles.DEFAULT_POLLINATIONS);
		});

		// Tree type
		genetics.registerSpeciesType(ForestrySpeciesTypes.TREE, karyotype -> {
			karyotype.setSpecies(TreeChromosomes.SPECIES, ForestryTreeSpecies.OAK);
			karyotype.set(TreeChromosomes.HEIGHT, ForestryAlleles.HEIGHT_SMALL)
					.addAlleles(List.of(ForestryAlleles.HEIGHT_SMALLEST, ForestryAlleles.HEIGHT_SMALLER, ForestryAlleles.HEIGHT_SMALL, ForestryAlleles.HEIGHT_AVERAGE, ForestryAlleles.HEIGHT_LARGE, ForestryAlleles.HEIGHT_LARGER, ForestryAlleles.HEIGHT_LARGEST, ForestryAlleles.HEIGHT_GIGANTIC));
			karyotype.set(TreeChromosomes.SAPLINGS, ForestryAlleles.SAPLINGS_LOWER)
					.addAlleles(List.of(ForestryAlleles.SAPLINGS_LOWEST, ForestryAlleles.SAPLINGS_LOWER, ForestryAlleles.SAPLINGS_LOW, ForestryAlleles.SAPLINGS_AVERAGE, ForestryAlleles.SAPLINGS_HIGH, ForestryAlleles.SAPLINGS_HIGHER, ForestryAlleles.SAPLINGS_HIGHEST));
			karyotype.set(TreeChromosomes.FRUITS, ForestryAlleles.FRUIT_NONE);
			karyotype.set(TreeChromosomes.YIELD, ForestryAlleles.YIELD_LOWEST)
					.addAlleles(List.of(ForestryAlleles.YIELD_LOWEST, ForestryAlleles.YIELD_LOWER, ForestryAlleles.YIELD_LOW, ForestryAlleles.YIELD_AVERAGE, ForestryAlleles.YIELD_HIGH, ForestryAlleles.YIELD_HIGHER, ForestryAlleles.YIELD_HIGHEST));
			karyotype.set(TreeChromosomes.SAPPINESS, ForestryAlleles.SAPPINESS_LOWEST)
					.addAlleles(List.of(ForestryAlleles.SAPPINESS_LOWEST, ForestryAlleles.SAPPINESS_LOWER, ForestryAlleles.SAPPINESS_LOW, ForestryAlleles.SAPPINESS_AVERAGE, ForestryAlleles.SAPPINESS_HIGH, ForestryAlleles.SAPPINESS_HIGHER, ForestryAlleles.SAPPINESS_HIGHEST));
			karyotype.set(TreeChromosomes.EFFECT, ForestryAlleles.TREE_EFFECT_NONE)
					.addAlleles(List.of(ForestryAlleles.TREE_EFFECT_NONE));
			karyotype.set(TreeChromosomes.MATURATION, ForestryAlleles.MATURATION_AVERAGE)
					.addAlleles(List.of(ForestryAlleles.MATURATION_SLOWEST, ForestryAlleles.MATURATION_SLOWER, ForestryAlleles.MATURATION_SLOW, ForestryAlleles.MATURATION_AVERAGE, ForestryAlleles.MATURATION_FAST, ForestryAlleles.MATURATION_FASTER, ForestryAlleles.MATURATION_FASTEST));
			karyotype.set(TreeChromosomes.GIRTH, ForestryAlleles.GIRTH_1)
					.addAlleles(List.of(ForestryAlleles.GIRTH_1, ForestryAlleles.GIRTH_2, ForestryAlleles.GIRTH_3, ForestryAlleles.GIRTH_4, ForestryAlleles.GIRTH_5, ForestryAlleles.GIRTH_6, ForestryAlleles.GIRTH_7, ForestryAlleles.GIRTH_8, ForestryAlleles.GIRTH_9, ForestryAlleles.GIRTH_10));
			karyotype.set(TreeChromosomes.FIREPROOF, false);
		});

		// Butterfly type
		genetics.registerSpeciesType(ForestrySpeciesTypes.BUTTERFLY, karyotype -> {
			karyotype.setSpecies(ButterflyChromosomes.SPECIES, ForestryButterflySpecies.MONARCH);
			karyotype.set(ButterflyChromosomes.SIZE, ForestryAlleles.SIZE_SMALL);
			karyotype.set(ButterflyChromosomes.SPEED, ForestryAlleles.SPEED_SLOWEST);
			karyotype.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.LIFESPAN_SHORTER);
			karyotype.set(ButterflyChromosomes.METABOLISM, ForestryAlleles.METABOLISM_SLOWER);
			karyotype.set(ButterflyChromosomes.FERTILITY, ForestryAlleles.FERTILITY_3);
			karyotype.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ForestryAlleles.TOLERANCE_NONE);
			karyotype.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ForestryAlleles.TOLERANCE_NONE);
			karyotype.set(ButterflyChromosomes.NEVER_SLEEPS, false);
			karyotype.set(ButterflyChromosomes.TOLERATES_RAIN, false);
			karyotype.set(ButterflyChromosomes.FIREPROOF, false);
			karyotype.set(ButterflyChromosomes.FLOWER_TYPE, ForestryAlleles.FLOWER_TYPE_VANILLA);
			karyotype.set(ButterflyChromosomes.EFFECT, ForestryAlleles.BUTTERFLY_EFFECT_NONE);
			karyotype.set(ButterflyChromosomes.COCOON, ForestryAlleles.REGULAR_COCOON);
		});

		// Taxonomy
		BeeTaxonomy.defineTaxa(genetics);
		TreeTaxonomy.defineTaxa(genetics);
		ButterflyTaxonomy.defineTaxa(genetics);
	}

	@Override
	public void registerApiculture(IApicultureRegistration apiculture) {
		DefaultBeeSpecies.register(apiculture);

		// Default hives
		Supplier<List<ItemStack>> honeyComb = getHoneyComb(EnumHoneyComb.HONEY);
		Supplier<List<ItemStack>> parchedComb = getHoneyComb(EnumHoneyComb.PARCHED);
		Supplier<List<ItemStack>> silkyComb = getHoneyComb(EnumHoneyComb.SILKY);
		Supplier<List<ItemStack>> mysteriousComb = getHoneyComb(EnumHoneyComb.MYSTERIOUS);
		Supplier<List<ItemStack>> frozenComb = getHoneyComb(EnumHoneyComb.FROZEN);
		Supplier<List<ItemStack>> mossyComb = getHoneyComb(EnumHoneyComb.MOSSY);

		apiculture.registerHive(HiveType.FOREST.getId(), HiveDefinition.FOREST)
				.addDrop(0.80, ForestryBeeSpecies.FOREST, honeyComb, 0.7f)
				.addDrop(0.08, ForestryBeeSpecies.FOREST, honeyComb, 0.0f, Map.of(BeeChromosomes.TOLERATES_RAIN, ForestryAlleles.TRUE))
				.addDrop(0.08, ForestryBeeSpecies.VALIANT, honeyComb);

		apiculture.registerHive(HiveType.MEADOWS.getId(), HiveDefinition.MEADOWS)
				.addDrop(0.80, ForestryBeeSpecies.MEADOWS, honeyComb, 0.7f)
				.addDrop(0.03, ForestryBeeSpecies.VALIANT, honeyComb);

		apiculture.registerHive(HiveType.DESERT.getId(), HiveDefinition.DESERT)
				.addDrop(0.80, ForestryBeeSpecies.MODEST, parchedComb, 0.7f)
				.addDrop(0.03, ForestryBeeSpecies.VALIANT, parchedComb);

		apiculture.registerHive(HiveType.JUNGLE.getId(), HiveDefinition.JUNGLE)
				.addDrop(0.80, ForestryBeeSpecies.TROPICAL, silkyComb, 0.7f)
				.addDrop(0.03, ForestryBeeSpecies.VALIANT, silkyComb);

		apiculture.registerHive(HiveType.END.getId(), HiveDefinition.END)
				.addDrop(0.90, ForestryBeeSpecies.ENDED, mysteriousComb);

		apiculture.registerHive(HiveType.SNOW.getId(), HiveDefinition.SNOW)
				.addDrop(0.80, ForestryBeeSpecies.WINTRY, frozenComb, 0.5f)
				.addDrop(0.03, ForestryBeeSpecies.VALIANT, frozenComb);

		apiculture.registerHive(HiveType.SWAMP.getId(), HiveDefinition.SWAMP)
				.addDrop(0.80, ForestryBeeSpecies.MARSHY, mossyComb, 0.4f)
				.addDrop(0.03, ForestryBeeSpecies.VALIANT, mossyComb);


		// Common village bees
		apiculture.addVillageBee(ForestryBeeSpecies.FOREST, false);
		apiculture.addVillageBee(ForestryBeeSpecies.MEADOWS, false);
		apiculture.addVillageBee(ForestryBeeSpecies.MODEST, false);
		apiculture.addVillageBee(ForestryBeeSpecies.MARSHY, false);
		apiculture.addVillageBee(ForestryBeeSpecies.WINTRY, false);
		apiculture.addVillageBee(ForestryBeeSpecies.TROPICAL, false);

		// Rare village bees
		// todo: this is supposed to be a tolerant flyer variant
		//apiculture.addVillageBee(ForestryBeeSpecies.FOREST, true).withGenome(genome -> ...);
		apiculture.addVillageBee(ForestryBeeSpecies.COMMON, true);
		apiculture.addVillageBee(ForestryBeeSpecies.VALIANT, true);

		// Default flower types
		for (ForestryFlowerType type : ForestryFlowerType.values()) {
			apiculture.registerFlowerType(type);
		}

		// todo plantable flower tags
		//String[] standardTypes = new String[]{FlowerManager.FlowerTypeVanilla, FlowerManager.FlowerTypeSnow};
		//for (Block standardFlower : standardFlowers) {
		//	flowerRegistry.registerPlantableFlower(standardFlower.defaultBlockState(), 1.0, standardTypes);
		//}
		//flowerRegistry.registerPlantableFlower(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1.0, FlowerManager.FlowerTypeMushrooms);
		//flowerRegistry.registerPlantableFlower(Blocks.RED_MUSHROOM.defaultBlockState(), 1.0, FlowerManager.FlowerTypeMushrooms);
		//flowerRegistry.registerPlantableFlower(Blocks.CACTUS.defaultBlockState(), 1.0, FlowerManager.FlowerTypeCacti);

	}

	private static Supplier<List<ItemStack>> getHoneyComb(EnumHoneyComb type) {
		return () -> List.of(ApicultureItems.BEE_COMBS.stack(type));
	}

	@Override
	public void registerArboriculture(IArboricultureRegistration arboriculture) {
		DefaultTreeSpecies.register(arboriculture);

		//arboriculture.registerFruit();
	}

	@Override
	public void registerLepidopterology(ILepidopterologyRegistration lepidopterology) {
		DefaultButterflySpecies.register(lepidopterology);

		//lepidopterology.registerCocoon();
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
