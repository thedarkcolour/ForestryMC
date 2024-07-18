package forestry.apiculture.genetics;

import forestry.api.apiculture.BeeManager;

import genetics.ApiInstance;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import forestry.api.genetics.alleles.IKaryotype;

import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.alleles.FertilityAllele;
import forestry.core.genetics.alleles.FlowerTypeAllele;
import forestry.core.genetics.alleles.PollinationAllele;
import forestry.core.genetics.alleles.SpeedAllele;
import forestry.core.genetics.alleles.TerritoryAllele;
import forestry.core.genetics.alleles.ToleranceAllele;

public class BeeHelper {
	public static IKaryotype getKaryotype() {
		return BeeManager.beeRoot.getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return ApiInstance.INSTANCE.getRoot(ForestrySpeciesType.BEE).getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(BeeChromosomes.SPEED, SpeedAllele.SLOWEST)
			.set(BeeChromosomes.SPECIES, BeeDefinition.FOREST.getSpecies())
			.set(BeeChromosomes.LIFESPAN, ForestryAlleles.SHORTER)
			.set(BeeChromosomes.FERTILITY, FertilityAllele.NORMAL)
			.set(BeeChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.NONE)
			.set(BeeChromosomes.NEVER_SLEEPS, false)
			.set(BeeChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.NONE)
			.set(BeeChromosomes.TOLERATES_RAIN, false)
			.set(BeeChromosomes.CAVE_DWELLING, false)
			.set(BeeChromosomes.FLOWER_TYPE, FlowerTypeAllele.VANILLA)
			.set(BeeChromosomes.POLLINATION, PollinationAllele.SLOWEST)
			.set(BeeChromosomes.TERRITORY, TerritoryAllele.AVERAGE)
			.set(BeeChromosomes.EFFECT, AlleleEffects.effectNone).build();
	}
}
