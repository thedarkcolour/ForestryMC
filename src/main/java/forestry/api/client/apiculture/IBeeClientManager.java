package forestry.api.client.apiculture;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.genetics.ILifeStage;

/**
 * Tracks client-only data for bee species.
 */
public interface IBeeClientManager {
	/**
	 * Retrieves all model locations used to display bees with the given life stage.
	 * To add a custom model for your bee, use {@link forestry.api.client.plugin.IClientRegistration#setCustomBeeModel}.
	 * If no custom model is set, then the default model for the given life stage will be used instead, which is set by
	 * {@link forestry.api.client.plugin.IClientRegistration#setDefaultBeeModel}.
	 *
	 * @param stage The life stage to retrieve bee models for.
	 * @return A bee model map for the given life stage. (Ex. all drone models)
	 */
	Map<IBeeSpecies, ResourceLocation> getBeeModels(ILifeStage stage);
}
