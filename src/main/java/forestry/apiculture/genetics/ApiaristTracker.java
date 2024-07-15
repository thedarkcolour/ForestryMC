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

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.apiculture.ModuleApiculture;
import forestry.core.genetics.BreedingTracker;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.IMutation;
import genetics.api.mutation.IMutationContainer;
import genetics.api.root.components.ComponentKeys;

public class ApiaristTracker extends BreedingTracker implements IApiaristTracker {
	private int queensTotal = 0;
	private int dronesTotal = 0;
	private int princessesTotal = 0;

	/**
	 * Required for creation from map storage
	 */
	public ApiaristTracker() {
		super(ModuleApiculture.beekeepingMode);
	}

	public ApiaristTracker(CompoundTag tag) {
		super(ModuleApiculture.beekeepingMode, tag);

		queensTotal = tag.getInt("QueensTotal");
		princessesTotal = tag.getInt("PrincessesTotal");
		dronesTotal = tag.getInt("DronesTotal");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		nbt.putInt("QueensTotal", queensTotal);
		nbt.putInt("PrincessesTotal", princessesTotal);
		nbt.putInt("DronesTotal", dronesTotal);

		nbt = super.save(nbt);
		return nbt;
	}

	@Override
	public void registerPickup(IIndividual individual) {
		IBeeSpeciesType speciesRoot = (IBeeSpeciesType) individual.getRoot();
		if (!speciesRoot.id().equals(getSpeciesId())) {
			return;
		}

		if (!individual.isPureBred(BeeChromosomes.SPECIES)) {
			return;
		}

		IMutationContainer<IBee, ? extends IMutation> container = speciesRoot.getComponent(ComponentKeys.MUTATIONS);
		if (!container.getCombinations(individual.getGenome().getPrimarySpecies()).isEmpty()) {
			return;
		}

		registerSpecies(individual.getGenome().getPrimarySpecies());
	}

	@Override
	public void registerQueen(IIndividual bee) {
		queensTotal++;
	}

	@Override
	public int getQueenCount() {
		return queensTotal;
	}

	@Override
	public void registerPrincess(IIndividual bee) {
		princessesTotal++;
		registerBirth(bee);
	}

	@Override
	public int getPrincessCount() {
		return princessesTotal;
	}

	@Override
	public void registerDrone(IIndividual bee) {
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
		return ForestrySpeciesType.BEE;
	}
}
