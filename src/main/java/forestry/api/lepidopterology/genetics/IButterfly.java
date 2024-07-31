/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import forestry.api.core.IError;
import forestry.api.genetics.IIndividualLiving;
import forestry.api.lepidopterology.IButterflyCocoon;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.IEntityButterfly;

public interface IButterfly extends IIndividualLiving {
	/**
	 * @return true if the butterfly can naturally spawn at the given location at this time. (Used to auto-spawn butterflies from tree leaves.)
	 */
	boolean canSpawn(Level level, double x, double y, double z);

	/**
	 * @return true if the butterfly can take flight at the given location at this time. (Used to auto-spawn butterflies from dropped items.)
	 */
	boolean canTakeFlight(Level level, double x, double y, double z);

	default boolean isAcceptedEnvironment(Level level, double x, double y, double z) {
		return isAcceptedEnvironment(level, new BlockPos(x, y, z));
	}

	boolean isAcceptedEnvironment(Level world, BlockPos pos);

	/**
	 * @return create a caterpillar with the two genome's from the nursery.
	 */
	@Nullable
	IButterfly spawnCaterpillar(IButterflyNursery nursery);

	/**
	 * Determines whether the caterpillar can grow.
	 *
	 * @param cocoon  the {@link IButterflyCocoon} the caterpillar resides in.
	 * @param nursery the {@link IButterflyNursery} of the caterpillar.
	 * @return an empty set if the caterpillar can grow, a set of error states if the caterpillar can not grow
	 * @since 5.3.3
	 */
	Set<IError> getCanGrow(IButterflyNursery nursery, @Nullable IButterflyCocoon cocoon);

	/**
	 * Determines whether the caterpillar can spawn. (Used to auto-spawn butterflies out of a cocoon.)
	 *
	 * @param cocoon  the {@link IButterflyCocoon} the caterpillar resides in.
	 * @param nursery the {@link IButterflyNursery} of the caterpillar.
	 * @return an empty set if the caterpillar can spawn, a set of error states if the caterpillar can not spawn
	 * @since 5.3.3
	 */
	Set<IError> getCanSpawn(IButterflyNursery nursery, @Nullable IButterflyCocoon cocoon);

	/**
	 * @param playerKill Whether or not the butterfly was killed by a player.
	 * @param lootLevel  Loot level according to the weapon used to kill the butterfly.
	 * @return Items to drop on death of the given entity.
	 */
	List<ItemStack> getLootDrop(IEntityButterfly entity, boolean playerKill, int lootLevel);

	/**
	 * @param playerKill Whether or not the nursery was broken by a player.
	 * @param lootLevel  Fortune level.
	 * @return Items to drop on breaking of the nursery.
	 */
	List<ItemStack> getCaterpillarDrop(IButterflyNursery nursery, boolean playerKill, int lootLevel);

	/**
	 * @param includeButterfly Whether the butterfly should drop from the cocoon. Used by worldgen cocoons.
	 * @return Items to drop on breaking of the cocoon.
	 */
	List<ItemStack> getCocoonDrop(boolean includeButterfly, IButterflyCocoon cocoon);

	@Override
	IButterfly copy();

	@Override
	IButterflySpeciesType getType();

	@Override
	IButterflySpecies getSpecies();

	Component getDisplayName();
}
