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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.fuels.FuelManager;
import forestry.core.config.Constants;
import forestry.api.core.ForestryError;
import forestry.core.features.CoreItems;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.tiles.TemperatureState;
import forestry.energy.features.EnergyTiles;
import forestry.energy.menu.PeatEngineMenu;
import forestry.energy.inventory.InventoryEnginePeat;

public class PeatEngineBlockEntity extends EngineBlockEntity implements WorldlyContainer {
	private ItemStack fuel = ItemStack.EMPTY;
	private int burnTime;
	private int totalBurnTime;
	private int ashProduction;
	private final int ashForItem;

	public PeatEngineBlockEntity(BlockPos pos, BlockState state) {
		super(EnergyTiles.PEAT_ENGINE.tileType(), pos, state, "engine.copper", Constants.ENGINE_COPPER_HEAT_MAX, 200000);

		ashForItem = Constants.ENGINE_COPPER_ASH_FOR_ITEM;
		setInternalInventory(new InventoryEnginePeat(this));
	}

	private int getFuelSlot() {
		IInventoryAdapter inventory = getInternalInventory();
		if (inventory.getItem(InventoryEnginePeat.SLOT_FUEL).isEmpty()) {
			return -1;
		}

		if (determineFuelValue(inventory.getItem(InventoryEnginePeat.SLOT_FUEL)) > 0) {
			return InventoryEnginePeat.SLOT_FUEL;
		}

		return -1;
	}

	private int getFreeWasteSlot() {
		IInventoryAdapter inventory = getInternalInventory();
		for (int i = InventoryEnginePeat.SLOT_WASTE_1; i <= InventoryEnginePeat.SLOT_WASTE_COUNT; i++) {
			ItemStack waste = inventory.getItem(i);
			if (waste.isEmpty()) {
				return i;
			}

			if (!CoreItems.ASH.itemEqual(waste)) {
				continue;
			}

			if (waste.getCount() < waste.getMaxStackSize()) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state) {
		super.serverTick(level, pos, state);

		if (updateOnInterval(40)) {
			int fuelSlot = getFuelSlot();
			boolean hasFuel = fuelSlot >= 0 && determineBurnDuration(getInternalInventory().getItem(fuelSlot)) > 0;
			getErrorLogic().setCondition(!hasFuel, ForestryError.NO_FUEL);
		}
	}

	@Override
	public void burn() {

		currentOutput = 0;

		if (burnTime > 0) {
			burnTime--;
			addAsh(1);

			if (isRedstoneActivated()) {
				currentOutput = determineFuelValue(fuel);
				energyStorage.generateEnergy(currentOutput);
				level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());    //TODO - I thuink
			}
		} else if (isRedstoneActivated()) {
			int fuelSlot = getFuelSlot();
			int wasteSlot = getFreeWasteSlot();

			if (fuelSlot >= 0 && wasteSlot >= 0) {
				IInventoryAdapter inventory = getInternalInventory();
				ItemStack fuelStack = inventory.getItem(fuelSlot);
				burnTime = totalBurnTime = determineBurnDuration(fuelStack);
				if (burnTime > 0 && !fuelStack.isEmpty()) {
					fuel = fuelStack.copy();
					removeItem(fuelSlot, 1);
				}
			}
		}
	}

	@Override
	public void dissipateHeat() {
		if (heat <= 0) {
			return;
		}

		int loss = 0;

		if (!isBurning()) {
			loss += 1;
		}

		TemperatureState tempState = getTemperatureState();
		if (tempState == TemperatureState.OVERHEATING || tempState == TemperatureState.OPERATING_TEMPERATURE) {
			loss += 1;
		}

		heat -= loss;
	}

	@Override
	public void generateHeat() {

		int heatToAdd = 0;

		if (isBurning()) {
			heatToAdd++;
			if ((double) energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored() > 0.5) {
				heatToAdd++;
			}
		}

		addHeat(heatToAdd);
	}

	private void addAsh(int amount) {

		ashProduction += amount;
		if (ashProduction < ashForItem) {
			return;
		}

		// If we have reached the necessary amount, we need to add ash
		int wasteSlot = getFreeWasteSlot();
		if (wasteSlot >= 0) {
			IInventoryAdapter inventory = getInternalInventory();
			ItemStack wasteStack = inventory.getItem(wasteSlot);
			if (wasteStack.isEmpty()) {
				inventory.setItem(wasteSlot, CoreItems.ASH.stack());
			} else {
				wasteStack.grow(1);
			}
		}
		// Reset
		ashProduction = 0;
	}

	/**
	 * Returns the fuel value (power per cycle) an item of the passed ItemStack provides
	 */
	private static int determineFuelValue(ItemStack fuel) {
		if (FuelManager.peatEngineFuel.containsKey(fuel)) {
			return FuelManager.peatEngineFuel.get(fuel).powerPerCycle();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the fuel value (power per cycle) an item of the passed ItemStack provides
	 */
	private static int determineBurnDuration(ItemStack fuel) {
		if (FuelManager.peatEngineFuel.containsKey(fuel)) {
			return FuelManager.peatEngineFuel.get(fuel).burnDuration();
		} else {
			return 0;
		}
	}

	// / STATE INFORMATION
	@Override
	public boolean isBurning() {
		return mayBurn() && burnTime > 0;
	}

	@Override
	public int getBurnTimeRemainingScaled(int i) {
		if (totalBurnTime == 0) {
			return 0;
		}

		return burnTime * i / totalBurnTime;
	}

	@Override
	public boolean hasFuelMin(float percentage) {
		int fuelSlot = this.getFuelSlot();
		if (fuelSlot < 0) {
			return false;
		}

		IInventoryAdapter inventory = getInternalInventory();
		return (float) inventory.getItem(fuelSlot).getCount() / (float) inventory.getItem(fuelSlot).getMaxStackSize() > percentage;
	}

	// / LOADING AND SAVING
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);

		if (compoundNBT.contains("EngineFuelItemStack")) {
			CompoundTag fuelItemNbt = compoundNBT.getCompound("EngineFuelItemStack");
			fuel = ItemStack.of(fuelItemNbt);
		}

		burnTime = compoundNBT.getInt("EngineBurnTime");
		totalBurnTime = compoundNBT.getInt("EngineTotalTime");
		if (compoundNBT.contains("AshProduction")) {
			ashProduction = compoundNBT.getInt("AshProduction");
		}
	}


	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		if (!fuel.isEmpty()) {
			nbt.put("EngineFuelItemStack", fuel.serializeNBT());
		}

		nbt.putInt("EngineBurnTime", burnTime);
		nbt.putInt("EngineTotalTime", totalBurnTime);
		nbt.putInt("AshProduction", ashProduction);
	}

	@Override
	public void writeGuiData(FriendlyByteBuf data) {
		super.writeGuiData(data);
		data.writeInt(burnTime);
		data.writeInt(totalBurnTime);
	}

	@Override
	public void readGuiData(FriendlyByteBuf data) {
		super.readGuiData(data);
		burnTime = data.readInt();
		totalBurnTime = data.readInt();
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory pPlayerInventory, Player pPlayer) {
		return new PeatEngineMenu(windowId, pPlayerInventory, this);
	}
}
