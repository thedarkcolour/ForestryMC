package forestry.apiimpl;

import forestry.api.IForestryApi;
import forestry.api.apiculture.hives.IHiveManager;
import forestry.api.circuits.ICircuitManager;
import forestry.api.climate.IClimateManager;
import forestry.api.core.IErrorManager;
import forestry.api.farming.IFarmRegistry;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.alleles.IAlleleManager;
import forestry.api.genetics.filter.IFilterManager;
import forestry.api.modules.IModuleManager;
import forestry.apiculture.hives.HiveManager;
import forestry.core.circuits.CircuitManager;
import forestry.core.climate.ForestryClimateManager;
import forestry.core.errors.ErrorManager;
import forestry.core.genetics.alleles.AlleleManager;
import forestry.farming.ForestryFarmRegistry;
import forestry.modules.ForestryModuleManager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class ForestryApiImpl implements IForestryApi {
	private final IModuleManager moduleManager = new ForestryModuleManager();
	private final IFarmRegistry farmRegistry = new ForestryFarmRegistry();
	private final IClimateManager biomeManager = new ForestryClimateManager();
	private final IHiveManager hiveManager = new HiveManager();
	private final IAlleleManager alleleRegistry = new AlleleManager();
	@Nullable
	private IGeneticManager geneticManager;
	@Nullable
	private IErrorManager errorManager;
	@Nullable
	private IFilterManager filterManager;
	@Nullable
	private ICircuitManager circuitManager;

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
		IErrorManager manager = this.errorManager;
		if (manager == null) {
			throw new IllegalStateException("IErrorManager not initialized yet");
		}
		return manager;
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
	public IFilterManager getFilterManager() {
		IFilterManager manager = this.filterManager;
		if (manager == null) {
			throw new IllegalStateException("IFilterManager not initialized yet. Wait until after item registration has finished");
		}
		return manager;
	}

	@Override
	public IGeneticManager getGeneticManager() {
		IGeneticManager manager = this.geneticManager;
		if (manager == null) {
			throw new IllegalStateException("IGeneticManager not initialized yet");
		}
		return this.geneticManager;
	}

	@Override
	public ICircuitManager getCircuitManager() {
		ICircuitManager manager = this.circuitManager;
		if (manager == null) {
			throw new IllegalStateException("ICircuitManager not initialized yet. Wait until after item registration has finished");
		}
		return manager;
	}

	@ApiStatus.Internal
	public void setCircuitManager(CircuitManager manager) {

	}

	public void setErrorManager(ErrorManager errorManager) {
		this.errorManager = errorManager;
	}
}
