package forestry.modules.features;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import forestry.core.fluids.BlockForestryFluid;
import forestry.core.items.definitions.DrinkProperties;

public class FeatureFluid extends ModFeature implements IFluidFeature {
	private final FeatureBlock<BlockForestryFluid, BlockItem> block;
	private final FluidProperties properties;
	private final ForgeFlowingFluid.Properties internal;

	private final RegistryObject<? extends FlowingFluid> fluidObject;
	private final RegistryObject<? extends FlowingFluid> flowingFluidObject;

	public FeatureFluid(Builder builder) {
		super(builder.moduleID, builder.registry.getModId(), builder.identifier);
		this.block = builder.registry.block(() -> new BlockForestryFluid(this), "fluid_" + builder.identifier);
		this.properties = new FluidProperties(builder);
		RegistryObject<FluidType> attributes = builder.registry.getRegistry(ForgeRegistries.Keys.FLUID_TYPES).register(identifier, () -> new ForestryFluidType(this.properties, FluidType.Properties.create()
				.density(properties.density)
				.viscosity(properties.viscosity)
				.temperature(properties.temperature)));
		DeferredRegister<Fluid> fluidRegistry = builder.registry.getRegistry(Registry.FLUID_REGISTRY);
		this.internal = new ForgeFlowingFluid.Properties(attributes, this::fluid, this::flowing).block(block::block).bucket(properties().bucket);
		this.fluidObject = fluidRegistry.register(identifier, () -> new ForgeFlowingFluid.Source(internal));
		this.flowingFluidObject = fluidRegistry.register(identifier + "_flowing", () -> new ForgeFlowingFluid.Flowing(internal));
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.FLUID_REGISTRY;
	}

	@Override
	public FeatureBlock<BlockForestryFluid, BlockItem> fluidBlock() {
		return block;
	}

	@Override
	public FlowingFluid fluid() {
		return fluidObject.get();
	}

	@Override
	public FlowingFluid flowing() {
		return flowingFluidObject.get();
	}

	@Override
	public FluidProperties properties() {
		return properties;
	}

	public static class Builder {
		private final IFeatureRegistry registry;
		private final String moduleID;
		final String identifier;

		int density = 1000;
		int viscosity = 1000;
		int temperature = 295;
		Color particleColor = Color.WHITE;
		int flammability = 0;
		boolean flammable = false;
		@Nullable
		DrinkProperties properties = null;
		Supplier<Item> bucket = () -> Items.AIR;

		public Builder(IFeatureRegistry registry, String moduleID, String identifier) {
			this.registry = registry;
			this.moduleID = moduleID;
			this.identifier = identifier;
		}

		public Builder flammable() {
			this.flammable = true;
			return this;
		}

		public Builder flammability(int flammability) {
			this.flammability = flammability;
			return this;
		}

		public Builder density(int density) {
			this.density = density;
			return this;
		}

		public Builder viscosity(int viscosity) {
			this.viscosity = viscosity;
			return this;
		}

		public Builder temperature(int temperature) {
			this.temperature = temperature;
			return this;
		}

		public Builder particleColor(Color color) {
			this.particleColor = color;
			return this;
		}

		public Builder bucket(Supplier<Item> bucket) {
			this.bucket = bucket;
			return this;
		}

		public Builder drinkProperties(int healAmount, float saturationModifier, int maxItemUseDuration) {
			this.properties = new DrinkProperties(healAmount, saturationModifier, maxItemUseDuration);
			return this;
		}

		public FeatureFluid create() {
			return registry.register(new FeatureFluid(this));
		}
	}

	public static class ForestryFluidType extends FluidType {
		private final int color;
		private final ResourceLocation stillTexture;
		private final ResourceLocation flowingTexture;

		public ForestryFluidType(FluidProperties forestryProps, Properties properties) {
			super(properties);
			this.color = forestryProps.particleColor.getRGB();
			this.stillTexture = forestryProps.resources[0];
			this.flowingTexture = forestryProps.resources[1];
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public ResourceLocation getStillTexture() {
					return stillTexture;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return flowingTexture;
				}

				@Override
				public int getTintColor() {
					return color;
				}
			});
		}
	}
}
