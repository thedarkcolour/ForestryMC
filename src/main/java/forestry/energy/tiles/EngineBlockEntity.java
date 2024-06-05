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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import forestry.api.core.IErrorLogic;
import forestry.core.blocks.BlockBase;
import forestry.core.config.Constants;
import forestry.core.errors.EnumErrorCode;
import forestry.core.network.IStreamableGui;
import forestry.core.network.packets.PacketActiveUpdate;
import forestry.core.tiles.IActivatable;
import forestry.core.tiles.TemperatureState;
import forestry.core.tiles.TileBase;
import forestry.core.utils.NetworkUtil;
import forestry.energy.EnergyHelper;
import forestry.energy.ForestryEnergyStorage;
import forestry.energy.EnergyTransferMode;

public abstract class EngineBlockEntity extends TileBase implements IActivatable, IStreamableGui {
	private static final int CANT_SEND_ENERGY_TIME = 20;

	private boolean active = false; // Used for smp.
	private int cantSendEnergyCountdown = CANT_SEND_ENERGY_TIME;
	/**
	 * Indicates whether the piston is receding from or approaching the
	 * combustion chamber
	 */
	public int stagePiston = 0;
	/**
	 * Piston speed as supplied by the server
	 */
	public float pistonSpeedServer = 0;

	protected int currentOutput = 0;
	protected int heat;
	protected final int maxHeat;
	protected boolean forceCooldown = false;
	public float progress;
	protected final ForestryEnergyStorage energyStorage;
	private final LazyOptional<IEnergyStorage> energyCap;
	private final String hintKey;

