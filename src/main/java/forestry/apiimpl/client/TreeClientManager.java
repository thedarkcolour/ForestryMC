package forestry.apiimpl.client;

import java.util.IdentityHashMap;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.client.arboriculture.ILeafTint;
import forestry.api.client.arboriculture.ITreeClientManager;

import org.jetbrains.annotations.Nullable;

public class TreeClientManager implements ITreeClientManager {
	private final IdentityHashMap<ITreeSpecies, ILeafSprite> sprites;
	private final IdentityHashMap<ITreeSpecies, ILeafTint> tints;

	public TreeClientManager(IdentityHashMap<ITreeSpecies, ILeafSprite> sprites, IdentityHashMap<ITreeSpecies, ILeafTint> tints) {
		this.sprites = sprites;
		this.tints = tints;
	}

	@Override
	public ILeafSprite getLeafSprite(@Nullable ITreeSpecies species) {
		return this.sprites.get(species);
	}

	@Override
	public ILeafTint getTint(@Nullable ITreeSpecies species) {
		return this.tints.get(species);
	}
}
