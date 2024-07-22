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

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;

import forestry.api.ForestryConstants;
import forestry.api.arboriculture.LeafType;

public record LeafTexture(ResourceLocation fast, ResourceLocation fancy, ResourceLocation pollinatedFast, ResourceLocation pollinatedFancy) {
	private static final EnumMap<LeafType, LeafTexture> leafTextures = new EnumMap<>(LeafType.class);

	static {
		for (LeafType leafType : LeafType.values()) {
			leafTextures.put(leafType, LeafTexture.create(leafType));
		}
	}

	public static LeafTexture get(LeafType leafType) {
		return leafTextures.get(leafType);
	}

	public static void registerAllSprites(TextureStitchEvent.Pre event) {
		for (LeafTexture leafTexture : leafTextures.values()) {
			leafTexture.registerSprites(event);
		}
	}

	private static LeafTexture create(LeafType leafType) {
		String id = leafType.toString().toLowerCase(Locale.ENGLISH);

		return new LeafTexture(
				ForestryConstants.forestry("block/leaves/" + id + "_fast"),
				ForestryConstants.forestry("block/leaves/" + id),
				ForestryConstants.forestry("block/leaves/" + id + "_pollinated_fast"),
				ForestryConstants.forestry("block/leaves/" + id + "_pollinated")
		);
	}

	private void registerSprites(TextureStitchEvent.Pre event) {
		event.addSprite(fast);
		event.addSprite(fancy);
		event.addSprite(pollinatedFast);
		event.addSprite(pollinatedFancy);
	}

	public ResourceLocation getSprite(boolean pollinated, boolean fancy) {
		if (pollinated) {
			return fancy ? this.pollinatedFancy : this.pollinatedFast;
		} else {
			return fancy ? this.fancy : this.fast;
		}
	}
}