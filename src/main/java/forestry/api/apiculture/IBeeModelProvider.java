/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture;

import net.minecraft.client.resources.model.ModelResourceLocation;

import forestry.api.apiculture.genetics.BeeLifeStage;

public interface IBeeModelProvider {

	ModelResourceLocation getModel(BeeLifeStage type);
}
