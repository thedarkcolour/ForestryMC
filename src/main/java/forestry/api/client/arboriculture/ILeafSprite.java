/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.client.arboriculture;

import net.minecraft.resources.ResourceLocation;

/**
 * Provides textures used for rendering leaves on a Forestry tree.
 */
public interface ILeafSprite {
	/**
	 * Returns the location of the leaf texture sprite to use for rendering.
	 *
	 * @param pollinated Whether the leaves are pollinated.
	 * @param fancy      Whether the game is using fancy graphics. If fast, replace transparent pixels with black.
	 * @return The location of the sprite to use for leaf block rendering.
	 */
	ResourceLocation get(boolean pollinated, boolean fancy);

	/**
	 * @return The leaf texture used for block particles (walking, destroying, etc.)
	 */
	ResourceLocation getParticle();
}
