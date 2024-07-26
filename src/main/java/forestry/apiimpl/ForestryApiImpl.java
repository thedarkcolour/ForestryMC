package forestry.apiimpl;

import forestry.api.IForestryApi;
import forestry.api.apiculture.hives.IHiveManager;
import forestry.api.climate.IClimateManager;
import forestry.api.core.IErrorManager;
import forestry.api.farming.IFarmRegistry;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.alleles.IAlleleManager;
import forestry.api.modules.IModuleManager;
import forestry.apiculture.hives.HiveManager;
import forestry.core.climate.ForestryClimateManager;
import forestry.core.errors.ErrorManager;
import forestry.core.genetics.alleles.AlleleManager;
import forestry.farming.ForestryFarmRegistry;
import forestry.modules.ForestryModuleManager;

public class ForestryApiImpl implements IForestryApi {
	private final IModuleManager moduleManager = new ForestryModuleManager();
	private final IFarmRegistry farmRegistry = new ForestryFarmRegistry();
	private final IErrorManager errorStateRegistry = new ErrorManager();
	private final IClimateManager biomeManager = new ForestryClimateManager();
	private final IHiveManager hiveManager = new HiveManager();
	private final IAlleleManager alleleRegistry = new AlleleManager();
	private final IGeneticManager geneticManager;

	@Override
	public IModuleManager getModuleManager() {
		return this.moduleManager;
	}

	@Override
	public IFarmRegistry getFarmRegistry() {
		return this.farmRegistry;
	}

	@Override
	public IErrorManager getErrorManager() {
		return this.errorStateRegistry;
	}

	@Override
	public IClimateManager getClimateManager() {
		return this.biomeManager;
	}

	@Override
	public IHiveManager getHiveManager() {
		return this.hiveManager;
	}

	@Override
	public IAlleleManager getAlleleManager() {
		return this.alleleRegistry;
	}

	@Override
	public IGeneticManager getGeneticManager() {
		return this.geneticManager;
	}
}
