/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************//*

package forestry.climatology.tiles;

import javax.annotation.Nullable;
import java.util.Objects;

import forestry.api.climate.ClimateState;
import forestry.api.recipes.IHygroregulatorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import forestry.api.climate.ClimateCapabilities;
import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateManipulator;
import forestry.api.climate.IClimateTransformer;
import forestry.api.climate.IClimatised;
import forestry.api.core.IErrorLogic;
import forestry.api.recipes.IHygroregulatorRecipe;
import forestry.core.recipes.RecipeManagers;
import forestry.climatology.features.ClimatologyTiles;
import forestry.climatology.gui.ContainerHabitatFormer;
import forestry.climatology.inventory.InventoryHabitatFormer;
import forestry.core.climate.ClimateTransformer;
import forestry.core.config.Constants;
import forestry.api.core.ForestryError;
import forestry.core.fluids.FilteredTank;
import forestry.core.fluids.FluidHelper;
import forestry.core.fluids.ITankManager;
import forestry.core.fluids.TankManager;
import forestry.core.tiles.ILiquidTankTile;
import forestry.core.tiles.TilePowered;
import forestry.energy.ForestryEnergyStorage;

public class TileHabitatFormer extends TilePowered implements IClimateHousing, IClimatised, ILiquidTankTile {
	private static final String TRANSFORMER_KEY = "Transformer";

	//The logic that handles the climate  changes.
	private final ClimateTransformer transformer;

	private final FilteredTank resourceTank;
	private final TankManager tankManager;

	public TileHabitatFormer(BlockPos pos, BlockState state) {
		super(ClimatologyTiles.HABITAT_FORMER.tileType(), pos, state, 1200, 10000);
		this.transformer = new ClimateTransformer(this);
		setInternalInventory(new InventoryHabitatFormer(this));
		this.resourceTank = new FilteredTank(Constants.PROCESSOR_TANK_CAPACITY).setFilters(() -> RecipeManagers.hygroregulatorManager.getRecipeFluids(level.getRecipeManager()));
		this.tankManager = new TankManager(this, resourceTank);
		setTicksPerWorkCycle(10);
		setEnergyPerWorkCycle(0);
	}

	@Override
	public ITankManager getTankManager() {
		return tankManager;
	}

	@Override
	public void onLoad() {
		if (!this.level.isClientSide) {
			this.transformer.onAdded((ServerLevel) this.level, this.worldPosition);
		}
	}

	@Override
	public void setRemoved() {
		if (!this.level.isClientSide) {
			this.transformer.onRemoved((ServerLevel) level);
		}
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state) {
		super.serverTick(level, pos, state);
		if (updateOnInterval(20)) {
			// Check if we have suitable items waiting in the item slot
			FluidHelper.drainContainers(tankManager, this, 0);
		}
	}

	@Override
	public boolean hasWork() {
		return true;
	}

	@Nullable
	private FluidStack cachedStack = null;

	@Override
	protected boolean workCycle() {
		IErrorLogic errorLogic = getErrorLogic();
		ClimateState defaultState = transformer.getDefault();
		IClimateState currentState = transformer.getCurrent();
		IClimateState changedState = transformer.getTarget().subtract(currentState);
		int humidityDifference = transformer.();
		cachedStack = null;
		if (difference.getHumidity() != 0.0F) {
			updateHumidity(errorLogic, changedState);
		}
		if (difference.getTemperature() != 0.0F) {
			updateTemperature(errorLogic, changedState);
		}
		return true;
	}

