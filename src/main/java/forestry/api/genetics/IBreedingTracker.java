/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.genetics.alleles.IAllele;

/**
 * Keeps track of who bred, discovered, and researched which species in a world.
 *
 * @author SirSengir
 */
public interface IBreedingTracker {
	/**
	 * @return Amount of species discovered.
	 */
	int getSpeciesBred();

	/**
	 * Register the birth of a species. Will mark it as discovered.
	 */
	void registerBirth(ISpecies<?> species);

	/**
	 * Register the pickup of a species.
	 */
	void registerPickup(ISpecies<?> species);

	/**
	 * Marks a species as discovered. Should only be called from registerIndividual normally.
	 */
	void registerSpecies(ISpecies<?> species);

	/**
	 * Register a successful mutation. Will mark it as discovered.
	 */
	void registerMutation(IMutation<?> mutation);

	/**
	 * Queries the tracker for discovered species.
	 *
	 * @param mutation Mutation to query for.
	 * @return true if the mutation has been discovered.
	 */
	boolean isDiscovered(IMutation<?> mutation);

	/**
	 * Queries the tracker for discovered species.
	 *
	 * @param species Species to check.
	 * @return true if the species has been bred.
	 */
	boolean isDiscovered(ISpecies<?> species);

	/**
	 * @return A collection that contains the {@link IAllele#alleleId()}s of all discovered species.
	 */
	default Collection<String> getDiscoveredSpecies() {
		return Collections.emptyList();
	}

	/**
	 * Register a successfully researched mutation.
	 * Mutations are normally researched in the Escritoire.
	 * Researched mutations may have bonuses such as occurring at a higher rate.
	 * Researched mutations count as discovered.
	 */
	void researchMutation(IMutation<?> mutation);

	/**
	 * @return true if the mutation has been researched.
	 */
	boolean isResearched(IMutation<?> mutation);

	/**
	 * Synchronizes the tracker to the client side.
	 * Before Forestry 4.2.1: Should be called before opening any gui needing that information.
	 * Since Forestry 4.2.1: Breeding tracker should be automatically synced, only Forestry should need to call this.
	 */
	void synchToPlayer(Player player);

	/* LOADING & SAVING */
	void decodeFromNBT(CompoundTag compound);

	void encodeToNBT(CompoundTag compound);

}
