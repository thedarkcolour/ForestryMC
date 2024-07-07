package forestry.apiculture.genetics;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.genetics.IBeeRoot;
import genetics.ApiInstance;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IKaryotype;

import forestry.api.apiculture.genetics.BeeChromosomes;
import forestry.apiculture.genetics.alleles.AlleleEffects;
import forestry.core.genetics.alleles.EnumAllele;

public class BeeHelper {
	public static IKaryotype getKaryotype() {
		return BeeManager.beeRoot.getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return ApiInstance.INSTANCE.<IBeeRoot>getRoot(BeeRoot.UID).get().getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(BeeChromosomes.SPEED, EnumAllele.Speed.SLOWEST)
			.set(BeeChromosomes.SPECIES, BeeDefinition.FOREST.getSpecies())
			.set(BeeChromosomes.LIFESPAN, EnumAllele.Lifespan.SHORTER)
			.set(BeeChromosomes.FERTILITY, EnumAllele.Fertility.NORMAL)
			.set(BeeChromosomes.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(BeeChromosomes.NEVER_SLEEPS, false)
			.set(BeeChromosomes.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(BeeChromosomes.TOLERATES_RAIN, false)
			.set(BeeChromosomes.CAVE_DWELLING, false)
			.set(BeeChromosomes.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA)
			.set(BeeChromosomes.FLOWERING, EnumAllele.Flowering.SLOWEST)
			.set(BeeChromosomes.TERRITORY, EnumAllele.Territory.AVERAGE)
			.set(BeeChromosomes.EFFECT, AlleleEffects.effectNone).build();
	}
}
