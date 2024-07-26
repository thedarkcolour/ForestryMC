package forestry.arboriculture.models;

import forestry.arboriculture.ForestryWoodType;
import forestry.arboriculture.VanillaWoodType;
import forestry.api.arboriculture.IGermlingModelProvider;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.IWoodType;

public class ModelProviderFactory {
	public static IGermlingModelProvider create(IWoodType woodType, String modelUid, ILeafSpriteProvider leafSpriteProvider) {
		if (woodType instanceof VanillaWoodType) {
			return new ModelProviderGermlingVanilla((VanillaWoodType) woodType, leafSpriteProvider);
		} else if (woodType instanceof ForestryWoodType) {
			return new ModelProviderGermling(modelUid, leafSpriteProvider);
		} else {
			throw new IllegalArgumentException("Unknown wood type: " + woodType);
		}
	}
}
