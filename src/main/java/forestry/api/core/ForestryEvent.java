/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
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

	/**
	 * Fired when a {@linkplain forestry.api.genetics.IPollinatable pollinatable}, such as leaves, is about to be
	 * pollinated.
	 *
	 * <p>This event allows for mods to react to pollinations, change the genomes used for pollination, and
	 * to cancel pollinations entirely. Event listeners that cancel a pollination should use a higher
	 * {@linkplain EventPriority event priority} so that lower priority listeners can be aware that a pollination was
	 * canceled before reacting to it.</p>
	 *
	 * <p>This event is {@linkplain Cancelable cancelable}, and does not {@linkplain HasResult have a result}.
	 * If the event is canceled, then the individual will not be pollinated.</p>
	 *
	 * <p>This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus} on both the logical
	 * client and the logical server.</p>
	 */
	@Cancelable
	public static class PollinateIndividual extends ForestryEvent {
		private final Level level;
		private final BlockPos pos;
		private final IIndividual individual;
		private final IIndividual pollen;
		@Nullable
		private final Object pollinator;

		private IGenome pollenGenome;

		public PollinateIndividual(Level level, BlockPos pos, IIndividual individual, IIndividual pollen, @Nullable Object pollinator) {
			this.level = level;
			this.pos = pos;
			this.individual = individual;
			this.pollen = pollen;
			this.pollinator = pollinator;
			this.pollenGenome = pollen.getGenome();
		}

		/**
		 * @return The level where the pollination will occur.
		 */
		public Level getLevel() {
			return this.level;
		}

		/**
		 * @return The position of the individual to be pollinated (ex. leaves block).
		 */
		public BlockPos getPos() {
			return this.pos;
		}

		/**
		 * @return The individual (ex. leaves block) to be pollinated.
		 */
		public IIndividual getIndividual() {
			return this.individual;
		}

		/**
		 * @return The pollen used to pollinate the individual.
		 */
		public IIndividual getPollen() {
			return this.pollen;
		}

		/**
		 * @return The individual (for bees and butterflies) or entity (for players) causing the pollination. May be {@code null}.
		 */
		@Nullable
		public Object getPollinator() {
			return this.pollinator;
		}

		/**
		 * @return The genome to pollinate with. Starts as the genome of the pollen, but can be modified in {@link #setPollenGenome}.
		 */
		public IGenome getPollenGenome() {
			return this.pollenGenome;
		}

		/**
		 * Set the genome used to pollinate this individual.
		 *
		 * @param pollenGenome The genome that the individual should be pollinated with.
		 */
		public void setPollenGenome(IGenome pollenGenome) {
			this.pollenGenome = pollenGenome;
		}
	}
}
