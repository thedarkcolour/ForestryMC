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
package forestry.core.circuits;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import forestry.api.circuits.ICircuitLayout;

// todo fix translations
public class CircuitLayout implements ICircuitLayout {
	private final String uid;
	private final ResourceLocation socketType;

	public CircuitLayout(String uid, ResourceLocation socketType) {
		this.uid = uid;
		this.socketType = socketType;
	}

	@Override
	public String getId() {
		return this.uid;
	}

	@Override
	public Component getName() {
		return Component.translatable("circuit.layout." + this.uid);
	}

	@Override
	public MutableComponent getUsage() {
		return Component.translatable("circuit.layout." + this.uid + ".usage");
	}

	@Override
	public ResourceLocation getSocketType() {
		return this.socketType;
	}
}
