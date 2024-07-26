/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import net.minecraft.world.entity.player.Player;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.eventbus.api.Event;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

// TODO add documentation
public abstract class ForestryEvent extends Event {
	private static abstract class BreedingEvent extends ForestryEvent {
		public final ISpeciesType<?, ?> root;
		public final IBreedingTracker tracker;
		public final GameProfile username;

		private BreedingEvent(ISpeciesType<?, ?> root, GameProfile username, IBreedingTracker tracker) {
			this.root = root;
			this.username = username;
			this.tracker = tracker;
		}
	}

	public static class SpeciesDiscovered extends BreedingEvent {
		public final ISpecies<?> species;

		public SpeciesDiscovered(ISpeciesType<?, ?> root, GameProfile username, ISpecies<?> species, IBreedingTracker tracker) {
			super(root, username, tracker);
			this.species = species;
		}
	}

	public static class MutationDiscovered extends BreedingEvent {
		public final IMutation<?> allele;

		public MutationDiscovered(ISpeciesType<?, ?> root, GameProfile username, IMutation<?> allele, IBreedingTracker tracker) {
			super(root, username, tracker);
			this.allele = allele;
		}
	}

	public static class SyncedBreedingTracker extends ForestryEvent {
		public final IBreedingTracker tracker;
		public final Player player;

		public SyncedBreedingTracker(IBreedingTracker tracker, Player player) {
			this.tracker = tracker;
			this.player = player;
		}
	}
}
