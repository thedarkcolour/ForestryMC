package forestry.api;

import java.util.ServiceLoader;

import forestry.api.apiculture.hives.IHiveManager;
import forestry.api.climate.IClimateManager;
import forestry.api.core.IErrorManager;
import forestry.api.farming.IFarmRegistry;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.alleles.IAlleleRegistry;
import forestry.api.modules.IModuleManager;

public interface IForestryApi {
	IForestryApi INSTANCE = ServiceLoader.load(IForestryApi.class).findFirst().orElseThrow();

	IModuleManager getModuleManager();

	IFarmRegistry getFarmRegistry();

	IErrorManager getErrorManager();

	IClimateManager getClimateManager();

	IHiveManager getHiveManager();

	IGeneticManager getGeneticManager();

	IAlleleRegistry getAlleleFactory();
}
