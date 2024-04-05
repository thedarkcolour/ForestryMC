package forestry.storage;

import net.minecraftforge.fml.DistExecutor;

import forestry.api.modules.ForestryModule;
import forestry.core.config.Constants;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ISidedModuleHandler;
import forestry.storage.proxy.ProxyStorage;
import forestry.storage.proxy.ProxyStorageClient;

@ForestryModule(moduleID = ForestryModuleUids.CRATE, modId = Constants.MOD_ID, name = "Crate", author = "SirSengir", url = Constants.URL, unlocalizedDescription = "for.module.crates.description")
public class ModuleCrates extends BlankForestryModule {

	private final ProxyStorage proxy = DistExecutor.safeRunForDist(() -> ProxyStorageClient::new, () -> ProxyStorage::new);

	@Override
	public ISidedModuleHandler getModuleHandler() {
		return proxy;
	}
}
