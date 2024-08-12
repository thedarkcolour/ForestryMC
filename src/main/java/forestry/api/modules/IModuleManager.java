/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.modules;

import java.util.Collection;
import java.util.List;

import net.minecraft.resources.ResourceLocation;

/**
 * The module manager of Forestry.
 * Modules are a way to organize related features and compatibility into separate parts of the mod.
 * Register your {@link IForestryModule} classes with the {@link ForestryModule} annotation.
 */
public interface IModuleManager {
	/**
	 * @return List of loaded modules in LOAD order, where dependency modules come before dependent modules.
	 * Modules with the same dependencies are not guaranteed to be in the same order between runs.
	 */
	Collection<IForestryModule> getLoadedModules();

	/**
	 * @return {@code true} if a module with the given ID is loaded,
	 * or {@code false} if the module does not exist or is missing module dependencies.
	 */
	boolean isModuleLoaded(ResourceLocation id);

	List<IForestryModule> getModulesForMod(String modId);
}
