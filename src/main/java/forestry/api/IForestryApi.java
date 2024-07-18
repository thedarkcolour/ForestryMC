package forestry.api;

import java.util.ServiceLoader;

import forestry.api.apiculture.hives.IHiveManager;
import forestry.api.climate.IClimateManager;
import forestry.api.core.IErrorManager;
import forestry.api.farming.IFarmRegistry;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.alleles.IAlleleManager;
import forestry.api.modules.IModuleManager;

/**
 * The Forestry API class is used to query
 */
public interface IForestryApi {
	IForestryApi INSTANCE = ServiceLoader.load(IForestryApi.class).findFirst().orElseThrow();

	IModuleManager getModuleManager();

	IFarmRegistry getFarmRegistry();

	IErrorManager getErrorManager();

	IClimateManager getClimateManager();

	IHiveManager getHiveManager();

	/**
	 * @return The genetic manager, used to track taxonomy, mutations, species types, and registered species.
	 */
	IGeneticManager getGeneticManager();

	/**
	 * @return The allele manager, used to manage allele instances and ensures that there is at most one allele instance representing a certain value.
	 * Also used to create and register chromosomes as well as providing the allele codec.
	 */
	IAlleleManager getAlleleManager();
}
