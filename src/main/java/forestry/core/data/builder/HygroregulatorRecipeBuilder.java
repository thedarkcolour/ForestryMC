package forestry.core.data.builder;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.minecraftforge.fluids.FluidStack;

import forestry.factory.features.FactoryRecipeTypes;
import forestry.factory.recipes.RecipeSerializers;

public class HygroregulatorRecipeBuilder {
	private FluidStack liquid;
	private int humiditySteps;
	private int temperatureSteps;
	private int retainTime;

	public HygroregulatorRecipeBuilder setLiquid(FluidStack liquid) {
		this.liquid = liquid;
		return this;
	}

	public HygroregulatorRecipeBuilder setHumiditySteps(int humiditySteps) {
		this.humiditySteps = humiditySteps;
		return this;
	}

	public HygroregulatorRecipeBuilder setTemperatureSteps(int temperatureSteps) {
		this.temperatureSteps = temperatureSteps;
		return this;
	}

	public HygroregulatorRecipeBuilder setRetainTime(int retainTime) {
		this.retainTime = retainTime;
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.liquid, this.retainTime, this.humiditySteps, this.temperatureSteps));
	}

	private static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final FluidStack liquid;
		private final byte humiditySteps;
		private final byte temperatureSteps;
		private final int retainTime;

		public Result(ResourceLocation id, FluidStack liquid, int retainTime, int humiditySteps, int temperatureSteps) {
			this.id = id;
			this.liquid = liquid;
			this.retainTime = retainTime;
			this.humiditySteps = (byte) humiditySteps;
			this.temperatureSteps = (byte) temperatureSteps;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("liquid", RecipeSerializers.serializeFluid(liquid));
			json.addProperty("time", retainTime);
			json.addProperty("humidity_steps", humiditySteps);
			json.addProperty("temperature_steps", temperatureSteps);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return FactoryRecipeTypes.HYGROREGULATOR.serializer();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