	private void updateHumidity(IErrorLogic errorLogic, ClimateState changedState) {
		IClimateManipulator manipulator = transformer.createManipulator(ClimateType.HUMIDITY, false);
		if (manipulator.canAdd()) {
			errorLogic.setCondition(false, ForestryError.WRONG_RESOURCE);
			int currentCost = getFluidCost(changedState);
			if (!resourceTank.drain(currentCost, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
				IClimateState simulatedState = */
/*changedState.add(ClimateType.HUMIDITY, climateChange)*//*

						changedState.toImmutable().add(manipulator.addChange(true));
				int fluidCost = getFluidCost(simulatedState);
				if (!resourceTank.drain(fluidCost, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
					cachedStack = resourceTank.drain(fluidCost, IFluidHandler.FluidAction.EXECUTE);
					manipulator.addChange(false);
				} else {
					cachedStack = resourceTank.drain(currentCost, IFluidHandler.FluidAction.EXECUTE);
				}
				errorLogic.setCondition(false, ForestryError.NO_RESOURCE_LIQUID);
			} else {

				manipulator.removeChange(false);
				errorLogic.setCondition(true, ForestryError.NO_RESOURCE_LIQUID);
			}
		} else {
			if (resourceTank.isEmpty()) {
				errorLogic.setCondition(true, ForestryError.NO_RESOURCE_LIQUID);
			} else {
				errorLogic.setCondition(true, ForestryError.WRONG_RESOURCE);
				errorLogic.setCondition(false, ForestryError.NO_RESOURCE_LIQUID);
			}
		}
		manipulator.finish();
	}

	private void updateTemperature(IErrorLogic errorLogic, ClimateState changedState) {
		IClimateManipulator manipulator = transformer.createManipulator(ClimateType.TEMPERATURE, true);
		ForestryEnergyStorage energyStorage = getEnergyManager();
		int currentCost = getEnergyCost(changedState);
		if (energyStorage.extractEnergy(currentCost, true) > 0) {
			IClimateState simulatedState = manipulator.addChange(true);
			int energyCost = getEnergyCost(simulatedState);
			if (energyStorage.extractEnergy(energyCost, true) > 0) {
				energyStorage.extractEnergy(energyCost, false);
				manipulator.addChange(false);
			} else {
				energyStorage.extractEnergy(currentCost, false);
			}
			errorLogic.setCondition(false, ForestryError.NO_POWER);
		} else {
			manipulator.removeChange(false);
			errorLogic.setCondition(true, ForestryError.NO_POWER);
		}
		manipulator.finish();
	}

	private int getFluidCost(ClimateState state) {
		FluidStack fluid = resourceTank.getFluid();
		return RecipeManagers.hygroregulatorManager.findMatchingRecipe(null, fluid)
				.map(recipe -> getEnergyCost(state) * recipe.getResource().getAmount())
				.orElse(0);
	}

	private int getEnergyCost(ClimateState state) {
		return Math.round((1.0F + Mth.abs(state.temperature())) * transformer.getCostModifier());
	}

	@Override
	public float getChangeForState(ClimateType type, IClimateManipulator manipulator) {
		IHygroregulatorManager manager = RecipeManagers.hygroregulatorManager;

		if (type == ClimateType.HUMIDITY) {
			FluidStack fluid = resourceTank.getFluid();
			return manager.findMatchingRecipe(null, fluid)
					.map(IHygroregulatorRecipe::getHumidChange)
					.map(humidChange -> humidChange / transformer.getSpeedModifier())
					.orElse(0f);
		}

		if (cachedStack != null) {
			return manager.findMatchingRecipe(null, cachedStack)
					.map(IHygroregulatorRecipe::getTemperatureSteps)
					.map(tempChange -> (0.05F + Math.abs(tempChange)) * 0.5F / transformer.getSpeedModifier())
					.orElse(0f);
		}
		return 0;
	}

	private ClimateState getClimateDifference() {
		ClimateState defaultState = transformer.getDefault();
		ClimateState targetedState = transformer.getTarget();
		return targetedState.subtract(defaultState);
	}

	@Override
	public void markNetworkUpdate() {
        sendNetworkUpdate();
    }

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new ContainerHabitatFormer(windowId, inv, this);
	}

	@Override
	public Holder<Biome> getBiome() {
		Level level = Objects.requireNonNull(this.level);
		return level.getBiome(getBlockPos()).value();
	}

	@Override
	public float getExactTemperature() {
		return transformer.getCurrent().getTemperature();
	}

	@Override
	public float getExactHumidity() {
		return transformer.getCurrent().getHumidity();
	}

	*/
/* Methods - Implement IClimateHousing *//*

	@Override
	public IClimateTransformer getTransformer() {
		return transformer;
	}

	*/
/* Methods - Implement IStreamableGui *//*

	@Override
	public void writeGuiData(FriendlyByteBuf data) {
		super.writeGuiData(data);
		transformer.writeData(data);
	}

	@Override
	public void readGuiData(FriendlyByteBuf data) {
		super.readGuiData(data);
		transformer.readData(data);
	}

	*/
/* Methods - SAVING & LOADING *//*

	@Override
	public void saveAdditional(CompoundTag data) {
		super.saveAdditional(data);

		tankManager.write(data);

		data.put(TRANSFORMER_KEY, transformer.write(new CompoundTag()));
	}

	@Override
	public void load(CompoundTag data) {
		super.load(data);

		tankManager.read(data);

		if (data.contains(TRANSFORMER_KEY)) {
			CompoundTag nbtTag = data.getCompound(TRANSFORMER_KEY);
			transformer.read(nbtTag);
		}
	}

	*/
/* Network *//*

	@Override
	public void writeData(FriendlyByteBuf data) {
		super.writeData(data);
		tankManager.writeData(data);
		transformer.writeData(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(FriendlyByteBuf data) {
		super.readData(data);
		tankManager.readData(data);
		transformer.readData(data);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return LazyOptional.of(() -> tankManager).cast();
		}
		if (capability == ClimateCapabilities.CLIMATE_TRANSFORMER) {
			return LazyOptional.of(() -> transformer).cast();
		}
		return super.getCapability(capability, facing);
	}
}
*/
