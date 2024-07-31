package forestry.factory.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.IFabricatorSmeltingRecipe;
import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.IHygroregulatorRecipe;
import forestry.api.recipes.IMoistenerRecipe;
import forestry.api.recipes.ISqueezerContainerRecipe;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.IStillRecipe;
import forestry.apiculture.recipes.HygroregulatorRecipe;
import forestry.factory.recipes.CarpenterRecipe;
import forestry.factory.recipes.CentrifugeRecipe;
import forestry.factory.recipes.FabricatorRecipe;
import forestry.factory.recipes.FabricatorSmeltingRecipe;
import forestry.factory.recipes.FermenterRecipe;
import forestry.factory.recipes.MoistenerRecipe;
import forestry.factory.recipes.SqueezerContainerRecipe;
import forestry.factory.recipes.SqueezerRecipe;
import forestry.factory.recipes.StillRecipe;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.FeatureRecipeType;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class FactoryRecipeTypes {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.FACTORY);

	public static final FeatureRecipeType<ICarpenterRecipe> CARPENTER = REGISTRY.recipeType("carpenter", CarpenterRecipe.Serializer::new);
	public static final FeatureRecipeType<ICentrifugeRecipe> CENTRIFUGE = REGISTRY.recipeType("centrifuge", CentrifugeRecipe.Serializer::new);
	public static final FeatureRecipeType<IFabricatorRecipe> FABRICATOR = REGISTRY.recipeType("fabricator", FabricatorRecipe.Serializer::new);
	public static final FeatureRecipeType<IFabricatorSmeltingRecipe> FABRICATOR_SMELTING = REGISTRY.recipeType("fabricator_smelting", FabricatorSmeltingRecipe.Serializer::new);
	public static final FeatureRecipeType<IFermenterRecipe> FERMENTER = REGISTRY.recipeType("fermenter", FermenterRecipe.Serializer::new);
	public static final FeatureRecipeType<IHygroregulatorRecipe> HYGROREGULATOR = REGISTRY.recipeType("hygroregulator", HygroregulatorRecipe.Serializer::new);
	public static final FeatureRecipeType<IMoistenerRecipe> MOISTENER = REGISTRY.recipeType("moistener", MoistenerRecipe.Serializer::new);
	public static final FeatureRecipeType<ISqueezerRecipe> SQUEEZER = REGISTRY.recipeType("squeezer", SqueezerRecipe.Serializer::new);
	public static final FeatureRecipeType<ISqueezerContainerRecipe> SQUEEZER_CONTAINER = REGISTRY.recipeType("squeezer_container", SqueezerContainerRecipe.Serializer::new);
	public static final FeatureRecipeType<IStillRecipe> STILL = REGISTRY.recipeType("still", StillRecipe.Serializer::new);
}
