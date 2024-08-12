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
package forestry.farming.circuits;

import javax.annotation.Nullable;

import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.util.Lazy;

import forestry.api.IForestryApi;
import forestry.api.farming.HorizontalDirection;
import forestry.api.farming.IFarmCircuit;
import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmType;
import forestry.core.circuits.Circuit;

public class CircuitFarmLogic extends Circuit implements IFarmCircuit {
	private final Supplier<IFarmType> farmType;
	private final boolean manual;

	public CircuitFarmLogic(String uid, ResourceLocation farmTypeId, boolean manual) {
		super(uid);
		this.farmType = Lazy.of(() -> IForestryApi.INSTANCE.getFarmingManager().getFarmType(farmTypeId));
		this.manual = manual;
	}

	@Override
	public String getTranslationKey() {
		return this.farmType.get().getTranslationKey();
	}

	@Override
	public Component getDisplayName() {
		return this.farmType.get().getDisplayName(this.manual);
	}

	@Override
	public IFarmType getProperties() {
		return this.farmType.get();
	}

	@Override
	public boolean isManual() {
		return this.manual;
	}

	@Override
	public boolean isCircuitable(Object tile) {
		return tile instanceof IFarmHousing;
	}

	@Nullable
	private IFarmHousing getCircuitable(Object tile) {
		if (!isCircuitable(tile)) {
			return null;
		}
		return (IFarmHousing) tile;
	}

	@Override
	public void onInsertion(int slot, Object tile) {
		IFarmHousing housing = getCircuitable(tile);
		if (housing == null) {
			return;
		}

		housing.setFarmLogic(HorizontalDirection.VALUES.get(slot), this.farmType.get().getLogic(this.manual));
	}

	@Override
	public void onLoad(int slot, Object tile) {
		onInsertion(slot, tile);
	}

	@Override
	public void onRemoval(int slot, Object tile) {
		IFarmHousing farmHousing = getCircuitable(tile);
		if (farmHousing == null) {
			return;
		}

		farmHousing.resetFarmLogic(HorizontalDirection.VALUES.get(slot));
	}

	@Override
	public void onTick(int slot, Object tile) {
	}
}
