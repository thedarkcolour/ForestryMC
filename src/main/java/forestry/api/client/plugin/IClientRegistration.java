package forestry.api.client.plugin;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.arboriculture.ILeafSprite;

/**
 * Handles client registration for Forestry plugins.
 */
public interface IClientRegistration {
	/**
	 * Registers a model for a tree species's saplings. Required for all species.
	 *
	 * @param speciesId  The ID of the tree species to register models for.
	 * @param blockModel The block model for the sapling.
	 * @param itemModel  The item model for the sapling.
	 */
	void registerSaplingModel(ResourceLocation speciesId, ResourceLocation blockModel, ResourceLocation itemModel);

	/**
	 * Registers a custom leaf sprite for this tree. Required for all species.
	 *
	 * @param speciesId The ID of the tree species to register models for.
	 * @param sprite    The sprite used for leaf rendering.
	 */
	void registerLeafSprite(ResourceLocation speciesId, ILeafSprite sprite);

	void registerButterflySprite();
}
