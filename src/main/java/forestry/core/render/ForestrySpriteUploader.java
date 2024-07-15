package forestry.core.render;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

/**
 * Uploads the forestry gui icon texture sprites to the forestry gui atlas texture.
 *
 * @see ForestryTextureManager
 */
public class ForestrySpriteUploader extends TextureAtlasHolder implements Consumer<ResourceLocation> {
	private final ArrayList<ResourceLocation> registeredSprites = new ArrayList<>();

	public ForestrySpriteUploader(TextureManager manager, ResourceLocation atlasLocation, String prefix) {
		super(manager, atlasLocation, prefix);
	}

	public void accept(ResourceLocation location) {
		this.registeredSprites.add(location);
	}

	@Override
	protected Stream<ResourceLocation> getResourcesToLoad() {
		return this.registeredSprites.stream();
	}

	// Public override
	@Override
	public TextureAtlasSprite getSprite(ResourceLocation location) {
		return super.getSprite(location);
	}
}
