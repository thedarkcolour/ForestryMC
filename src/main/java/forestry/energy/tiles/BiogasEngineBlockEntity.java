/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.energy.tiles;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import forestry.api.core.ForestryError;
import forestry.api.core.IErrorLogic;
import forestry.api.fuels.EngineBronzeFuel;
import forestry.api.fuels.FuelManager;
import forestry.core.config.Constants;
import forestry.core.fluids.FilteredTank;
import forestry.core.fluids.FluidHelper;
import forestry.core.fluids.FluidTagFilter;
import forestry.core.fluids.StandardTank;
import forestry.core.fluids.TankManager;
import forestry.core.tiles.ILiquidTankTile;
import forestry.energy.features.EnergyTiles;
import forestry.energy.inventory.InventoryEngineBiogas;
import forestry.energy.menu.BiogasEngineMenu;

import static net.minecraftforge.fluids.FluidType.BUCKET_VOLUME;

public class BiogasEngineBlockEntity extends EngineBlockEntity implements WorldlyContainer, ILiquidTankTile {
	public static final int ENGINE_BRONZE_HEAT_MAX = 10000;
	public static final int ENGINE_BRONZE_HEAT_GENERATION_ENERGY = 1;
	private final FilteredTank fuelTank;
	private final FilteredTank heatingTank;
	private final StandardTank burnTank;
	private final TankManager tankManager;

	private boolean shutdown; // true if the engine is too cold and needs to warm itself up.

	private final LazyOptional<IFluidHandler> fluidCap;

	public BiogasEngineBlockEntity(BlockPos pos, BlockState state) {
		super(EnergyTiles.BIOGAS_ENGINE.tileType(), pos, state, "engine.bronze", ENGINE_BRONZE_HEAT_MAX, 300000);

		setInternalInventory(new InventoryEngineBiogas(this));

		this.fuelTank = new FilteredTank(Constants.ENGINE_TANK_CAPACITY).setFilters(FuelManager.biogasEngineFuel.keySet());
		this.heatingTank = new FilteredTank(Constants.ENGINE_TANK_CAPACITY, true, false).setFilter(FluidTagFilter.LAVA);
		this.burnTank = new StandardTank(BUCKET_VOLUME, false, false);

		this.tankManager = new TankManager(this, fuelTank, heatingTank, burnTank);
		this.fluidCap = LazyOptional.of(() -> tankManager);
	}

	@Override
	public TankManager getTankManager() {
		return tankManager;
	}

	@Nullable
	public Fluid getBurnTankFluidType() {
		return burnTank.getFluidType();
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state) {
		super.serverTick(level, pos, state);
		if (!updateOnInterval(20)) {
			return;
		}

		// Check if we have suitable items waiting in the item slot
		FluidHelper.drainContainers(tankManager, this, InventoryEngineBiogas.SLOT_CAN);

		IErrorLogic errorLogic = getErrorLogic();

		boolean hasHeat = getHeatLevel() > 0.2 || heatingTank.getFluidAmount() > 0;
		errorLogic.setCondition(!hasHeat, ForestryError.NO_HEAT);

		boolean hasFuel = burnTank.getFluidAmount() > 0 || fuelTank.getFluidAmount() > 0;
		errorLogic.setCondition(!hasFuel, ForestryError.NO_FUEL);
	}

