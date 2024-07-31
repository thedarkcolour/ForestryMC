package forestry.cultivation;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.IClientModuleHandler;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.cultivation.proxy.CultivationClientHandler;
import forestry.modules.BlankForestryModule;

@ForestryModule
public class ModuleCultivation extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CULTIVATION;
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new CultivationClientHandler());
	}

	@Override
	public List<ResourceLocation> getModuleDependencies() {
		return List.of(ForestryModuleIds.CORE, ForestryModuleIds.FARMING);
	}
}
