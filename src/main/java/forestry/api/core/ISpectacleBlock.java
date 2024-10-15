package forestry.api.core;

import net.minecraft.world.entity.player.Player;

/**
 * Implement this interface on your block entities so that players wearing Spectacles can see them more easily.
 */
public interface ISpectacleBlock {
	/**
	 * Determines whether a highlight should be drawn around this block.
	 *
	 * @param player The player wearing spectacles.
	 * @return {@code true} if a highlighted bounding box should be drawn around this block, {@code false} otherwise.
	 */
	default boolean isHighlighted(Player player) {
		return true;
	}
}
