package forestry.storage;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.modules.BlankForestryModule;
import forestry.api.client.IClientModuleHandler;
import forestry.storage.client.StorageClientHandler;

@ForestryModule
public class ModuleCrates extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CRATE;
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new StorageClientHandler());
	}
}
