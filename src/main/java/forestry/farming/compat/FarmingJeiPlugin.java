package forestry.farming.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.IForestryApi;
import forestry.api.circuits.CircuitHolder;
import forestry.api.farming.IFarmCircuit;
import forestry.api.farming.IFarmLogic;
import forestry.api.modules.ForestryModuleIds;
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
		registration.addRecipes(FarmingInfoRecipeCategory.TYPE, getRecipes());
	}

	public static List<FarmingInfoRecipe> getRecipes() {
		ArrayList<FarmingInfoRecipe> info = new ArrayList<>();

		for (CircuitHolder holder : IForestryApi.INSTANCE.getCircuitManager().getCircuitHolders()) {
			if (holder.circuit() instanceof IFarmCircuit circuit) {
				if (circuit.isManual()) {
					info.add(new FarmingInfoRecipe(holder.stack(), circuit.getProperties(), circuit));
				}
			}
		}

		return info;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(CoreItems.CIRCUITBOARDS.get(EnumCircuitBoardType.INTRICATE)), FarmingInfoRecipeCategory.TYPE);
	}
}
