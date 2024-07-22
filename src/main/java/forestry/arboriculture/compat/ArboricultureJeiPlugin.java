package forestry.arboriculture.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;
import forestry.api.ForestryConstants;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.utils.JeiUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class ArboricultureJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(ForestryConstants.MOD_ID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) {
		IIngredientSubtypeInterpreter<ItemStack> arboSubtypeInterpreter = (stack, context) -> stack.getCapability(ForestryCapabilities.INDIVIDUAL)
				.map(individual -> individual.getGenome().getActiveValue(TreeChromosomes.SPECIES).getBinomial())
				.orElse(IIngredientSubtypeInterpreter.NONE);

		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ArboricultureItems.SAPLING.item(), arboSubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ArboricultureItems.POLLEN_FERTILE.item(), arboSubtypeInterpreter);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		JeiUtil.addDescription(registration, ArboricultureItems.GRAFTER.item(), ArboricultureItems.GRAFTER_PROVEN.item());
	}
}
