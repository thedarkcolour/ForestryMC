package forestry.apiimpl.client;

import java.util.IdentityHashMap;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import forestry.api.client.lepidopterology.IButterflyClientManager;
import forestry.api.lepidopterology.genetics.IButterflySpecies;

public class ButterflyClientManager implements IButterflyClientManager {
	private final IdentityHashMap<IButterflySpecies, Pair<ResourceLocation, ResourceLocation>> textures;

	public ButterflyClientManager(IdentityHashMap<IButterflySpecies, Pair<ResourceLocation, ResourceLocation>> textures) {
		this.textures = textures;
	}

	@Override
	public Pair<ResourceLocation, ResourceLocation> getTextures(IButterflySpecies species) {
		return this.textures.get(species);
	}
}
