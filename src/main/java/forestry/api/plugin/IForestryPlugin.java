package forestry.api.plugin;

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
	 * Override to register your bee species, mutations, etc. at the correct time.
	 */
	default void registerApiculture(IApicultureRegistration apiculture, IGeneticRegistration genetics) {
	}

	default void registerArboriculture(IArboricultureRegistration arboriculture, IGeneticRegistration genetics) {
	}

	default void registerLepidopterology(IGeneticRegistration genetics) {
	}
}
