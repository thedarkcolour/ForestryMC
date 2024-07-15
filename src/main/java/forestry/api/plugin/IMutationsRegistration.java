package forestry.api.plugin;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.IMutationBuilder;

public interface IMutationsRegistration {
	// todo should the chance be a float?

	/**
	 * Registers a mutation that results in this species.
	 *
	 * @param firstParent  One of the species involved in this mutation.
	 * @param secondParent The other species involved in this mutation.
	 * @param chance       The chance (with 100 as the maximum) of this mutation occurring.
	 */
	IMutationBuilder add(ResourceLocation firstParent, ResourceLocation secondParent, int chance);
}
