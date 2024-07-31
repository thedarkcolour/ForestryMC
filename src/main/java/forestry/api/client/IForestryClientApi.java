package forestry.api.client;

import java.util.Map;
import java.util.ServiceLoader;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import forestry.api.arboriculture.ITreeSpecies;

public interface IForestryClientApi {
	IForestryClientApi INSTANCE = ServiceLoader.load(IForestryClientApi.class).findFirst().orElseThrow();

	ITextureManager getTextureManager();

	void registerSaplingModels(Map<ITreeSpecies, Pair<ResourceLocation, ResourceLocation>> models);
}
