package forestry.api.client.arboriculture;

import forestry.api.arboriculture.ITreeSpecies;

import org.jetbrains.annotations.Nullable;

/**
 * Tracks client-only data for tree species.
 */
public interface ITreeClientManager {
	/**
	 * @return The leaf sprite for the given species.
	 */
	ILeafSprite getLeafSprite(@Nullable ITreeSpecies species);

	/**
	 * @return The leaf tint for the given species.
	 */
	ILeafTint getTint(@Nullable ITreeSpecies species);
}
