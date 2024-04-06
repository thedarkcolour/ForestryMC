package forestry.storage;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.modules.ForestryModule;
import forestry.core.ClientsideCode;
import forestry.core.config.Constants;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ISidedModuleHandler;
import forestry.storage.proxy.ProxyStorage;

@ForestryModule(moduleID = ForestryModuleUids.CRATE, modId = Constants.MOD_ID, name = "Crate", author = "SirSengir", url = Constants.URL, unlocalizedDescription = "for.module.crates.description")
public class ModuleCrates extends BlankForestryModule {

	private static final ProxyStorage PROXY = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newProxyStorage() : new ProxyStorage();

	@Override
	public ISidedModuleHandler getModuleHandler() {
		return PROXY;
	}
}
