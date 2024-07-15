package forestry.core.gui.slots;

import javax.annotation.Nullable;
import java.util.function.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import forestry.core.render.ForestryTextureManager;

public interface ISlotTextured {
	@Nullable
	ResourceLocation getBackgroundTexture();
}
