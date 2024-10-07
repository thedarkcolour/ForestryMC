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
package forestry.apiculture.multiblock;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import forestry.api.climate.IClimateControlled;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.blocks.BlockAlvearyType;
import forestry.core.tiles.IActivatable;
import forestry.energy.EnergyHelper;
import forestry.energy.EnergyTransferMode;
import forestry.energy.ForestryEnergyStorage;

// Used by Heater and Fan, which increase and decrease Temperature, respectively
public abstract class TileAlvearyClimatiser extends TileAlveary implements IActivatable, IAlvearyComponent.Climatiser<MultiblockLogicAlveary> {
	private static final int TICKS_PER_CYCLE = 1;
	private static final int FE_PER_OPERATION = 50;

	private final ForestryEnergyStorage energyStorage;
	private final LazyOptional<ForestryEnergyStorage> energyCap;
	private final byte temperatureSteps;

	private int workingTime = 0;

	protected TileAlvearyClimatiser(BlockAlvearyType alvearyType, BlockPos pos, BlockState state, byte temperatureSteps) {
		super(alvearyType, pos, state);
		this.temperatureSteps = temperatureSteps;

		this.energyStorage = new ForestryEnergyStorage(1000, 2000, EnergyTransferMode.RECEIVE);
		this.energyCap = LazyOptional.of(() -> this.energyStorage);
	}

	/* UPDATING */
	@Override
	public void changeClimate(int tick, IClimateControlled climateControlled) {
		if (workingTime < 20 && EnergyHelper.consumeEnergyToDoWork(energyStorage, TICKS_PER_CYCLE, FE_PER_OPERATION)) {
			// one tick of work for every 10 RF
			workingTime += FE_PER_OPERATION / 10;
		}

		if (workingTime > 0) {
			workingTime--;
			climateControlled.addTemperatureChange(this.temperatureSteps);
		}

		setActive(workingTime > 0);
	}

	/* LOADING & SAVING */
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);
		energyStorage.read(compoundNBT);
		workingTime = compoundNBT.getInt("Heating");
	}

	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);
		energyStorage.write(compoundNBT);
		compoundNBT.putInt("Heating", workingTime);
	}

	/* Network */
	@Override
	protected void encodeDescriptionPacket(CompoundTag packetData) {
		super.encodeDescriptionPacket(packetData);
	}

	@Override
	protected void decodeDescriptionPacket(CompoundTag packetData) {
		super.decodeDescriptionPacket(packetData);
	}

	/* IActivatable */
	@Override
	public boolean isActive() {
		return getBlockState().getValue(BlockAlveary.STATE) == BlockAlveary.State.ON;
	}

	@Override
	public void setActive(boolean active) {
		if (isActive() != active) {
			this.level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(BlockAlveary.STATE, active ? BlockAlveary.State.ON : BlockAlveary.State.OFF));
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!remove && capability == ForgeCapabilities.ENERGY) {
			return energyCap.cast();
		}
		return super.getCapability(capability, facing);
	}
}
