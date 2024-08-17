package forestry.core.fluids;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

import forestry.core.recipes.RecipeManagers;

public class ReloadableFluidFilter implements Supplier<Set<ResourceLocation>> {
	private final Supplier<Set<ResourceLocation>> filterGetter;
	private int recipeReload;
	@Nullable
	private Set<ResourceLocation> cachedFilter;

	public ReloadableFluidFilter(Supplier<Set<ResourceLocation>> filterGetter) {
		this.filterGetter = filterGetter;
	}

	@Override
	public Set<ResourceLocation> get() {
		int currentReloads = RecipeManagers.getRecipeReloads();
		// if null OR recipe manager has updated, create the cache
		if (currentReloads != this.recipeReload || this.cachedFilter == null) {
			this.recipeReload = currentReloads;
			this.cachedFilter = this.filterGetter.get();
		}
		return this.cachedFilter;
	}
}
