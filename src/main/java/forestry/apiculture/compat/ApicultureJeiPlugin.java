package forestry.apiculture.compat;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.modules.ForestryModuleIds;
import forestry.apiculture.features.ApicultureItems;
import forestry.core.utils.JeiUtil;
import forestry.core.utils.SpeciesUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class ApicultureJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ForestryModuleIds.APICULTURE;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registry) {
		JeiUtil.registerItemSubtypes(registry, BeeChromosomes.SPECIES, SpeciesUtil.BEE_TYPE.get());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		JeiUtil.addDescription(registry, "frames",
				ApicultureItems.FRAME_IMPREGNATED,
				ApicultureItems.FRAME_PROVEN,
				ApicultureItems.FRAME_UNTREATED
		);

		JeiUtil.addDescription(registry, "apiarist.suit",
				ApicultureItems.APIARIST_BOOTS,
				ApicultureItems.APIARIST_CHEST,
				ApicultureItems.APIARIST_HELMET,
				ApicultureItems.APIARIST_LEGS
		);

		JeiUtil.addDescription(registry,
				ApicultureItems.HABITAT_LOCATOR,
				ApicultureItems.SCOOP,
				ApicultureItems.IMPRINTER
		);
	}
}
