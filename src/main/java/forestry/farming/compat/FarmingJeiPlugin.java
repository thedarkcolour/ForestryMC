package forestry.farming.compat;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import forestry.api.modules.ForestryModuleIds;
import forestry.core.ClientsideCode;
import forestry.core.circuits.EnumCircuitBoardType;
import forestry.core.features.CoreItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

@JeiPlugin
public class FarmingJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ForestryModuleIds.FARMING;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(new FarmingInfoRecipeCategory(guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = ClientsideCode.getRecipeManager();

		List<FarmingInfoRecipe> recipes = FarmingInfoRecipeMaker.getRecipes(recipeManager);
		registration.addRecipes(FarmingInfoRecipeCategory.TYPE, recipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(CoreItems.CIRCUITBOARDS.get(EnumCircuitBoardType.INTRICATE)), FarmingInfoRecipeCategory.TYPE);
	}
}
