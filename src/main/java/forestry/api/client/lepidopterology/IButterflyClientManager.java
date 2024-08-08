package forestry.api.client.lepidopterology;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import forestry.api.lepidopterology.genetics.IButterflySpecies;

public interface IButterflyClientManager {
	/**
	 * @return The butterfly item and entity textures, respectively.
	 */
	Pair<ResourceLocation, ResourceLocation> getTextures(IButterflySpecies species);
}
