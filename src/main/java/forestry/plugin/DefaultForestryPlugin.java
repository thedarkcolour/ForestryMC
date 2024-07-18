package forestry.plugin;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.hives.HiveType;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.genetics.ForestrySpeciesType;
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
import forestry.apiculture.hives.HiveDefinition;
import forestry.lepidopterology.genetics.ButterflyDefinition;
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
		// Species types
		genetics.registerSpeciesType(ForestrySpeciesType.BEE)
				.setKaryotype(karyotype -> {
					karyotype.setSpecies(BeeChromosomes.SPECIES, ForestryBeeSpecies.FOREST);
					karyotype.set(BeeChromosomes.SPEED, ForestryAlleles.SPEED_SLOWEST)
							.addAlleles(List.of(ForestryAlleles.SPEED_SLOWEST, ForestryAlleles.SPEED_SLOWER, ForestryAlleles.SPEED_SLOW, ForestryAlleles.SPEED_NORMAL, ForestryAlleles.SPEED_FAST, ForestryAlleles.SPEED_FASTER, ForestryAlleles.SPEED_FASTEST));
					karyotype.set(BeeChromosomes.LIFESPAN, ForestryAlleles.LIFESPAN_SHORTER)
							.addAlleles(List.of(ForestryAlleles.LIFESPAN_SHORTEST, ForestryAlleles.LIFESPAN_SHORTER, ForestryAlleles.LIFESPAN_SHORT, ForestryAlleles.LIFESPAN_SHORTENED, ForestryAlleles.LIFESPAN_NORMAL, ForestryAlleles.LIFESPAN_ELONGATED, ForestryAlleles.LIFESPAN_LONG, ForestryAlleles.LIFESPAN_LONGER, ForestryAlleles.LIFESPAN_LONGEST));
					karyotype.set(BeeChromosomes.FERTILITY, ForestryAlleles.FERTILITY_2)
							.addAlleles(List.of(ForestryAlleles.FERTILITY_1, ForestryAlleles.FERTILITY_2, ForestryAlleles.FERTILITY_3, ForestryAlleles.FERTILITY_4));
					karyotype.set(BeeChromosomes.TEMPERATURE_TOLERANCE, ForestryAlleles.TOLERANCE_NONE)
							.addAlleles(List.of(ForestryAlleles.TOLERANCE_NONE, ForestryAlleles.TOLERANCE_BOTH_1, ForestryAlleles.TOLERANCE_BOTH_2, ForestryAlleles.TOLERANCE_BOTH_3, ForestryAlleles.TOLERANCE_BOTH_4, ForestryAlleles.TOLERANCE_BOTH_5, ForestryAlleles.TOLERANCE_UP_1, ForestryAlleles.TOLERANCE_UP_2, ForestryAlleles.TOLERANCE_UP_3, ForestryAlleles.TOLERANCE_UP_4, ForestryAlleles.TOLERANCE_UP_5, ForestryAlleles.TOLERANCE_DOWN_1, ForestryAlleles.TOLERANCE_DOWN_2, ForestryAlleles.TOLERANCE_DOWN_3, ForestryAlleles.TOLERANCE_DOWN_4, ForestryAlleles.TOLERANCE_DOWN_5));
					karyotype.set(BeeChromosomes.HUMIDITY_TOLERANCE, ForestryAlleles.TOLERANCE_NONE)
							.addAlleles(List.of(ForestryAlleles.TOLERANCE_NONE, ForestryAlleles.TOLERANCE_BOTH_1, ForestryAlleles.TOLERANCE_BOTH_2, ForestryAlleles.TOLERANCE_UP_1, ForestryAlleles.TOLERANCE_UP_2, ForestryAlleles.TOLERANCE_DOWN_1, ForestryAlleles.TOLERANCE_DOWN_2));
					karyotype.set(BeeChromosomes.NEVER_SLEEPS, false);
					karyotype.set(BeeChromosomes.CAVE_DWELLING, false);
					karyotype.set(BeeChromosomes.TOLERATES_RAIN, false);
					karyotype.set(BeeChromosomes.FLOWER_TYPE, ForestryAlleles.FLOWER_TYPE_VANILLA)
							.addAlleles(List.of(ForestryAlleles.FLOWER_TYPE_VANILLA, ForestryAlleles.FLOWER_TYPE_NETHER, ForestryAlleles.FLOWER_TYPE_CACTI, ForestryAlleles.FLOWER_TYPE_MUSHROOMS, ForestryAlleles.FLOWER_TYPE_END, ForestryAlleles.FLOWER_TYPE_JUNGLE, ForestryAlleles.FLOWER_TYPE_SNOW, ForestryAlleles.FLOWER_TYPE_WHEAT, ForestryAlleles.FLOWER_TYPE_GOURD));
					karyotype.set(BeeChromosomes.TERRITORY, ForestryAlleles.TERRITORY_AVERAGE)
							.addAlleles(List.of());
					karyotype.set(BeeChromosomes.EFFECT, ForestryAlleles.EFFECT_NONE);
					karyotype.set(BeeChromosomes.POLLINATION, ForestryAlleles.POLLINATION_SLOWEST);
				});
		genetics.registerSpeciesType(ForestrySpeciesType.TREE)
				.setKaryotype(karyotype -> {
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

		genetics.registerSpeciesType(ForestrySpeciesType.BUTTERFLY)
				.setKaryotype(karyotype -> {
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
					karyotype.set(ButterflyChromosomes.COCOON, ForestryAlleles.DEFAULT_COCOON);
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
		apiculture.registerHive(HiveType.FOREST.getId(), HiveDefinition.FOREST);
		apiculture.registerHive(HiveType.MEADOWS.getId(), HiveDefinition.MEADOWS);
		apiculture.registerHive(HiveType.DESERT.getId(), HiveDefinition.DESERT);
		apiculture.registerHive(HiveType.JUNGLE.getId(), HiveDefinition.JUNGLE);
		apiculture.registerHive(HiveType.END.getId(), HiveDefinition.END);
		apiculture.registerHive(HiveType.SNOW.getId(), HiveDefinition.SNOW);
		apiculture.registerHive(HiveType.SWAMP.getId(), HiveDefinition.SWAMP);

		for (ForestryFlowerType type : ForestryFlowerType.values()) {
			apiculture.registerFlowerType(type);
		}
	}

	@Override
	public void registerArboriculture(IArboricultureRegistration arboriculture) {
		DefaultTreeSpecies.register(arboriculture);
	}

	@Override
	public void registerLepidopterology(ILepidopterologyRegistration lepidopterology) {
		DefaultButterflySpecies.register(lepidopterology);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
