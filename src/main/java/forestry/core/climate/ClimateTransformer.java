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
package forestry.core.climate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateManager;
import forestry.api.climate.IClimateManipulator;
import forestry.api.climate.IClimateTransformer;
import forestry.api.climate.IClimatised;
import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;
import forestry.core.config.Config;
import forestry.core.network.IStreamable;
import forestry.core.utils.NetworkUtil;

public class ClimateTransformer implements IClimateTransformer, IStreamable, INbtReadable, INbtWritable {
	private static final String CURRENT_STATE_KEY = "Current";

	private static final String STATE_KEY = "State";
	private static final String TARGETED_STATE_KEY = "Target";
	private static final String CIRCULAR_KEY = "Circular";
	private static final String RANGE_KEY = "Range";

	// The owning block entity
	protected final IClimateHousing housing;
	@Nullable
	private ClimateState targetState;
	@Nullable
	private ClimateState actualState;
	@Nullable
	private ClimateState originalState;
	//The range of the habitatformer in blocks in one direction.
	private int range;
	//The area of the former in blocks.
	private int area;
	//True if the area of the former is circular.
	private boolean circular;

	public ClimateTransformer(IClimateHousing housing) {
		this.housing = housing;
		setRange(Config.habitatformerRange);
		this.circular = true;
	}

	// Only for network deserialization
	public ClimateTransformer(FriendlyByteBuf buffer) {
		readData(buffer);
		this.housing = null;
	}

	@Override
	public IClimateHousing getHousing() {
		return housing;
	}

	@Override
	public void onAdded(ServerLevel level, BlockPos pos) {
		this.originalState = IForestryApi.INSTANCE.getClimateManager().getBiomeState(level, pos);
		if (this.targetState != null) {
			setCurrent(this.originalState);
			setTarget(originalState);
		}
		WorldClimateHolder worldClimate = WorldClimateHolder.get((ServerLevel) getWorldObj());
		worldClimate.updateTransformer(this);
	}

	/* Climate Holders */
	@Override
	public void onRemoved(ServerLevel level) {
		WorldClimateHolder worldClimate = WorldClimateHolder.get((ServerLevel) getWorldObj());
		worldClimate.updateTransformer(this);
	}

	/* Save and Load */
	@Override
	public CompoundTag write(CompoundTag nbt) {
		if (this.actualState != null) {
			nbt.put(CURRENT_STATE_KEY, this.actualState.writeToNbt());
		}
		if (this.targetState != null) {
			nbt.put(TARGETED_STATE_KEY, this.targetState.writeToNbt());
		}
		nbt.putBoolean(CIRCULAR_KEY, circular);
		nbt.putInt(RANGE_KEY, range);
		return nbt;
	}

	@Override
	public void read(CompoundTag nbt) {
		IClimateManager climateFactory = IForestryApi.INSTANCE.getClimateManager();
		this.actualState = ClimateState.readFromNbt(nbt.getCompound(CURRENT_STATE_KEY));
		this.targetState = ClimateState.readFromNbt(nbt.getCompound(TARGETED_STATE_KEY));
		this.circular = nbt.getBoolean(CIRCULAR_KEY);
		this.range = nbt.getInt(RANGE_KEY);
		onAreaChange(range, circular);
	}

	@Override
	public IClimateManipulator createManipulator(ClimateType type, boolean allowBackwards) {
		return new ClimateManipulator(
				this.targetState,
				this.originalState,
				this.actualState,
				housing::getChangeForState,
				this::setCurrent,
				allowBackwards,
				type
		);
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		NetworkUtil.writeClimateState(data, actualState);
		NetworkUtil.writeClimateState(data, targetState);
		NetworkUtil.writeClimateState(data, originalState);
		data.writeBoolean(circular);
		data.writeVarInt(range);
		onAreaChange(range, circular);
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		actualState = NetworkUtil.readClimateState(data);
		targetState = NetworkUtil.readClimateState(data);
		originalState = NetworkUtil.readClimateState(data);
		circular = data.readBoolean();
		range = data.readVarInt();
	}

	public void copy(ClimateTransformer other) {
		this.actualState = other.actualState;
		this.targetState = other.targetState;
		this.originalState = other.originalState;
		this.circular = other.circular;
		this.range = other.range;
	}

	@Nullable
	@Override
	public ClimateState getCurrent() {
		return actualState;
	}

	@Override
	public void setCurrent(ClimateState state) {
		if (!state.equals(actualState)) {
			this.actualState = state;
			housing.markNetworkUpdate();
			WorldClimateHolder worldClimate = WorldClimateHolder.get((ServerLevel) getWorldObj());
			worldClimate.updateTransformer(this);
		}
	}

	@Nullable
	@Override
	public ClimateState getTarget() {
		return targetState;
	}

	@Override
	public void setTarget(@Nullable ClimateState target) {
		this.targetState = target;
		this.housing.markNetworkUpdate();
	}

	@Nullable
	@Override
	public ClimateState getDefault() {
		return originalState;
	}

	public void setCircular(boolean value) {
		if (this.circular != value) {
			this.circular = value;
			onAreaChange(range, !value);
			housing.markNetworkUpdate();
			WorldClimateHolder worldClimate = WorldClimateHolder.get((ServerLevel) getWorldObj());
			worldClimate.updateTransformer(this);
		}
	}

	@Override
	public boolean isCircular() {
		return circular;
	}

	@Override
	public void setRange(int value) {
		if (value != range) {
			int oldRange = range;
			this.range = Mth.clamp(value, 1, 16);
			onAreaChange(oldRange, this.circular);
			this.housing.markNetworkUpdate();
			WorldClimateHolder worldClimate = WorldClimateHolder.get((ServerLevel) getWorldObj());
			worldClimate.updateTransformer(this);
		}
	}

	private void onAreaChange(int range, boolean circular) {
		int prevArea = area;
		this.area = computeArea(range, circular);
		if (area != prevArea) {
			int areaDelta = Math.abs(area - prevArea);
			float speedDelta = calculateSpeedModifier(areaDelta);
			//IClimatised deltaState = actualState.subtract(originalState);
			//IClimatised scaledDelta = deltaState.multiply(area > prevArea ? (1.0F / speedDelta) : speedDelta);
			//setCurrent(scaledDelta.add(originalState));
		}
	}

	private static int computeArea(int range, boolean circular) {
		return circular ? Math.round((range + 0.5F) * (range + 0.5F) * 2.0F * (float) Math.PI) : (range * 2 + 1) * (range * 2 + 1);
	}

	@Override
	public float getAreaModifier() {
		return calculateAreaModifier(area);
	}

	@Override
	public float getCostModifier() {
		return 1.0F + (getAreaModifier() * Config.habitatformerAreaCostModifier);
	}

	@Override
	public float getSpeedModifier() {
		return calculateSpeedModifier(area);
	}

	private static float calculateSpeedModifier(float area) {
		return 1.0F + (calculateAreaModifier(area) * Config.habitatformerAreaSpeedModifier);
	}

	private static float calculateAreaModifier(float area) {
		return area / 36.0F;
	}

	@Override
	public int getArea() {
		return area;
	}

	@Override
	public int getRange() {
		return range;
	}

	@Override
	public BlockPos getCoordinates() {
		return housing.getCoordinates();
	}

	@Nullable
	@Override
	public Level getWorldObj() {
		return housing.getWorldObj();
	}
}
