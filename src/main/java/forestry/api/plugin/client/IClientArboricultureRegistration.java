package forestry.api.plugin.client;

import net.minecraft.resources.ResourceLocation;

public interface IClientArboricultureRegistration {
	void registerSaplingModels(ResourceLocation speciesId, ResourceLocation blockModel, ResourceLocation itemModel);
}
