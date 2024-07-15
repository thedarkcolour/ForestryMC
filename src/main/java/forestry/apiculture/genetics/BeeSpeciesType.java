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

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IBreedingTrackerHandler;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.gatgets.IDatabasePlugin;
import forestry.apiculture.BeeHousingListener;
import forestry.apiculture.BeeHousingModifier;
import forestry.apiculture.BeekeepingLogic;
import forestry.core.genetics.root.BreedingTrackerManager;

import forestry.api.genetics.IGenome;
import genetics.api.individual.IGenomeWrapper;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;
import genetics.api.root.IRootContext;
import genetics.api.root.SpeciesType;
import genetics.utils.AlleleUtils;

public class BeeSpeciesType extends SpeciesType<IBee> implements IBeeSpeciesType, IBreedingTrackerHandler {
	private int beeSpeciesCount = -1;

	public BeeSpeciesType(IRootContext<IBee> context) {
		super(context);
		BreedingTrackerManager.INSTANCE.registerTracker(ForestrySpeciesType.BEE, this);
	}

	@Override
	public Class<? extends IBee> getMemberClass() {
		return IBee.class;
	}

	@Override
	public int getSpeciesCount() {
		if (beeSpeciesCount < 0) {
			beeSpeciesCount = (int) AlleleUtils.filteredStream((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES)
					.filter(IAlleleBeeSpecies::isCounted).count();
		}

		return beeSpeciesCount;
	}


	@Override
	public BeeLifeStage getIconType() {
		return BeeLifeStage.DRONE;
	}

	@Override
	public ILifeStage getTypeForMutation(int position) {
		return switch (position) {
			case 0 -> BeeLifeStage.PRINCESS;
			case 1 -> BeeLifeStage.DRONE;
			case 2 -> BeeLifeStage.QUEEN;
			default -> getIconType();
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
	public IGenomeWrapper createWrapper(IGenome genome) {
		return () -> genome;
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
		return "ApiaristTracker." + (profile == null ? "common" : profile.getId());
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
	@OnlyIn(Dist.CLIENT)
	public IDatabasePlugin getSpeciesPlugin() {
		return BeePlugin.INSTANCE;
	}
}
