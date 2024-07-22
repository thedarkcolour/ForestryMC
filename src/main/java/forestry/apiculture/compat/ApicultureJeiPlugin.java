package forestry.apiculture.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;
import forestry.api.ForestryConstants;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.apiculture.features.ApicultureItems;
import forestry.core.utils.JeiUtil;

import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class ApicultureJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(ForestryConstants.MOD_ID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) {
		IIngredientSubtypeInterpreter<ItemStack> beeSubtypeInterpreter = (stack, context) -> stack.getCapability(ForestryCapabilities.INDIVIDUAL)
				.map(individual -> individual.getGenome().getActiveValue(BeeChromosomes.SPECIES).getBinomial())
				.orElse(IIngredientSubtypeInterpreter.NONE);

		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ApicultureItems.BEE_DRONE.item(), beeSubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ApicultureItems.BEE_PRINCESS.item(), beeSubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ApicultureItems.BEE_QUEEN.item(), beeSubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ApicultureItems.BEE_LARVAE.item(), beeSubtypeInterpreter);
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
