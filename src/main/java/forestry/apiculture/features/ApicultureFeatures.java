package forestry.apiculture.features;

import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import forestry.api.modules.ForestryModuleIds;
import forestry.apiculture.hives.HiveDecorator;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class ApicultureFeatures {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ForestryModuleIds.APICULTURE);

	public static final DeferredRegister<Feature<?>> FEATURES = REGISTRY.getRegistry(Registry.FEATURE_REGISTRY);
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = REGISTRY.getRegistry(Registry.CONFIGURED_FEATURE_REGISTRY);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = REGISTRY.getRegistry(Registry.PLACED_FEATURE_REGISTRY);

	public static final RegistryObject<HiveDecorator> HIVE_DECORATOR = FEATURES.register("hive", HiveDecorator::new);
	public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_HIVE_DECORATOR = CONFIGURED_FEATURES.register("hive", () -> new ConfiguredFeature<>(HIVE_DECORATOR.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<?> PLACED_HIVE_DECORATOR = PLACED_FEATURES.register("hive", () -> new PlacedFeature(CONFIGURED_HIVE_DECORATOR.getHolder().get(), List.of()));
}
