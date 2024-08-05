package forestry.api.client.arboriculture;

import forestry.api.arboriculture.ITreeSpecies;

import org.jetbrains.annotations.Nullable;

public interface ITreeClientManager {
	ILeafSprite getLeafSprite(@Nullable ITreeSpecies species);

	ILeafTint getTint(@Nullable ITreeSpecies species);
}
