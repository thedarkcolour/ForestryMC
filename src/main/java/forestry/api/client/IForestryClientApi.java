package forestry.api.client;

import java.util.ServiceLoader;

public interface IForestryClientApi {
	IForestryClientApi INSTANCE = ServiceLoader.load(IForestryClientApi.class).findFirst().orElseThrow();

	ITextureManager getTextureManager();
}
