package forestry.api.plugin;

import net.minecraft.resources.ResourceLocation;

/**
 * Entry point for registering things to the Forestry API. Forestry uses {@link java.util.ServiceLoader} to
 * load IForestryPlugin classes, so your mod jar must include a file named:
 *
 * <pre>{@code
 *     META-INF/services/forestry.api.plugin.IForestryPlugin
 * }</pre>
 * <p>
 * With a line for each IForestryPlugin class you want to register:
 *
 * <pre>{@code
 *     com.example.examplemod.compat.ExampleForestryPlugin  # You can list multiple plugins
 *     com.example.examplemod.compat.AnotherExampleForestryPlugin
 * }</pre>
 *
 * @see java.util.ServiceLoader For how to implement a service provider for the IForestryPlugin service
 */
public interface IForestryPlugin {
	/**
	 * Called before registerApiculture, registerArboriculture, and registerLepidopterology to set up core genetic data.
	 */
	default void registerGenetics(IGeneticRegistration genetics) {
	}

	/**
	 * Override to register your bee species, effects, flower types, hives, etc. at the correct time.
	 */
	default void registerApiculture(IApicultureRegistration apiculture) {
	}

	/**
	 * Override to register your tree species, fruits, etc. at the correct time.
	 */
	default void registerArboriculture(IArboricultureRegistration arboriculture) {
	}

	/**
	 * Override to register your butterfly species at the correct time.
	 */
	default void registerLepidopterology(ILepidopterologyRegistration lepidopterology) {
	}

	/**
	 * Called after all species are registered. Register your circuits and circuit layout types heere.
	 */
	default void registerCircuits(ICircuitRegistration circuits) {
	}

	default void registerErrors(IErrorRegistration errors) {
	}

	default void registerFarming(IFarmingRegistration farming) {
	}

	/**
	 * @return Unique ID for this plugin.
	 */
	ResourceLocation id();

	/**
	 * @return Whether this plugin should have any of its registerGenetics, registerApiculture, etc. methods called.
	 */
	default boolean shouldLoad() {
		return true;
	}
}
