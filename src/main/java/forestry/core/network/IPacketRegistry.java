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
package forestry.core.network;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IPacketRegistry {
	/**
	 * Register a packet that is handled on the main server thread when the sender is not null.
	 */
	<P extends IForestryPacketServer> void serverbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> packetHandler);

	<P extends IForestryPacketClient> void clientbound(ResourceLocation id, Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, Player> packetHandler);
}
