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
package forestry.core.network.packets;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.common.MinecraftForge;

import forestry.api.core.ForestryEvent;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesType;
import forestry.core.genetics.BreedingTracker;
import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IIndividual;

public record PacketGenomeTrackerSync(@Nullable CompoundTag nbt) implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.GENOME_TRACKER_UPDATE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeNbt(nbt);
	}

	public static PacketGenomeTrackerSync decode(FriendlyByteBuf buffer) {
		return new PacketGenomeTrackerSync(buffer.readNbt());
	}

	public static void handle(PacketGenomeTrackerSync msg, Player player) {
		if (msg.nbt != null) {
			String type = msg.nbt.getString(BreedingTracker.TYPE_KEY);
			ISpeciesType<IIndividual> root = GeneticsAPI.apiInstance.getRoot(type);

			if (root != null) {
				IBreedingTracker tracker = root.getBreedingTracker(player.getCommandSenderWorld(), player.getGameProfile());
				tracker.decodeFromNBT(msg.nbt);
				MinecraftForge.EVENT_BUS.post(new ForestryEvent.SyncedBreedingTracker(tracker, player));
			}
		}
	}
}
