package forestry.modules.features;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import net.minecraftforge.registries.RegistryObject;

public class FeatureRecipeType<R extends Recipe<?>> extends ModFeature implements IModFeature {
	private final RegistryObject<RecipeType<R>> type;
	private final RegistryObject<RecipeSerializer<? extends R>> serializer;

	protected FeatureRecipeType(IFeatureRegistry registry, ResourceLocation moduleId, String name, Supplier<RecipeSerializer<? extends R>> serializer) {
		super(moduleId, name);

		this.type = registry.getRegistry(Registry.RECIPE_TYPE_REGISTRY).register(name, () -> RecipeType.simple(new ResourceLocation(moduleId.getNamespace(), name)));
		this.serializer = registry.getRegistry(Registry.RECIPE_SERIALIZER_REGISTRY).register(name, serializer);
	}

	public RecipeType<R> type() {
		return this.type.get();
	}

	public RecipeSerializer<? extends R> serializer() {
		return this.serializer.get();
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.RECIPE_TYPE_REGISTRY;
	}
}
