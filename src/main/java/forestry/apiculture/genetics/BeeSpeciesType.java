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

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;

import forestry.api.IForestryApi;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.Product;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.gatgets.IDatabasePlugin;
import forestry.api.plugin.IForestryPlugin;
import forestry.api.plugin.ISpeciesTypeBuilder;
import forestry.apiculture.BeeHousingListener;
import forestry.apiculture.BeeHousingModifier;
import forestry.apiculture.BeekeepingLogic;
import forestry.apiimpl.ForestryApiImpl;
import forestry.core.genetics.BreedingTracker;
import forestry.core.genetics.SpeciesType;
import forestry.core.genetics.root.BreedingTrackerManager;
import forestry.core.utils.ItemStackUtil;
import forestry.apiimpl.plugin.ApicultureRegistration;

public class BeeSpeciesType extends SpeciesType<IBeeSpecies, IBee> implements IBeeSpeciesType {
	public BeeSpeciesType(IKaryotype karyotype, ISpeciesTypeBuilder builder) {
		super(ForestrySpeciesTypes.BEE, karyotype, builder);
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
	public IApiaristTracker getBreedingTracker(LevelAccessor level, @Nullable GameProfile profile) {
		return BreedingTrackerManager.INSTANCE.getTracker(this, level, profile);
	}

	@Override
	public String getBreedingTrackerFile(@Nullable GameProfile profile) {
		return "ApiaristTracker." + (profile == null ? "common" : profile.getId());
	}

	@Override
	public IBreedingTracker createBreedingTracker() {
		return new ApiaristTracker();
	}

	@Override
	public IBreedingTracker createBreedingTracker(CompoundTag tag) {
		return new ApiaristTracker(tag);
	}

	@Override
	public void initializeBreedingTracker(IBreedingTracker tracker, @Nullable Level world, @Nullable GameProfile profile) {
		if (tracker instanceof BreedingTracker apiaristTracker) {
			apiaristTracker.setLevel(world);
			apiaristTracker.setUsername(profile);
		}
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
	public IDatabasePlugin getDatabasePlugin() {
		return BeePlugin.INSTANCE;
	}

	@Override
	public Codec<? extends IBee> getIndividualCodec() {
		return Bee.CODEC;
	}

	@Override
	public float getResearchSuitability(IBeeSpecies species, ItemStack stack) {
		for (Product product : species.getProducts()) {
			if (stack.is(product.item())) {
				return 1.0f;
			}
		}
		for (Product product : species.getSpecialties()) {
			if (stack.is(product.item())) {
				return 1.0f;
			}
		}
		return super.getResearchSuitability(species, stack);
	}

	@Override
	public List<ItemStack> getResearchBounty(IBeeSpecies species, Level level, GameProfile researcher, IBee individual, int bountyLevel) {
		List<ItemStack> bounty = super.getResearchBounty(species, level, researcher, individual, bountyLevel);
		if (bountyLevel > 10) {
			for (Product stack : species.getSpecialties()) {
				bounty.add(ItemStackUtil.copyWithRandomSize(stack, (int) ((float) bountyLevel / 2), level.random));
			}
		}
		for (Product stack : species.getProducts()) {
			bounty.add(ItemStackUtil.copyWithRandomSize(stack, (int) ((float) bountyLevel / 2), level.random));
		}
		return bounty;
	}

	@Override
	public ImmutableMap<ResourceLocation, IBeeSpecies> handleSpeciesRegistration(List<IForestryPlugin> plugins) {
		ApicultureRegistration registration = new ApicultureRegistration(this);

		for (IForestryPlugin plugin : plugins) {
			plugin.registerApiculture(registration);
		}

		// populate bee registry chromosomes
		BeeChromosomes.EFFECT.populate(registration.getBeeEffects());
		BeeChromosomes.FLOWER_TYPE.populate(registration.getFlowerTypes());

		// initialize hive manager
		((ForestryApiImpl) IForestryApi.INSTANCE).setHiveManager(registration.buildHiveManager());

		return registration.buildSpecies();
	}
}
