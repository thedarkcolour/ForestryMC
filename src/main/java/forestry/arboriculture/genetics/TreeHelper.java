package forestry.arboriculture.genetics;

import forestry.api.arboriculture.TreeManager;
import genetics.ApiInstance;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IKaryotype;

import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.genetics.alleles.AlleleFruits;
import forestry.arboriculture.genetics.alleles.LeafEffectAllele;
import forestry.core.genetics.alleles.FireproofAllele;
import forestry.core.genetics.alleles.HeightAllele;
import forestry.core.genetics.alleles.MaturationAllele;
import forestry.core.genetics.alleles.SaplingsAllele;
import forestry.core.genetics.alleles.SappinessAllele;
import forestry.core.genetics.alleles.YieldAllele;

public class TreeHelper {
	public static ITreeSpeciesType getRoot() {
		return TreeManager.treeRoot;
	}

	public static IKaryotype getKaryotype() {
		return ApiInstance.INSTANCE.<ITreeSpeciesType>getRoot(ForestrySpeciesType.TREE).get().getKaryotype();
	}

	public static IAlleleTemplateBuilder createTemplate() {
		return getKaryotype().createTemplate();
	}

	public static IAlleleTemplate createDefaultTemplate(IAlleleTemplateBuilder templateBuilder) {
		return templateBuilder.set(TreeChromosomes.FRUITS, AlleleFruits.fruitNone)
			.set(TreeChromosomes.SPECIES, TreeDefinition.Oak.getSpecies())
			.set(TreeChromosomes.HEIGHT, HeightAllele.SMALL)
			.set(TreeChromosomes.FERTILITY, SaplingsAllele.LOWER)
			.set(TreeChromosomes.YIELD, YieldAllele.LOWEST)
			.set(TreeChromosomes.SAPPINESS, SappinessAllele.LOWEST)
			.set(TreeChromosomes.EFFECT, LeafEffectAllele.NONE)
			.set(TreeChromosomes.MATURATION, MaturationAllele.AVERAGE)
			.set(TreeChromosomes.GIRTH, 1)
			.set(TreeChromosomes.FIREPROOF, FireproofAllele.FALSE).build();
	}
}
