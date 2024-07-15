package forestry.modules;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.RegisterEvent;

import forestry.Forestry;
import forestry.api.ForestryConstants;
import forestry.api.modules.IForestryModule;
import forestry.modules.features.ModFeatureRegistry;

//TODO - most of this needs tearing up and replacing
public class CommonModuleHandler {
	protected final ModFeatureRegistry registry = ModFeatureRegistry.get(ForestryConstants.MOD_ID);

	public void runSetup() {
		for (IForestryModule module : modules) {
			ResourceLocation id = module.getId();
			Forestry.LOGGER.debug("Setup API Start: {}", id);
			module.setupApi();
			Forestry.LOGGER.debug("Setup API Complete: {}", id);
		}
		for (IForestryModule module : disabledModules) {
			ResourceLocation id = module.getId();
			Forestry.LOGGER.warn("Fallback API Start: {}", id);
			module.setupFallbackApi();
			Forestry.LOGGER.warn("Fallback API Complete: {}", id);
		}
	}

	public void postRegistry(RegisterEvent event) {
		// used for wood kinds and block/item colors
		registry.postRegistry(event);
		// does misc object registration, not features
		postRegistry();
	}

	private void postRegistry() {
		for (IForestryModule module : modules) {
			// these are not registry objects, just other data
			module.registerObjects();
		}
	}

	public void registerGuiFactories() {
		for (IForestryModule module : modules) {
			module.registerMenuScreens();
		}
	}

	public void runInit() {
		for (IForestryModule module : modules) {
			Forestry.LOGGER.debug("Init Start: {}", module);
			module.doInit();
			module.registerRecipes();
			Forestry.LOGGER.debug("Init Complete: {}", module);
		}
	}

	private void registerHandlers(BlankForestryModule module) {
		Forestry.LOGGER.debug("Registering Handlers for Module: {}", module);
	}

	public void runPostInit() {
		for (IForestryModule module : modules) {
			Forestry.LOGGER.debug("Post-Init Start: {}", module);
			module.postInit();
			Forestry.LOGGER.debug("Post-Init Complete: {}", module);
		}
	}
}
