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
package forestry.farming.tiles;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import forestry.api.multiblock.IFarmComponent;
import forestry.api.multiblock.IFarmController;
import forestry.energy.EnergyHelper;
import forestry.energy.ForestryEnergyStorage;
import forestry.farming.features.FarmingTiles;

public class TileFarmGearbox extends TileFarm implements IFarmComponent.Active {
	private static final int WORK_CYCLES = 4;
	private static final int ENERGY_PER_OPERATION = WORK_CYCLES * 50;

	private final ForestryEnergyStorage energyStorage;
	private final LazyOptional<IEnergyStorage> energyCap;

	private int activationDelay = 0;
	private int previousDelays = 0;
	private int workCounter;

	public TileFarmGearbox(BlockPos pos, BlockState state) {
		super(FarmingTiles.GEARBOX.tileType(), pos, state);

		this.energyStorage = new ForestryEnergyStorage(200, 10000);
		this.energyCap = LazyOptional.of(() -> energyStorage);
	}

	/* SAVING & LOADING */
	@Override
	public void load(CompoundTag compoundNBT) {
		super.load(compoundNBT);

		energyStorage.read(compoundNBT);

		activationDelay = compoundNBT.getInt("ActivationDelay");
		previousDelays = compoundNBT.getInt("PrevDelays");
	}


	@Override
	public void saveAdditional(CompoundTag compoundNBT) {
		super.saveAdditional(compoundNBT);

		energyStorage.write(compoundNBT);

		compoundNBT.putInt("ActivationDelay", activationDelay);
		compoundNBT.putInt("PrevDelays", previousDelays);
	}

	@Override
	public void updateServer(int tickCount) {
		if (energyStorage.getEnergyStored() <= 0) {
			return;
		}

		if (activationDelay > 0) {
			activationDelay--;
			return;
		}

		// Hard limit to 4 cycles / second.
		if (workCounter < WORK_CYCLES && EnergyHelper.consumeEnergyToDoWork(energyStorage, WORK_CYCLES, ENERGY_PER_OPERATION)) {
			workCounter++;
		}

		if (workCounter >= WORK_CYCLES && tickCount % 5 == 0) {
			IFarmController farmController = getMultiblockLogic().getController();
			if (farmController.doWork()) {
				workCounter = 0;
				previousDelays = 0;
			} else {
				// If the central TE doesn't have work, we add to the activation delay to throttle the CPU usage.
				activationDelay = Math.min(10 * previousDelays, 120);
				previousDelays++; // First delay is free!
			}
		}
	}

	@Override
	public void updateClient(int tickCount) {
		// todo add sided multiblock component ticking and remove this
	}

	public ForestryEnergyStorage getEnergyManager() {
		return energyStorage;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction facing) {
		if (!remove && cap == ForgeCapabilities.ENERGY) {
			return energyCap.cast();
		}
		return super.getCapability(cap, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		energyCap.invalidate();
	}
}
