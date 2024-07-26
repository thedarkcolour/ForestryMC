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
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.ISpecies;
import forestry.core.genetics.BreedingTracker;
import forestry.core.utils.SpeciesUtil;

public class ApiaristTracker extends BreedingTracker implements IApiaristTracker {
	private int queensTotal = 0;
	private int dronesTotal = 0;
	private int princessesTotal = 0;

	/**
	 * Required for creation from map storage
	 */
	public ApiaristTracker() {
		super();
	}

	// todo get rid of this constructor and just call load
	public ApiaristTracker(CompoundTag tag) {
		super(tag);
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
	public void registerPickup(ISpecies<?> species) {
		IMutationManager<ISpecies<?>> manager = IForestryApi.INSTANCE.getGeneticManager().getMutations(ForestrySpeciesTypes.BEE);

		if (manager.getMutationsFrom(species).isEmpty()) {
			registerSpecies(species);
		}
	}

	@Override
	public void registerQueen(IBee bee) {
		this.queensTotal++;
		// no need to register because we should already have princess
	}

	@Override
	public int getQueenCount() {
		return this.queensTotal;
	}

	@Override
	public void registerPrincess(IBee bee) {
		this.princessesTotal++;
		registerBirth(bee.getSpecies());
	}

	@Override
	public int getPrincessCount() {
		return this.princessesTotal;
	}

	@Override
	public void registerDrone(IBee bee) {
		this.dronesTotal++;
		registerBirth(bee.getSpecies());
	}

	@Override
	public int getDroneCount() {
		return this.dronesTotal;
	}

	@Override
	protected IBreedingTracker getBreedingTracker(Player player) {
		return SpeciesUtil.BEE_TYPE.get().getBreedingTracker(player.level, player.getGameProfile());
	}

	@Override
	protected ResourceLocation getSpeciesId() {
		return ForestrySpeciesTypes.BEE;
	}
}
