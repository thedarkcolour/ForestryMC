package forestry.api.client;

import java.util.ServiceLoader;

import forestry.api.client.arboriculture.ITreeClientManager;

/**
 * The Forestry Client API manages client-only data related to Forestry.
 */
public interface IForestryClientApi {
	IForestryClientApi INSTANCE = ServiceLoader.load(IForestryClientApi.class).findFirst().orElseThrow();

	ITextureManager getTextureManager();

	ITreeClientManager getTreeManager();
}
