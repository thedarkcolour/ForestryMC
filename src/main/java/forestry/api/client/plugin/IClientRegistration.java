package forestry.api.client.plugin;

import net.minecraft.resources.ResourceLocation;

import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.client.arboriculture.ILeafTint;

/**
 * Handles client registration for Forestry plugins.
 */
public interface IClientRegistration {
	/**
	 * Registers a model for a tree species's saplings. Required for all tree species.
	 *
	 * @param speciesId  The ID of the tree species to register models for.
	 * @param blockModel The block model for the sapling.
	 * @param itemModel  The item model for the sapling.
	 */
	void setSaplingModel(ResourceLocation speciesId, ResourceLocation blockModel, ResourceLocation itemModel);

	/**
	 * Sets the leaf sprite of this species. Required for all tree species.
	 *
	 * @param speciesId The ID of the tree species to register models for.
	 * @param sprite    The sprite used for leaf rendering. Can be reused for multiple species.
	 */
	void setLeafSprite(ResourceLocation speciesId, ILeafSprite sprite);

	/**
	 * Sets the custom leaf tint object of this species. If none is registered, then a default tint based on the
	 * escritoire color assigned to the tree species will be used instead.
	 *
	 * @param speciesId The ID of the tree species to set the tint for.
	 * @param tint      The tint. Can be reused for multiple species.
	 */
	void setLeafTint(ResourceLocation speciesId, ILeafTint tint);

	// todo
	void setButterflySprite();
}
