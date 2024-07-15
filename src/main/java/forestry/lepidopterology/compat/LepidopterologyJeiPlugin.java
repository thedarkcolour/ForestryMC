package forestry.lepidopterology.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryConstants;
import forestry.lepidopterology.features.LepidopterologyItems;

import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;

@JeiPlugin
public class LepidopterologyJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(ForestryConstants.MOD_ID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) {
		IIngredientSubtypeInterpreter<ItemStack> butterflySubtypeInterpreter = (itemStack, context) -> {
			IIndividual individual = GeneticHelper.getIndividual(itemStack);
			return individual == null ? IIngredientSubtypeInterpreter.NONE : individual.getGenome().getPrimarySpecies().getBinomial();
		};

		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.BUTTERFLY_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.COCOON_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.CATERPILLAR_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.SERUM_GE.item(), butterflySubtypeInterpreter);
	}
}
