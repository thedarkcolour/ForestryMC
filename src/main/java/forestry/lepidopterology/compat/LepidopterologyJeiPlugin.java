package forestry.lepidopterology.compat;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.modules.ForestryModuleIds;
import forestry.core.utils.JeiUtil;
import forestry.core.utils.SpeciesUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class LepidopterologyJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ForestryModuleIds.LEPIDOPTEROLOGY;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registry) {
		JeiUtil.registerItemSubtypes(registry, ButterflyChromosomes.SPECIES, SpeciesUtil.BUTTERFLY_TYPE.get());
	}
}
