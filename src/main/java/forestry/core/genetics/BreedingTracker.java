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
package forestry.core.genetics;

import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;

import forestry.api.IForestryApi;
import forestry.api.core.ForestryEvent;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.core.network.packets.PacketGenomeTrackerSync;
import forestry.core.utils.NetworkUtil;

public abstract class BreedingTracker extends SavedData implements IBreedingTracker {
	private static final String SPECIES_KEY = "SD";
	private static final String MUTATIONS_KEY = "MD";
	private static final String RESEARCHED_KEY = "RD";
	private static final String MUTATION_FORMAT = "%s-%s=%s";

	public static final String TYPE_KEY = "TYPE";

	private final Set<ResourceLocation> discoveredSpecies = new HashSet<>();
	private final Set<String> discoveredMutations = new HashSet<>();
	private final Set<String> researchedMutations = new HashSet<>();

	@Nullable
	private GameProfile username;
	@Nullable
	private Level level;

	protected BreedingTracker() {
	}

	protected BreedingTracker(CompoundTag nbt) {
		load(nbt);
	}

	protected void load(CompoundTag nbt) {
		readFromNbt(nbt);
	}

	public void setUsername(@Nullable GameProfile username) {
		this.username = username;
	}

	public void setLevel(@Nullable Level level) {
		this.level = level;
	}

	/**
	 * Returns the common tracker
	 *
	 * @param player used to get world
	 * @return common tracker for this breeding system
	 */
	protected abstract IBreedingTracker getBreedingTracker(Player player);

	/**
	 * Tag stored in NBT to identify the type of the tracker being synced
	 */
	protected abstract ResourceLocation getTypeId();

	// todo why are there two syncToPlayer methods here?
	@Override
	public void synchToPlayer(Player player) {
		if (player instanceof ServerPlayer && !(player instanceof FakePlayer)) {
			CompoundTag nbt = new CompoundTag();
			encodeToNBT(nbt);
			PacketGenomeTrackerSync packet = new PacketGenomeTrackerSync(nbt);
			NetworkUtil.sendToPlayer(packet, (ServerPlayer) player);
		}
	}

	private void syncToPlayer(Collection<ResourceLocation> discoveredSpecies, Collection<String> discoveredMutations, Collection<String> researchedMutations) {
		if (level != null && username != null && username.getName() != null) {
			Player player = level.getPlayerByUUID(username.getId());

			if (player instanceof ServerPlayer && !(player instanceof FakePlayer)) {
				CompoundTag nbt = new CompoundTag();
				writeToNBT(nbt, discoveredSpecies, discoveredMutations, researchedMutations);
				PacketGenomeTrackerSync packet = new PacketGenomeTrackerSync(nbt);
				NetworkUtil.sendToPlayer(packet, (ServerPlayer) player);
			}
		}
	}

	/* HELPER FUNCTIONS TO PREVENT OBFUSCATION OF INTERFACE METHODS */
	@Override
	public void decodeFromNBT(CompoundTag nbt) {
		readFromNbt(nbt);
	}

	@Override
	public void encodeToNBT(CompoundTag compound) {
		save(compound);
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		writeToNBT(nbt, discoveredSpecies, discoveredMutations, researchedMutations);
		return nbt;
	}

	private void writeToNBT(CompoundTag nbt, Collection<ResourceLocation> discoveredSpecies, Collection<String> discoveredMutations, Collection<String> researchedMutations) {
		nbt.putString(TYPE_KEY, getTypeId().toString());

		writeValuesToNBT(nbt, Iterables.transform(discoveredSpecies, ResourceLocation::toString), SPECIES_KEY);
		writeValuesToNBT(nbt, discoveredMutations, MUTATIONS_KEY);
		writeValuesToNBT(nbt, researchedMutations, RESEARCHED_KEY);
	}

	private static void writeValuesToNBT(CompoundTag nbt, Iterable<String> values, String key) {
		ListTag nbtList = new ListTag();
		for (String value : values) {
			nbtList.add(StringTag.valueOf(value));
		}
		nbt.put(key, nbtList);
	}

	private void readFromNbt(CompoundTag nbt) {
		readValuesFromNBT(nbt, value -> this.discoveredSpecies.add(new ResourceLocation(value)), SPECIES_KEY);
		readValuesFromNBT(nbt, this.discoveredMutations::add, MUTATIONS_KEY);
		readValuesFromNBT(nbt, this.researchedMutations::add, RESEARCHED_KEY);
	}

	private static void readValuesFromNBT(CompoundTag nbt, Consumer<String> values, String key) {
		if (nbt.contains(key)) {
			ListTag nbtList = nbt.getList(key, Tag.TAG_STRING);
			for (Tag stringTag : nbtList) {
				values.accept(stringTag.getAsString());
			}
		}
	}

	private static String getMutationString(IMutation<?> mutation) {
		String species0 = mutation.getFirstParent().id().toString();
		String species1 = mutation.getSecondParent().id().toString();
		String resultSpecies = mutation.getResult().id().toString();
		return String.format(MUTATION_FORMAT, species0, species1, resultSpecies);
	}

	@Override
	public void registerMutation(IMutation<?> mutation) {
		String mutationString = getMutationString(mutation);
		if (!discoveredMutations.contains(mutationString)) {
			discoveredMutations.add(mutationString);
			setDirty();

			ISpeciesType<?, ?> speciesRoot = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(getTypeId());
			ForestryEvent event = new ForestryEvent.MutationDiscovered(speciesRoot, username, mutation, this);
			MinecraftForge.EVENT_BUS.post(event);

			syncToPlayer(List.of(), List.of(mutationString), List.of());
		}
	}

	@Override
	public boolean isDiscovered(IMutation<?> mutation) {
		String mutationString = getMutationString(mutation);
		return discoveredMutations.contains(mutationString) || researchedMutations.contains(mutationString);
	}

	@Override
	public boolean isDiscovered(ISpecies<?> species) {
		return discoveredSpecies.contains(species.id());
	}

	@Override
	public Set<ResourceLocation> getDiscoveredSpecies() {
		return discoveredSpecies;
	}

	@Override
	public int getSpeciesBred() {
		return discoveredSpecies.size();
	}

	@Override
	public void registerBirth(ISpecies<?> species) {
		registerSpecies(species);
	}

	@Override
	public void registerSpecies(ISpecies<?> species) {
		ResourceLocation speciesId = species.id();

		if (!discoveredSpecies.contains(speciesId)) {
			discoveredSpecies.add(speciesId);

			ISpeciesType<?, ?> speciesType = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(getTypeId());
			ForestryEvent event = new ForestryEvent.SpeciesDiscovered(speciesType, this.username, species, this);
			MinecraftForge.EVENT_BUS.post(event);

			syncToPlayer(List.of(speciesId), List.of(), List.of());
		}
	}

	@Override
	public void researchMutation(IMutation<?> mutation) {
		String mutationString = getMutationString(mutation);
		if (!researchedMutations.contains(mutationString)) {
			researchedMutations.add(mutationString);
			setDirty();

			registerMutation(mutation);

			syncToPlayer(List.of(), List.of(), List.of(mutationString));
		}
	}

	@Override
	public boolean isResearched(IMutation<?> mutation) {
		String mutationString = getMutationString(mutation);
		return researchedMutations.contains(mutationString);
	}
}
