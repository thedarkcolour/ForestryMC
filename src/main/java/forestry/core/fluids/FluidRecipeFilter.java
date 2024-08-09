package forestry.core.fluids;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.IFabricatorSmeltingRecipe;
import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.IHygroregulatorRecipe;
import forestry.api.recipes.IStillRecipe;
import forestry.core.recipes.RecipeManagers;
import forestry.core.utils.RecipeUtils;
import forestry.factory.features.FactoryRecipeTypes;

public class FluidRecipeFilter implements Supplier<Set<ResourceLocation>> {
	public static final FluidRecipeFilter HYGROREGULATOR_INPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.HYGROREGULATOR.type(), IHygroregulatorRecipe::getInputFluid));
	public static final FluidRecipeFilter CARPENTER_INPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.CARPENTER.type(), ICarpenterRecipe::getInputFluid));
	public static final FluidRecipeFilter FERMENTER_INPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.FERMENTER.type(), IFermenterRecipe::getInputFluid));
	public static final FluidRecipeFilter FERMENTER_OUTPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluids(manager, FactoryRecipeTypes.FERMENTER.type(), IFermenterRecipe::getOutput));
	public static final FluidRecipeFilter FABRICATOR_SMELTING_OUTPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.FABRICATOR_SMELTING.type(), IFabricatorSmeltingRecipe::getResultFluid));
	public static final FluidRecipeFilter STILL_INPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.STILL.type(), IStillRecipe::getInput));
	public static final FluidRecipeFilter STILL_OUTPUT = new FluidRecipeFilter(manager -> RecipeUtils.getTargetFluidsFromStacks(manager, FactoryRecipeTypes.STILL.type(), IStillRecipe::getOutput));

	private final Function<RecipeManager, Set<ResourceLocation>> filterGetter;
	private int recipeReload;
	@Nullable
	private Set<ResourceLocation> cachedFilter;

	FluidRecipeFilter(Function<RecipeManager, Set<ResourceLocation>> filters) {
		this.filterGetter = filters;
		this.recipeReload = RecipeManagers.getRecipeReloads();
	}

	@Override
	public Set<ResourceLocation> get() {
		int currentReloads = RecipeManagers.getRecipeReloads();
		// if null OR recipe manager has updated, create the cache
		if (currentReloads != this.recipeReload || this.cachedFilter == null) {
			this.recipeReload = currentReloads;
			this.cachedFilter = this.filterGetter.apply(RecipeUtils.getRecipeManager());
		}
		return this.cachedFilter;
	}
}
