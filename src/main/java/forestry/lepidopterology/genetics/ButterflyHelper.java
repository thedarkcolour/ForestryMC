package forestry.lepidopterology.genetics;

import genetics.api.GeneticsAPI;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IKaryotype;
import genetics.api.root.IRootDefinition;

import forestry.api.lepidopterology.genetics.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IButterflyRoot;
import forestry.core.genetics.alleles.EnumAllele;
import forestry.lepidopterology.genetics.alleles.ButterflyAlleles;

public class ButterflyHelper {
	public static final IRootDefinition<ButterflyRoot> ROOT = GeneticsAPI.apiInstance.getRoot(ButterflyRoot.UID);

	public static IButterflyRoot getRoot() {
		return ROOT.get();
	}

	public static IKaryotype getKaryotype() {
		return getRoot().getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(ButterflyChromosomes.SIZE, EnumAllele.Size.SMALL)
			.set(ButterflyChromosomes.SPEED, EnumAllele.Speed.SLOWEST)
			.set(ButterflyChromosomes.LIFESPAN, EnumAllele.Lifespan.SHORTER)
			.set(ButterflyChromosomes.METABOLISM, EnumAllele.Metabolism.SLOWER)
			.set(ButterflyChromosomes.FERTILITY, 3)
			.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE)
			.set(ButterflyChromosomes.NOCTURNAL, false)
			.set(ButterflyChromosomes.TOLERATES_RAIN, false)
			.set(ButterflyChromosomes.FIRE_RESIST, false)
			.set(ButterflyChromosomes.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA)
			.set(ButterflyChromosomes.EFFECT, ButterflyAlleles.butterflyNone)
			.set(ButterflyChromosomes.COCOON, ButterflyAlleles.DEFAULT_COCOON)
			.set(ButterflyChromosomes.SPECIES, ButterflyDefinition.Monarch.getSpecies())
			.build();
	}
}
