package forestry.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import forestry.api.apiculture.IArmorApiarist;
import forestry.api.core.IArmorNaturalist;
import forestry.api.genetics.capability.IIndividualHandlerItem;
import forestry.api.genetics.filter.IFilterLogic;

/**
 * All capabilities added by base Forestry.
 */
public class ForestryCapabilities {
	// Apiculture
	public static Capability<IArmorApiarist> ARMOR_APIARIST = CapabilityManager.get(new CapabilityToken<>() {
	});

	// Arboriculture
	public static Capability<IArmorNaturalist> ARMOR_NATURALIST = CapabilityManager.get(new CapabilityToken<>() {
	});

	// Genetics
	public static Capability<IIndividualHandlerItem> INDIVIDUAL_HANDLER_ITEM = CapabilityManager.get(new CapabilityToken<>() {
	});

	// Genetic Filter
	public static Capability<IFilterLogic> FILTER_LOGIC = CapabilityManager.get(new CapabilityToken<>() {
	});
}
