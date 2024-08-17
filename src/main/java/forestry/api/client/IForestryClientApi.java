package forestry.api.client;

import java.util.ServiceLoader;

import forestry.api.client.arboriculture.ITreeClientManager;
import forestry.api.client.lepidopterology.IButterflyClientManager;
import forestry.api.client.plugin.IClientHelper;

/**
 * The Forestry Client API manages client-only data related to Forestry.
 */
public interface IForestryClientApi {
	IForestryClientApi INSTANCE = ServiceLoader.load(IForestryClientApi.class).findFirst().orElseThrow();

	ITextureManager getTextureManager();

	ITreeClientManager getTreeManager();

	IButterflyClientManager getButterflyManager();

	/**
	 * @since 1.0.5
	 * @return The IClientHelper containing methods to create instances of Forestry's client objects.
	 */
	IClientHelper getHelper();
}
