/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.client;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

public interface ISpriteRegister {
	void registerSprites(Consumer<ResourceLocation> registry);
}
