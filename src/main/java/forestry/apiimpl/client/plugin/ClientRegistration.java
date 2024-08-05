package forestry.apiimpl.client.plugin;

import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.client.plugin.IClientRegistration;

public class ClientRegistration implements IClientRegistration {
	// ID -> (block model, item model)
	private final HashMap<ResourceLocation, Pair<ResourceLocation, ResourceLocation>> saplingModels = new HashMap<>();
	// ID -> leaf sprite
	private final HashMap<ResourceLocation, ILeafSprite> leafSprites = new HashMap<>();

	@Override
	public void registerSaplingModel(ResourceLocation speciesId, ResourceLocation blockModel, ResourceLocation itemModel) {
		this.saplingModels.put(speciesId, Pair.of(blockModel, itemModel));
	}

	@Override
	public void registerLeafSprite(ResourceLocation speciesId, ILeafSprite sprite) {
		this.leafSprites.put(speciesId, sprite);
	}

	@Override
	public void registerButterflySprite() {

	}

	public HashMap<ResourceLocation, Pair<ResourceLocation, ResourceLocation>> getSaplingModels() {
		return this.saplingModels;
	}

	public HashMap<ResourceLocation, ILeafSprite> getLeafSprites() {
		return this.leafSprites;
	}
}
