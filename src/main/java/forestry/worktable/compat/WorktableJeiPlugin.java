package forestry.worktable.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.modules.ForestryModuleIds;
import forestry.core.utils.JeiUtil;
import forestry.worktable.features.WorktableBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;

@JeiPlugin
public class WorktableJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ForestryModuleIds.WORKTABLE;
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		JeiUtil.addDescription(registration, WorktableBlocks.WORKTABLE.block());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new WorktableRecipeTransferHandler(), RecipeTypes.CRAFTING);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(WorktableBlocks.WORKTABLE), RecipeTypes.CRAFTING);
	}
}
