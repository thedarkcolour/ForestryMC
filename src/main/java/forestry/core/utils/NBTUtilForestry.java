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
package forestry.core.utils;

import forestry.core.network.IStreamable;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class NBTUtilForestry {

	public static CompoundTag writeStreamableToNbt(IStreamable streamable, CompoundTag nbt) {
		FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
		streamable.writeData(data);

		byte[] bytes = new byte[data.readableBytes()];
		data.getBytes(0, bytes);
		nbt.putByteArray("dataBytes", bytes);
		return nbt;
	}

	public static void readStreamableFromNbt(IStreamable streamable, CompoundTag nbt) {
		if (nbt.contains("dataBytes")) {
			byte[] bytes = nbt.getByteArray("dataBytes");
			FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes));
			streamable.readData(data);
		}
	}
}
