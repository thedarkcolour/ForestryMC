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
package forestry.arboriculture.models;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.arboriculture.ILeafSprite;

public class LeafSprite implements ILeafSprite {
	private final ResourceLocation fast;
	private final ResourceLocation fancy;
	private final ResourceLocation pollinatedFast;
	private final ResourceLocation pollinatedFancy;

	public LeafSprite(ResourceLocation fast, ResourceLocation fancy, ResourceLocation pollinatedFast, ResourceLocation pollinatedFancy) {
		this.fast = fast;
		this.fancy = fancy;
		this.pollinatedFast = pollinatedFast;
		this.pollinatedFancy = pollinatedFancy;
	}

	public static LeafSprite create(ResourceLocation id) {
		String namespace = id.getNamespace();
		String path = "block/leaves/" + id.getPath();

		return new LeafSprite(
				new ResourceLocation(namespace, path + "_fast"),
				new ResourceLocation(namespace, path),
				new ResourceLocation(namespace, path + "_pollinated_fast"),
				new ResourceLocation(namespace, path + "_pollinated")
		);
	}

	@Override
	public ResourceLocation get(boolean pollinated, boolean fancy) {
		if (pollinated) {
			return fancy ? this.pollinatedFancy : this.pollinatedFast;
		} else {
			return fancy ? this.fancy : this.fast;
		}
	}

	@Override
	public ResourceLocation getParticle() {
		return this.fancy;
	}
}

