package forestry.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import forestry.api.apiculture.IArmorApiarist;
import forestry.api.climate.IClimateListener;
import forestry.api.climate.IClimateTransformer;
import forestry.api.core.IArmorNaturalist;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.filter.IFilterLogic;

import genetics.api.IGeneTemplate;
import genetics.api.individual.IIndividual;

public class ForestryCapabilities {
	// Apiculture
	public static Capability<IArmorApiarist> ARMOR_APIARIST = CapabilityManager.get(new CapabilityToken<>() {});

	// Arboriculture
	public static Capability<IArmorNaturalist> ARMOR_NATURALIST = CapabilityManager.get(new CapabilityToken<>() {});

	// Genetics
	public static Capability<IIndividual> INDIVIDUAL = CapabilityManager.get(new CapabilityToken<>() {});

	// Climate (habitat reformer)
	public static Capability<IClimateListener> CLIMATE_LISTENER = CapabilityManager.get(new CapabilityToken<>() {});
	public static Capability<IClimateTransformer> CLIMATE_TRANSFORMER = CapabilityManager.get(new CapabilityToken<>() {});

	// Genetic Filter
	public static Capability<IFilterLogic> FILTER_LOGIC = CapabilityManager.get(new CapabilityToken<>() {});
}
