package forestry.lepidopterology.genetics;

import genetics.api.GeneticsAPI;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IKaryotype;

import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.ForestryChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.core.genetics.alleles.FlowerTypeAllele;
import forestry.core.genetics.alleles.MetabolismAllele;
import forestry.core.genetics.alleles.SizeAllele;
import forestry.core.genetics.alleles.SpeedAllele;
import forestry.core.genetics.alleles.ToleranceAllele;
import forestry.lepidopterology.genetics.alleles.ButterflyAlleles;

public class ButterflyHelper {
	public static final ButterflySpeciesType ROOT = GeneticsAPI.apiInstance.getRoot(ForestrySpeciesType.BUTTERFLY);

	public static IButterflySpeciesType getRoot() {
		return ROOT.get();
	}

	public static IKaryotype getKaryotype() {
		return getRoot().getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(ButterflyChromosomes.SIZE, SizeAllele.SMALL)
			.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWEST)
			.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORTER)
			.set(ButterflyChromosomes.METABOLISM, MetabolismAllele.SLOWER)
			.set(ButterflyChromosomes.FERTILITY, 3)
			.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.NONE)
			.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.NONE)
			.set(ButterflyChromosomes.NEVER_SLEEPS, false)
			.set(ButterflyChromosomes.TOLERATES_RAIN, false)
			.set(ButterflyChromosomes.FIRE_RESISTANT, false)
			.set(ButterflyChromosomes.FLOWER_PROVIDER, FlowerTypeAllele.VANILLA)
			.set(ButterflyChromosomes.EFFECT, ButterflyAlleles.butterflyNone)
			.set(ButterflyChromosomes.COCOON, ButterflyAlleles.DEFAULT_COCOON)
			.set(ButterflyChromosomes.SPECIES, ButterflyDefinition.Monarch.getSpecies())
			.build();
	}
}
