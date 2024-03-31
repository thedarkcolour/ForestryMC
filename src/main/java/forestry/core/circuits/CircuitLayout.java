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


import forestry.api.circuits.ICircuitLayout;
import forestry.api.circuits.ICircuitSocketType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CircuitLayout implements ICircuitLayout {
	private final String uid;
	private final ICircuitSocketType socketType;

	public CircuitLayout(String uid, ICircuitSocketType socketType) {
		this.uid = uid;
		this.socketType = socketType;
	}

	@Override
	public String getUID() {
		return "forestry." + this.uid;
	}

	@Override
	public Component getName() {
		return Component.translatable("for.circuit.layout." + this.uid);
	}

	@Override
	public MutableComponent getUsage() {
		return Component.translatable("for.circuit.layout." + this.uid + ".usage");
	}

	@Override
	public ICircuitSocketType getSocketType() {
		return socketType;
	}
}
