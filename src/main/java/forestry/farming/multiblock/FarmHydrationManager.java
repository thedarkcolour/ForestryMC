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
package forestry.farming.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import forestry.api.core.HumidityType;
import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;
import forestry.api.core.TemperatureType;
import forestry.core.network.IStreamable;
import forestry.cultivation.IFarmHousingInternal;
import forestry.farming.gui.IFarmLedgerDelegate;

public class FarmHydrationManager implements IFarmLedgerDelegate, INbtWritable, INbtReadable, IStreamable {
	private static final int DELAY_HYDRATION = 100;
	private static final float RAINFALL_MODIFIER_MAX = 15f;
	private static final float RAINFALL_MODIFIER_MIN = 0.5f;

	private final IFarmHousingInternal housing;
	private int hydrationDelay = 0;
	private int ticksSinceRainfall = 0;

	public FarmHydrationManager(IFarmHousingInternal housing) {
		this.housing = housing;
	}

	public void updateServer() {
		Level world = housing.getWorldObj();
		BlockPos coordinates = housing.getTopCoord();
		if (world.isRainingAt(coordinates.above())) {
			if (hydrationDelay > 0) {
				hydrationDelay--;
			} else {
				ticksSinceRainfall = 0;
			}
		} else {
			hydrationDelay = DELAY_HYDRATION;
			if (ticksSinceRainfall < Integer.MAX_VALUE) {
				ticksSinceRainfall++;
			}
		}
	}

	@Override
	public float getHydrationModifier() {
		return getHydrationTempModifier() * getHydrationHumidModifier() * getHydrationRainfallModifier();
	}

	@Override
	public float getHydrationTempModifier() {
		return switch (temperature()) {
			case NORMAL -> 1.0f;
			case WARM -> 1.5f;
			case HOT, HELLISH -> 2.0f;
			default -> 0.8f;
		};
	}

	@Override
	public float getHydrationHumidModifier() {
		return switch (humidity()) {
			case ARID -> 2.0f;
			case NORMAL -> 1.5f;
			case DAMP -> 1.0f;
		};
	}

	@Override
	public TemperatureType temperature() {
		return this.housing.temperature();
	}

	@Override
	public HumidityType humidity() {
		return this.housing.humidity();
	}

	@Override
	public float getHydrationRainfallModifier() {
		return Mth.clamp(RAINFALL_MODIFIER_MIN, (float) ticksSinceRainfall / 24000, RAINFALL_MODIFIER_MAX);
	}

	@Override
	public double getDrought() {
		return Math.round((double) ticksSinceRainfall / 24000 * 10) / 10.;
	}

	@Override
	public CompoundTag write(CompoundTag compoundNBT) {
		compoundNBT.putInt("HydrationDelay", hydrationDelay);
		compoundNBT.putInt("TicksSinceRainfall", ticksSinceRainfall);
		return compoundNBT;
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		data.writeVarInt(hydrationDelay);
		data.writeVarInt(ticksSinceRainfall);
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		hydrationDelay = data.readVarInt();
		ticksSinceRainfall = data.readVarInt();
	}

	@Override
	public void read(CompoundTag nbt) {
		hydrationDelay = nbt.getInt("HydrationDelay");
		ticksSinceRainfall = nbt.getInt("TicksSinceRainfall");
	}
}
