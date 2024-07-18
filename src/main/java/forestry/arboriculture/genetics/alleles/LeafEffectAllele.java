package forestry.arboriculture.genetics.alleles;

import forestry.api.genetics.IAlleleRegistry;

import forestry.api.genetics.alleles.TreeChromosomes;

public class LeafEffectAllele {
	public static final LeafEffectNone NONE = new LeafEffectNone();

	public static void registerAlleles(IAlleleRegistry registry) {
		registry.registerAllele(NONE, TreeChromosomes.EFFECT);
	}
}
