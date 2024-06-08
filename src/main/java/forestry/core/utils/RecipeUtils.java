package forestry.core.utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.core.ClientsideCode;
import forestry.worktable.inventory.WorktableCraftingContainer;

public final class RecipeUtils {
    @Nullable
    public static RecipeManager getRecipeManager(@Nullable Level world){
        RecipeManager manager = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.getRecipeManager() : null;
        return manager != null ?  manager : world != null ? world.getRecipeManager() : null;
    }

    @Nullable
    public static <C extends Container, T extends Recipe<C>> Recipe<C> getRecipe(RecipeType<T> recipeType, ResourceLocation name, @Nullable Level world) {
        RecipeManager manager = getRecipeManager(world);
        if(manager == null){
            return null;
        }
        return manager.byType(recipeType).get(name);
    }

    public static <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> recipeType, C inventory, @Nullable Level world) {
        RecipeManager manager = getRecipeManager(world);
        if (manager == null || world == null) {
            return Collections.emptyList();
        }
		return manager.getRecipesFor(recipeType, inventory, world);
    }

    public static List<CraftingRecipe> findMatchingRecipes(CraftingContainer inventory, Level level) {
        return level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, inventory, level);
    }

    // Returns a crafting matrix for a certain recipe using available items
    @Nullable
    public static WorktableCraftingContainer getUsedMatrix(WorktableCraftingContainer originalMatrix, NonNullList<ItemStack> availableItems, Level level, CraftingRecipe recipe) {
        if (!recipe.matches(originalMatrix, level)) {
            return null;
        }

        ItemStack expectedOutput = recipe.assemble(originalMatrix);
        if (expectedOutput.isEmpty()) {
            return null;
        }

        WorktableCraftingContainer usedMatrix = new WorktableCraftingContainer();
        NonNullList<ItemStack> stockCopy = ItemStackUtil.condenseStacks(availableItems);

        for (int slot = 0; slot < originalMatrix.getContainerSize(); slot++) {
            ItemStack stack = originalMatrix.getItem(slot);

            if (!stack.isEmpty()) {
                ItemStack equivalent = getCraftingEquivalent(stockCopy, originalMatrix, slot, level, recipe, expectedOutput);
                if (equivalent.isEmpty()) {
                    return null;
                } else {
                    usedMatrix.setItem(slot, equivalent);
                }
            }
        }

        if (recipe.matches(usedMatrix, level)) {
            ItemStack output = recipe.assemble(usedMatrix);
            if (ItemStack.matches(output, expectedOutput)) {
                return usedMatrix;
            }
        }

        return null;
    }

    private static ItemStack getCraftingEquivalent(NonNullList<ItemStack> stockCopy, WorktableCraftingContainer originalMatrix, int slot, Level level, CraftingRecipe recipe, ItemStack expectedOutput) {
        ItemStack originalStack = originalMatrix.getItem(slot);
        for (ItemStack stockStack : stockCopy) {
            if (!stockStack.isEmpty()) {
                ItemStack singleStockStack = stockStack.copy();
                singleStockStack.setCount(1);
                originalMatrix.setItem(slot, singleStockStack);

                if (recipe.matches(originalMatrix, level)) {
                    ItemStack output = recipe.assemble(originalMatrix);
                    if (ItemStack.matches(output, expectedOutput)) {
                        originalMatrix.setItem(slot, originalStack);
                        return stockStack.split(1);
                    }
                }
            }
        }
        originalMatrix.setItem(slot, originalStack);
        return ItemStack.EMPTY;
    }
}
