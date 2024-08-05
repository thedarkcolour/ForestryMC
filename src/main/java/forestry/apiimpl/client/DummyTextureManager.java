package forestry.apiimpl.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;

import forestry.api.client.ITextureManager;

// Used in data generation
public class DummyTextureManager implements ITextureManager {
	@Override
	public TextureAtlasSprite getSprite(ResourceLocation location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerSprites(TextureStitchEvent.Pre event) {

	}
}
