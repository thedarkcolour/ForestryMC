package forestry.arboriculture.compat;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.modules.ForestryModuleIds;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.utils.JeiUtil;
import forestry.core.utils.SpeciesUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class ArboricultureJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ForestryModuleIds.ARBORICULTURE;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registry) {
		JeiUtil.registerItemSubtypes(registry, TreeChromosomes.SPECIES, SpeciesUtil.TREE_TYPE.get());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		JeiUtil.addDescription(registration, ArboricultureItems.GRAFTER.item(), ArboricultureItems.GRAFTER_PROVEN.item());
	}
}
