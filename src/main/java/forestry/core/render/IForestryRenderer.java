package forestry.core.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import forestry.api.ForestryConstants;

/**
 * Handler for the rendering of a forestry tile entity and its item.
 *
 * @param <T> The type of the tile entity
 */
public interface IForestryRenderer<T extends BlockEntity> {
	static ModelLayerLocation register(String name) {
		return new ModelLayerLocation(ForestryConstants.forestry(name), "main");
	}

	/**
	 * Renders the given tile entity.
	 *
	 * @param tile The tile entity that this handler renders.
	 */
	void renderTile(T tile, RenderHelper helper);

	/**
	 * Renders the given stack of the tile entity
	 *
	 * @param stack The item stack of the tile entity.
	 */
	void renderItem(ItemStack stack, RenderHelper helper);
}