	protected EngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, String hintKey, int maxHeat, int maxEnergy) {
		super(type, pos, state);
		this.hintKey = hintKey;
		this.maxHeat = maxHeat;
		this.energyStorage = new ForestryEnergyStorage(2000, maxEnergy, EnergyTransferMode.EXTRACT);
		this.energyCap = LazyOptional.of(() -> energyStorage);
	}

	public String getHintKey() {
		return hintKey;
	}

	protected void addHeat(int i) {
		heat += i;

		if (heat > maxHeat) {
			heat = maxHeat;
		}
	}

	protected abstract void dissipateHeat();

	protected abstract void generateHeat();

	protected boolean mayBurn() {
		return !forceCooldown;
	}

	protected abstract void burn();


	@Override
	public void clientTick(Level level, BlockPos pos, BlockState state) {
		if (stagePiston != 0) {
			progress += pistonSpeedServer;

			if (progress > 1) {
				stagePiston = 0;
				progress = 0;
			}
		} else if (this.active) {
			stagePiston = 1;
		}
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state) {
		TemperatureState energyState = getTemperatureState();
		if (energyState == TemperatureState.MELTING && heat > 0) {
			forceCooldown = true;
		} else if (forceCooldown && heat <= 0) {
			forceCooldown = false;
		}

		IErrorLogic errorLogic = getErrorLogic();
		errorLogic.setCondition(forceCooldown, EnumErrorCode.FORCED_COOLDOWN);

		boolean enabledRedstone = isRedstoneActivated();
		errorLogic.setCondition(!enabledRedstone, EnumErrorCode.NO_REDSTONE);

		// Determine targeted tile
		BlockState blockState = getBlockState();
		Direction facing = blockState.getValue(BlockBase.FACING);
		BlockEntity tile = level.getBlockEntity(getBlockPos().relative(facing));

		float newPistonSpeed = getPistonSpeed();
		if (newPistonSpeed != pistonSpeedServer) {
			pistonSpeedServer = newPistonSpeed;
			sendNetworkUpdate();
		}

		if (stagePiston != 0) {
			progress += pistonSpeedServer;

			EnergyHelper.sendEnergy(energyStorage, facing, tile);

			if (progress > 0.25 && stagePiston == 1) {
				stagePiston = 2;
			} else if (progress >= 0.5) {
				progress = 0;
				stagePiston = 0;
			}
		} else if (enabledRedstone && EnergyHelper.isEnergyReceiverOrEngine(facing.getOpposite(), tile)) {
			if (EnergyHelper.canSendEnergy(energyStorage, facing, tile)) {
				stagePiston = 1; // If we can transfer energy, start running
				setActive(true);
				cantSendEnergyCountdown = CANT_SEND_ENERGY_TIME;
			} else {
				if (isActive()) {
					cantSendEnergyCountdown--;
					if (cantSendEnergyCountdown <= 0) {
						setActive(false);
					}
				}
			}
		} else {
			setActive(false);
		}

		dissipateHeat();
		generateHeat();
		// Now let's fire up the engine:
		if (mayBurn()) {
			burn();
		} else {
			energyStorage.drainEnergy(20);
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		if (this.active == active) {
			return;
		}
		this.active = active;

		if (!level.isClientSide) {
			NetworkUtil.sendNetworkPacket(new PacketActiveUpdate(this), worldPosition, level);
		}
	}

	// STATE INFORMATION
	protected double getHeatLevel() {
		return (double) heat / (double) maxHeat;
	}

	protected abstract boolean isBurning();

	public int getBurnTimeRemainingScaled(int i) {
		return 0;
	}

	public boolean hasFuelMin(float percentage) {
		return false;
	}

	public int getCurrentOutput() {
		if (isBurning() && isRedstoneActivated()) {
			return currentOutput;
		} else {
			return 0;
		}
	}

	public int getHeat() {
		return heat;
	}

	/**
	 * Returns the current energy state of the engine
	 */
	public TemperatureState getTemperatureState() {
		return TemperatureState.getState(heat, maxHeat);
	}

	protected float getPistonSpeed() {
		return switch (getTemperatureState()) {
			case COOL -> 0.03f;
			case WARMED_UP -> 0.04f;
			case OPERATING_TEMPERATURE -> 0.05f;
			case RUNNING_HOT -> 0.06f;
			case OVERHEATING -> 0.07f;
			case MELTING -> Constants.ENGINE_PISTON_SPEED_MAX;
			default -> 0;
		};
	}

	/* SAVING & LOADING */
	@Override
	public void load(CompoundTag nbt) {
		super.load( nbt);
		energyStorage.read(nbt);

		heat = nbt.getInt("EngineHeat");

		progress = nbt.getFloat("EngineProgress");
		forceCooldown = nbt.getBoolean("ForceCooldown");
	}


	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		energyStorage.write(nbt);

		nbt.putInt("EngineHeat", heat);
		nbt.putFloat("EngineProgress", progress);
		nbt.putBoolean("ForceCooldown", forceCooldown);
	}

	/* NETWORK */
	@Override
	public void writeData(FriendlyByteBuf data) {
		super.writeData(data);
		data.writeBoolean(active);
		data.writeInt(heat);
		data.writeFloat(pistonSpeedServer);
		energyStorage.writeData(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(FriendlyByteBuf data) {
		super.readData(data);
		active = data.readBoolean();
		heat = data.readInt();
		pistonSpeedServer = data.readFloat();
		energyStorage.readData(data);
	}

	@Override
	public void writeGuiData(FriendlyByteBuf data) {
		data.writeInt(currentOutput);
		data.writeInt(heat);
		data.writeBoolean(forceCooldown);
		energyStorage.writeData(data);
	}

	@Override
	public void readGuiData(FriendlyByteBuf data) {
		currentOutput = data.readInt();
		heat = data.readInt();
		forceCooldown = data.readBoolean();
		energyStorage.readData(data);
	}

	public ForestryEnergyStorage getEnergyManager() {
		return energyStorage;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		if (!remove && capability == ForgeCapabilities.ENERGY && side == getFacing()) {
			return energyCap.cast();
		}
		return super.getCapability(capability, side);
	}
}
