package genetics.plugins.vanilla;

import forestry.core.config.Constants;
import genetics.api.GeneticPlugin;
import genetics.api.IGeneticPlugin;
import genetics.api.alleles.IAlleleRegistry;

@GeneticPlugin(modId = Constants.MOD_ID)
public class VanillaPlugin implements IGeneticPlugin {
	@Override
	public void registerAlleles(IAlleleRegistry registry) {
		for (int i = 1; i <= 10; i++) {
			registry.registerAllele("i", i + "d", i, true);
		}
		registry.registerAllele("bool", Boolean.toString(true), true, false);
		registry.registerAllele("bool", Boolean.toString(false), false, false);
	}
}
