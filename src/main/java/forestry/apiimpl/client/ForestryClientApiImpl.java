package forestry.apiimpl.client;

import com.google.common.base.Preconditions;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.client.IForestryClientApi;
import forestry.api.client.ITextureManager;
import forestry.api.modules.ForestryModule;
import forestry.core.render.ForestryTextureManager;

public class ForestryClientApiImpl implements IForestryClientApi {
	private final ITextureManager textureManager = new ForestryTextureManager();

	public ForestryClientApiImpl() {
		Preconditions.checkState(FMLEnvironment.dist == Dist.CLIENT, "Tried to load IForestryClientApi on invalid side " + FMLEnvironment.dist);
	}

	@Override
	public ITextureManager getTextureManager() {
		return this.textureManager;
	}

	@Override
	public void registerSaplingModels(Map<ITreeSpecies, Pair<ResourceLocation, ResourceLocation>> models) {

	}
}
