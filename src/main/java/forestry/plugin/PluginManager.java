package forestry.plugin;

import java.util.ArrayList;
import java.util.ServiceLoader;

import forestry.api.plugin.IForestryPlugin;

public class PluginManager {
	private static final ArrayList<IForestryPlugin> LOADED_PLUGINS = new ArrayList<>();

	public static void init() {
		ServiceLoader<IForestryPlugin> serviceLoader = ServiceLoader.load(IForestryPlugin.class);

		for (IForestryPlugin plugin : serviceLoader) {
			if (plugin.shouldLoad()) {
				if (plugin.getClass() == DefaultForestryPlugin.class) {
					LOADED_PLUGINS.add(0, plugin);
				} else {
					LOADED_PLUGINS.add(plugin);
				}
			}
		}

		GeneticRegistration genetics = new GeneticRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			plugin.registerGenetics(genetics);
		}

		genetics.finishRegistration();
	}
}
