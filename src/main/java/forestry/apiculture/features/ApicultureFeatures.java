package forestry.apiculture.features;

import forestry.api.ForestryConstants;
import forestry.apiculture.hives.HiveDecorator;
import forestry.core.utils.ForgeUtils;
import forestry.modules.features.FeatureProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@FeatureProvider
public class ApicultureFeatures {
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, ForestryConstants.MOD_ID);
	private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ForestryConstants.MOD_ID);
	private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, ForestryConstants.MOD_ID);

	public static final RegistryObject<HiveDecorator> HIVE_DECORATOR = FEATURES.register("hive", HiveDecorator::new);
	public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_HIVE_DECORATOR = CONFIGURED_FEATURES.register("hive", () -> new ConfiguredFeature<>(HIVE_DECORATOR.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<?> PLACED_HIVE_DECORATOR = PLACED_FEATURES.register("hive", () -> new PlacedFeature(CONFIGURED_HIVE_DECORATOR.getHolder().get(), List.of()));

	static {
		IEventBus modBus = ForgeUtils.modBus();

		FEATURES.register(modBus);
		CONFIGURED_FEATURES.register(modBus);
		PLACED_FEATURES.register(modBus);
	}
}