	/**
	 * Burns fuel increasing stored energy
	 */
	@Override
	public void burn() {

		currentOutput = 0;

		if (isRedstoneActivated() && (fuelTank.getFluidAmount() >= BUCKET_VOLUME || burnTank.getFluidAmount() > 0)) {

			double heatStage = getHeatLevel();

			// If we have reached a safe temperature, enable energy transfer
			if (heatStage > 0.25 && shutdown) {
				shutdown(false);
			} else if (shutdown) {
				if (heatingTank.getFluidAmount() > 0 && heatingTank.getFluidType() == Fluids.LAVA) {
					addHeat(Constants.ENGINE_HEAT_VALUE_LAVA);
					heatingTank.drainInternal(1, IFluidHandler.FluidAction.EXECUTE);
				}
			}

			// We need a minimum temperature to generate energy
			if (heatStage > 0.2) {
				if (burnTank.getFluidAmount() > 0) {
					FluidStack drained = burnTank.drainInternal(1, IFluidHandler.FluidAction.EXECUTE);
					currentOutput = determineFuelValue(drained);
					energyStorage.generateEnergy(currentOutput);
					level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
				} else {
					FluidStack fuel = fuelTank.drainInternal(BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
					int burnTime = determineBurnTime(fuel);
					if (!fuel.isEmpty()) {
						fuel.setAmount(burnTime);
					}
					burnTank.setCapacity(burnTime);
					burnTank.setFluid(fuel);
				}
			} else {
				shutdown(true);
			}
		}
	}

	private void shutdown(boolean val) {
		shutdown = val;
	}

	@Override
	public void dissipateHeat() {
		if (heat <= 0) {
			return;
		}

		int loss = 1; // Basic loss even when running

		if (!isBurning()) {
			loss++;
		}

		double heatStage = getHeatLevel();
		if (heatStage > 0.55) {
			loss++;
		}

		// Lose extra heat when using water as fuel.
		if (fuelTank.getFluidAmount() > 0) {
			FluidStack fuelFluidStack = fuelTank.getFluid();
			if (!fuelFluidStack.isEmpty()) {
				EngineBronzeFuel fuel = FuelManager.biogasEngineFuel.get(fuelFluidStack.getFluid());
				if (fuel != null) {
					loss = loss * fuel.dissipationMultiplier();
				}
			}
		}

		heat -= loss;
	}

	@Override
	public void generateHeat() {

		int generate = 0;

		if (isRedstoneActivated() && burnTank.getFluidAmount() > 0) {
			double heatStage = getHeatLevel();
			if (heatStage >= 0.75) {
				generate += ENGINE_BRONZE_HEAT_GENERATION_ENERGY * 3;
			} else if (heatStage > 0.24) {
				generate += ENGINE_BRONZE_HEAT_GENERATION_ENERGY * 2;
			} else if (heatStage > 0.2) {
				generate += ENGINE_BRONZE_HEAT_GENERATION_ENERGY;
			}
		}

		heat += generate;

	}

	/**
	 * Returns the fuel value (power per cycle) an item of the passed fluid
	 */
	private static int determineFuelValue(@Nullable FluidStack fluidStack) {
		if (fluidStack != null) {
			Fluid fluid = fluidStack.getFluid();
			if (FuelManager.biogasEngineFuel.containsKey(fluid)) {
				return FuelManager.biogasEngineFuel.get(fluid).powerPerCycle();
			}
		}
		return 0;
	}

	/**
	 * @return Duration of burn cycle of one bucket
	 */
	private static int determineBurnTime(@Nullable FluidStack fluidStack) {
		if (fluidStack != null) {
			Fluid fluid = fluidStack.getFluid();
			if (FuelManager.biogasEngineFuel.containsKey(fluid)) {
				return FuelManager.biogasEngineFuel.get(fluid).burnDuration();
			}
		}
		return 0;
	}

	// / STATE INFORMATION
	@Override
	protected boolean isBurning() {
		return mayBurn() && burnTank.getFluidAmount() > 0;
	}

	@Override
	public int getBurnTimeRemainingScaled(int i) {
		if (burnTank.getCapacity() == 0) {
			return 0;
		}

		return burnTank.getFluidAmount() * i / burnTank.getCapacity();
	}

	public int getOperatingTemperatureScaled(int i) {
		return (int) Math.round(heat * i / (maxHeat * 0.2));
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);

		if (nbt.contains("shutdown")) {
			shutdown = nbt.getBoolean("shutdown");
		}
		tankManager.read(nbt);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		nbt.putBoolean("shutdown", shutdown);
		tankManager.write(nbt);
	}

	/* NETWORK */
	@Override
	public void writeData(FriendlyByteBuf data) {
		super.writeData(data);
		data.writeBoolean(shutdown);
		tankManager.writeData(data);
		burnTank.writeData(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(FriendlyByteBuf data) {
		super.readData(data);
		shutdown = data.readBoolean();
		tankManager.readData(data);
		burnTank.readData(data);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction facing) {
		if (!remove && cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluidCap.cast();
		}
		return super.getCapability(cap, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.fluidCap.invalidate();
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new BiogasEngineMenu(windowId, inv, this);
	}
}
