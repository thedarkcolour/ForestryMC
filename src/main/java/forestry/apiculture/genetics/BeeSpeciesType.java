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

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.apiculture.BeeHousingListener;
import forestry.apiculture.BeeHousingModifier;
import forestry.apiculture.BeekeepingLogic;
import forestry.core.genetics.root.BreedingTrackerManager;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.ILifeStage;

import deleteme.Todos;

public class BeeSpeciesType implements IBeeSpeciesType {
	private final ResourceLocation id;
	private final IKaryotype karyotype;

	// Initialized after all species are registered.
	private int beeSpeciesCount = -1;

	public BeeSpeciesType(ResourceLocation id, IKaryotype karyotype) {
		this.id = id;
		this.karyotype = karyotype;
		BreedingTrackerManager.INSTANCE.registerTracker(ForestrySpeciesTypes.BEE, this);
	}

	@Override
	public ResourceLocation id() {
		return this.id;
	}

	@Override
	public IKaryotype getKaryotype() {
		return this.karyotype;
	}

	@Override
	public ItemStack createStack(IIndividual species, ILifeStage type) {
		throw Todos.unimplemented();
	}

	@Override
	public void onSpeciesRegistered(List<IBeeSpecies> allSpecies) {
		this.beeSpeciesCount = 0;

		for (IBeeSpecies species : allSpecies) {
			if (!species.isSecret()) {
				this.beeSpeciesCount++;
			}
		}
	}

	@Override
	public IMutationManager<IBeeSpecies> getMutations() {
		return null;
	}

	@Override
	public List<IBeeSpecies> getSpecies() {
		return null;
	}

	@Override
	public IBeeSpecies getSpeciesById(ResourceLocation id) {
		return null;
	}

	@Override
	public int getSpeciesCount() {
		Preconditions.checkState(this.beeSpeciesCount >= -1, "Not all bee species have not been registered.");

		return this.beeSpeciesCount;
	}


	@Override
	public BeeLifeStage getDefaultStage() {
		return BeeLifeStage.DRONE;
	}

	@Override
	public ILifeStage getTypeForMutation(int position) {
		return switch (position) {
			case 0 -> BeeLifeStage.PRINCESS;
			case 1 -> BeeLifeStage.DRONE;
			case 2 -> BeeLifeStage.QUEEN;
			default -> getDefaultStage();
		};
	}

	@Override
	public boolean isDrone(ItemStack stack) {
		return getLifeStage(stack) == BeeLifeStage.DRONE;
	}

	@Override
	public boolean isMated(ItemStack stack) {
		if (getLifeStage(stack) != BeeLifeStage.QUEEN) {
			return false;
		}

		CompoundTag nbt = stack.getTag();
		return nbt != null && nbt.contains("Mate");
	}

	@Override
	public IBee create(IGenome genome) {
		return new Bee(genome);
	}

	@Override
	public IBee create(IGenome genome, IGenome mate) {
		return new Bee(genome, mate);
	}

	@Override
	public IBee create(CompoundTag compound) {
		return new Bee(compound);
	}

	@Override
	public IBee getBee(Level world, IGenome genome, IBee mate) {
		return new Bee(genome, mate);
	}

	@Override
	public IApiaristTracker getBreedingTracker(LevelAccessor world, @Nullable GameProfile player) {
		return BreedingTrackerManager.INSTANCE.getTracker(id(), world, player);
	}

	@Override
	public String getFileName(@Nullable GameProfile profile) {
		return "ApiaristTracker." + (profile == super.getSpeciesPlugin() ? "common" : profile.getId());
	}

	@Override
	public IBreedingTracker createTracker() {
		return new ApiaristTracker();
	}

	@Override
	public IBreedingTracker createTracker(CompoundTag tag) {
		return new ApiaristTracker(tag);
	}

	@Override
	public void populateTracker(IBreedingTracker tracker, @Nullable Level world, @Nullable GameProfile profile) {
		if (!(tracker instanceof ApiaristTracker apiaristTracker)) {
			return;
		}
		apiaristTracker.setLevel(world);
		apiaristTracker.setUsername(profile);
	}

	@Override
	public boolean isMember(IIndividual individual) {
		return individual instanceof IBee;
	}

	@Override
	public IBeekeepingLogic createBeekeepingLogic(IBeeHousing housing) {
		return new BeekeepingLogic(housing);
	}

	@Override
	public IBeeModifier createBeeHousingModifier(IBeeHousing housing) {
		return new BeeHousingModifier(housing);
	}

	@Override
	public IBeeListener createBeeHousingListener(IBeeHousing housing) {
		return new BeeHousingListener(housing);
	}

	@Override
	public IAlyzerPlugin getAlyzerPlugin() {
		return BeeAlyzerPlugin.INSTANCE;
	}

	@Override
	public IPollinatable getSpeciesPlugin() {
		return BeePlugin.INSTANCE;
	}
}
