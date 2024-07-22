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
package forestry.apiculture.genetics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.IForestryApi;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutationManager;
import forestry.apiculture.ModuleApiculture;
import forestry.core.genetics.BreedingTracker;

public class ApiaristTracker extends BreedingTracker<IBeeSpecies> implements IApiaristTracker {
	private int queensTotal = 0;
	private int dronesTotal = 0;
	private int princessesTotal = 0;

	/**
	 * Required for creation from map storage
	 */
	public ApiaristTracker() {
		super(ModuleApiculture.beekeepingMode);
	}

	// todo get rid of this constructor and just call load
	public ApiaristTracker(CompoundTag tag) {
		super(ModuleApiculture.beekeepingMode, tag);
	}

	@Override
	protected void load(CompoundTag nbt) {
		super.load(nbt);

		this.queensTotal = nbt.getInt("QueensTotal");
		this.princessesTotal = nbt.getInt("PrincessesTotal");
		this.dronesTotal = nbt.getInt("DronesTotal");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		nbt.putInt("QueensTotal", this.queensTotal);
		nbt.putInt("PrincessesTotal", this.princessesTotal);
		nbt.putInt("DronesTotal", this.dronesTotal);

		nbt = super.save(nbt);
		return nbt;
	}

	@Override
	public void registerPickup(IBeeSpecies species) {
		IMutationManager manager = IForestryApi.INSTANCE.getGeneticManager().getMutations(ForestrySpeciesTypes.BEE);

		if (manager.getMutationsFrom(species).isEmpty()) {
			registerSpecies(species);
		}
	}

	@Override
	public void registerQueen(IBee bee) {
		queensTotal++;
	}

	@Override
	public int getQueenCount() {
		return queensTotal;
	}

	@Override
	public void registerPrincess(IBee bee) {
		princessesTotal++;
		registerBirth(bee);
	}

	@Override
	public int getPrincessCount() {
		return princessesTotal;
	}

	@Override
	public void registerDrone(IBee bee) {
		dronesTotal++;
		registerBirth(bee);
	}

	@Override
	public int getDroneCount() {
		return dronesTotal;
	}

	@Override
	protected IBreedingTracker getBreedingTracker(Player player) {
		return BeeManager.beeRoot.getBreedingTracker(player.level, player.getGameProfile());
	}

	@Override
	protected ResourceLocation getSpeciesId() {
		return ForestrySpeciesTypes.BEE;
	}
}
