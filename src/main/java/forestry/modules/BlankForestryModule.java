package forestry.modules;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IForestryModule;

public abstract class BlankForestryModule implements IForestryModule {
	/**
	 * The ForestryModule.moduleId()s of any other modules this module depends on.
	 */
	@Override
	public List<ResourceLocation> getModuleDependencies() {
		// todo is this necessary? overriding isCore should be sufficient
		return List.of(ForestryModuleIds.CORE);
	}

	@Override
	public String toString() {
		return getId().toString();
	}
}
