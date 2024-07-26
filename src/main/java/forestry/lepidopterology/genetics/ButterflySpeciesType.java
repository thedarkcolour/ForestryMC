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
package forestry.lepidopterology.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;

import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IAlyzerPlugin;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.gatgets.IDatabasePlugin;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.ILepidopteristTracker;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.api.plugin.ISpeciesTypeBuilder;
import forestry.core.genetics.SpeciesType;
import forestry.core.genetics.root.BreedingTrackerManager;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.EntityUtil;
import forestry.core.utils.GeneticsUtil;
import forestry.lepidopterology.blocks.BlockCocoon;
import forestry.lepidopterology.entities.EntityButterfly;
import forestry.lepidopterology.features.LepidopterologyBlocks;
import forestry.lepidopterology.features.LepidopterologyEntities;
import forestry.lepidopterology.tiles.TileCocoon;

public class ButterflySpeciesType extends SpeciesType<IButterflySpecies, IButterfly> implements IButterflySpeciesType {
	public ButterflySpeciesType(IKaryotype karyotype, ISpeciesTypeBuilder builder) {
		super(ForestrySpeciesTypes.BUTTERFLY, karyotype, builder);
	}

	@Override
	public ButterflyLifeStage getDefaultStage() {
		return ButterflyLifeStage.BUTTERFLY;
	}

	@Override
	public boolean isMember(IIndividual individual) {
		return individual instanceof IButterfly;
	}

	@Override
	public EntityButterfly spawnButterflyInWorld(Level level, IButterfly butterfly, double x, double y, double z) {
		return EntityUtil.spawnEntity(level, EntityButterfly.create(LepidopterologyEntities.BUTTERFLY.entityType(), level, butterfly, new BlockPos(x, y, z)), x, y, z);
	}

	@Override
	public BlockPos plantCocoon(LevelAccessor level, BlockPos coordinates, @Nullable IButterfly caterpillar, GameProfile owner, int age, boolean createNursery) {
		if (caterpillar == null) {
			return BlockPos.ZERO;
		}

		BlockPos pos = getValidCocoonPos(level, coordinates, caterpillar, owner, createNursery);
		if (pos == BlockPos.ZERO) {
			return pos;
		}
		// todo replace with cocoon allele
		BlockState state = LepidopterologyBlocks.COCOON.defaultState().setValue(BlockCocoon.AGE, age);
		boolean placed = level.setBlock(pos, state, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
		if (!placed) {
			return BlockPos.ZERO;
		}

		Block block = level.getBlockState(pos).getBlock();
		if (!LepidopterologyBlocks.COCOON.blockEqual(block)) {
			return BlockPos.ZERO;
		}

		TileCocoon cocoon = TileUtil.getTile(level, pos, TileCocoon.class);
		if (cocoon == null) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
			return BlockPos.ZERO;
		}

		cocoon.setCaterpillar(caterpillar);
		cocoon.getOwnerHandler().setOwner(owner);

		return pos;
	}

	private BlockPos getValidCocoonPos(LevelAccessor world, BlockPos pos, IButterfly caterpillar, GameProfile gameProfile, boolean createNursery) {
		if (isPositionValid(world, pos.below(), caterpillar, gameProfile, createNursery)) {
			return pos.below();
		}
		for (int tries = 0; tries < 3; tries++) {
			for (int y = 1; y < world.getRandom().nextInt(5); y++) {
				BlockPos coordinate = pos.offset(world.getRandom().nextInt(6) - 3, -y, world.getRandom().nextInt(6) - 3);
				if (isPositionValid(world, coordinate, caterpillar, gameProfile, createNursery)) {
					return coordinate;
				}
			}
		}

		return BlockPos.ZERO;
	}

	public boolean isPositionValid(LevelAccessor world, BlockPos pos, IButterfly caterpillar, GameProfile gameProfile, boolean createNursery) {
		BlockState blockState = world.getBlockState(pos);
		if (BlockUtil.canReplace(blockState, world, pos)) {
			BlockPos nurseryPos = pos.above();
			IButterflyNursery nursery = GeneticsUtil.getNursery(world, nurseryPos);
			if (isNurseryValid(nursery, caterpillar, gameProfile)) {
				return true;
			} else if (createNursery && GeneticsUtil.canCreateNursery(world, nurseryPos)) {
				nursery = GeneticsUtil.getOrCreateNursery(gameProfile, world, nurseryPos, false);
				return isNurseryValid(nursery, caterpillar, gameProfile);
			}
		}
		return false;
	}

	private boolean isNurseryValid(@Nullable IButterflyNursery nursery, IButterfly caterpillar, GameProfile gameProfile) {
		return nursery != null && nursery.canNurse(caterpillar);
	}

	@Override
	public boolean isMated(ItemStack stack) {
		return IIndividualHandler.filter(stack, individual -> individual instanceof IButterfly butterfly && butterfly.getMate() != null);
	}

	/* BREEDING TRACKER */
	@Override
	public ILepidopteristTracker getBreedingTracker(LevelAccessor level, @Nullable GameProfile profile) {
		return BreedingTrackerManager.INSTANCE.getTracker(id(), level, profile);
	}

	@Override
	public String getFileName(@Nullable GameProfile profile) {
		return "LepidopteristTracker." + (profile == null ? "common" : profile.getId());
	}

	@Override
	public IBreedingTracker createTracker() {
		return new LepidopteristTracker();
	}

	@Override
	public IBreedingTracker createTracker(CompoundTag tag) {
		return new LepidopteristTracker(tag);
	}

	@Override
	public void populateTracker(IBreedingTracker tracker, @Nullable Level world, @Nullable GameProfile profile) {
		if (!(tracker instanceof LepidopteristTracker arboristTracker)) {
			return;
		}
		arboristTracker.setLevel(world);
		arboristTracker.setUsername(profile);
	}

	@Override
	public IAlyzerPlugin getAlyzerPlugin() {
		return ButterflyAlyzerPlugin.INSTANCE;
	}

	@Override
	public IDatabasePlugin getDatabasePlugin() {
		return ButterflyPlugin.INSTANCE;
	}

	@Override
	public float getResearchSuitability(IButterflySpecies species, ItemStack stack) {
		for (Product product : species.getButterflyLoot()) {
			if (stack.is(product.item())) {
				return 1.0f;
			}
		}
		for (Product product : species.getCaterpillarLoot()) {
			if (stack.is(product.item())) {
				return 1.0f;
			}
		}
		return super.getResearchSuitability(species, stack);
	}

	@Override
	public List<ItemStack> getResearchBounty(IButterflySpecies species, Level level, GameProfile researcher, IButterfly individual, int bountyLevel) {
		return List.of(individual.copyWithStage(ButterflyLifeStage.SERUM));
	}

	@Override
	public Codec<? extends IButterfly> getIndividualCodec() {
		return Butterfly.CODEC;
	}
}
