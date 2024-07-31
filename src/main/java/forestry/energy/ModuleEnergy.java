package forestry.energy;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import forestry.api.client.IClientModuleHandler;
import forestry.api.fuels.EngineBronzeFuel;
import forestry.api.fuels.EngineCopperFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.core.config.Constants;
import forestry.core.features.CoreItems;
import forestry.core.fluids.ForestryFluids;
import forestry.core.utils.datastructures.FluidMap;
import forestry.core.utils.datastructures.ItemStackMap;
import forestry.energy.client.EnergyClientHandler;
import forestry.modules.BlankForestryModule;

@ForestryModule
public class ModuleEnergy extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.ENERGY;
	}

	@Override
	public void setupApi() {
		setupFuelManager();
	}

	private static void setupFuelManager() {
		FuelManager.biogasEngineFuel = new FluidMap<>();
		FuelManager.peatEngineFuel = new ItemStackMap<>();
	}

	@Override
	public void preInit() {
		// Biogas Engine
		Fluid biomass = ForestryFluids.BIOMASS.getFluid();
		FuelManager.biogasEngineFuel.put(biomass, new EngineBronzeFuel(biomass,
				Constants.ENGINE_FUEL_VALUE_BIOMASS, Constants.ENGINE_CYCLE_DURATION_BIOMASS, 1));

		FuelManager.biogasEngineFuel.put(Fluids.WATER, new EngineBronzeFuel(Fluids.WATER,
				Constants.ENGINE_FUEL_VALUE_WATER, Constants.ENGINE_CYCLE_DURATION_WATER, 3));

		Fluid milk = ForestryFluids.MILK.getFluid();
		FuelManager.biogasEngineFuel.put(milk, new EngineBronzeFuel(milk,
				Constants.ENGINE_FUEL_VALUE_MILK, Constants.ENGINE_CYCLE_DURATION_MILK, 3));

		Fluid seedOil = ForestryFluids.SEED_OIL.getFluid();
		FuelManager.biogasEngineFuel.put(seedOil, new EngineBronzeFuel(seedOil,
				Constants.ENGINE_FUEL_VALUE_SEED_OIL, Constants.ENGINE_CYCLE_DURATION_SEED_OIL, 1));

		Fluid honey = ForestryFluids.HONEY.getFluid();
		FuelManager.biogasEngineFuel.put(honey, new EngineBronzeFuel(honey,
				Constants.ENGINE_FUEL_VALUE_HONEY, Constants.ENGINE_CYCLE_DURATION_HONEY, 1));

		Fluid juice = ForestryFluids.JUICE.getFluid();
		FuelManager.biogasEngineFuel.put(juice, new EngineBronzeFuel(juice,
				Constants.ENGINE_FUEL_VALUE_JUICE, Constants.ENGINE_CYCLE_DURATION_JUICE, 1));

		// Peat Engine
		ItemStack peat = CoreItems.PEAT.stack();
		FuelManager.peatEngineFuel.put(peat, new EngineCopperFuel(peat, Constants.ENGINE_COPPER_FUEL_VALUE_PEAT, Constants.ENGINE_COPPER_CYCLE_DURATION_PEAT));

		ItemStack bituminousPeat = CoreItems.BITUMINOUS_PEAT.stack();
		FuelManager.peatEngineFuel.put(bituminousPeat, new EngineCopperFuel(bituminousPeat, Constants.ENGINE_COPPER_FUEL_VALUE_BITUMINOUS_PEAT, Constants.ENGINE_COPPER_CYCLE_DURATION_BITUMINOUS_PEAT));
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new EnergyClientHandler());
	}
}
