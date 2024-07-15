package forestry.plugin;

import java.util.List;

import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.hives.HiveType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IForestryPlugin;
import forestry.api.plugin.IGeneticRegistration;
import forestry.plugin.species.DefaultBeeSpecies;
import forestry.plugin.taxonomy.BeeTaxonomy;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.hives.HiveDefinition;
import forestry.plugin.taxonomy.TreeTaxonomy;
import forestry.core.genetics.alleles.FertilityAllele;
import forestry.core.genetics.alleles.FlowerTypeAllele;
import forestry.core.genetics.alleles.LifespanAllele;
import forestry.core.genetics.alleles.PollinationAllele;
import forestry.core.genetics.alleles.SpeedAllele;
import forestry.core.genetics.alleles.TerritoryAllele;
import forestry.core.genetics.alleles.ToleranceAllele;
import forestry.plugin.taxonomy.ButterflyTaxonomy;

public class DefaultForestryPlugin implements IForestryPlugin {
	@Override
	public void registerGenetics(IGeneticRegistration genetics) {
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
	}

	@Override
	public void registerApiculture(IApicultureRegistration apiculture, IGeneticRegistration genetics) {
		BeeTaxonomy.defineTaxa(genetics);
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

		// Bee chromosomes (should probably be in species registration)
		genetics.registerChromosome(BeeChromosomes.SPEED).addAlleles(SpeedAllele.values());
		genetics.registerChromosome(BeeChromosomes.LIFESPAN).addAlleles(LifespanAllele.values());
		genetics.registerChromosome(BeeChromosomes.FERTILITY).addAlleles(FertilityAllele.values());
		genetics.registerChromosome(BeeChromosomes.TEMPERATURE_TOLERANCE).addAlleles(ToleranceAllele.values());
		genetics.registerChromosome(BeeChromosomes.NEVER_SLEEPS); // todo boolean chromosome types
		genetics.registerChromosome(BeeChromosomes.HUMIDITY_TOLERANCE).addAlleles(ToleranceAllele.humidityValues());
		genetics.registerChromosome(BeeChromosomes.TOLERATES_RAIN); // todo
		genetics.registerChromosome(BeeChromosomes.CAVE_DWELLING); // todo
		genetics.registerChromosome(BeeChromosomes.FLOWER_TYPE).addAlleles(FlowerTypeAllele.values());
		genetics.registerChromosome(BeeChromosomes.POLLINATION).addAlleles(PollinationAllele.values());
		genetics.registerChromosome(BeeChromosomes.TERRITORY).addAlleles(TerritoryAllele.values());
		genetics.registerChromosome(BeeChromosomes.EFFECT); // todo
		// AlleleEffects.registerAlleles(registry);
	}

	@Override
	public void registerArboriculture(IGeneticRegistration genetics) {
		TreeTaxonomy.defineTaxa(genetics);
	}

	@Override
	public void registerLepidopterology(IGeneticRegistration genetics) {
		ButterflyTaxonomy.defineTaxa(genetics);
	}
}
