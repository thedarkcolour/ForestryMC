package forestry.farming.blocks;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;

import forestry.api.ForestryConstants;
import forestry.api.core.IBlockSubtype;
import forestry.core.utils.ResourceUtil;

public enum EnumFarmBlockType implements IBlockSubtype {
	PLAIN,
	GEARBOX,
	HATCH,
	VALVE,
	CONTROL;

	public static final EnumFarmBlockType[] VALUES = values();

	private static final int TYPE_PLAIN = 0;
	private static final int TYPE_REVERSE = 1;
	private static final int TYPE_TOP = 2;
	private static final int TYPE_BAND = 3;
	private static final int TYPE_GEARS = 4;
	private static final int TYPE_HATCH = 5;
	private static final int TYPE_VALVE = 6;
	private static final int TYPE_CONTROL = 7;

	@OnlyIn(Dist.CLIENT)
	@Nullable
	private static ImmutableList<TextureAtlasSprite> sprites;

	@OnlyIn(Dist.CLIENT)
	public static void gatherSprites(TextureStitchEvent.Pre event) {
		event.addSprite(ForestryConstants.forestry("block/farm/plain"));
		event.addSprite(ForestryConstants.forestry("block/farm/reverse"));
		event.addSprite(ForestryConstants.forestry("block/farm/top"));
		event.addSprite(ForestryConstants.forestry("block/farm/band"));
		event.addSprite(ForestryConstants.forestry("block/farm/gears"));
		event.addSprite(ForestryConstants.forestry("block/farm/hatch"));
		event.addSprite(ForestryConstants.forestry("block/farm/valve"));
		event.addSprite(ForestryConstants.forestry("block/farm/control"));
	}

	@OnlyIn(Dist.CLIENT)
	public static void fillSprites(TextureStitchEvent.Post event) {
		TextureAtlas map = event.getAtlas();
		sprites = ImmutableList.of(
			map.getSprite(ForestryConstants.forestry("block/farm/plain")),
			map.getSprite(ForestryConstants.forestry("block/farm/reverse")),
			map.getSprite(ForestryConstants.forestry("block/farm/top")),
			map.getSprite(ForestryConstants.forestry("block/farm/band")),
			map.getSprite(ForestryConstants.forestry("block/farm/gears")),
			map.getSprite(ForestryConstants.forestry("block/farm/hatch")),
			map.getSprite(ForestryConstants.forestry("block/farm/valve")),
			map.getSprite(ForestryConstants.forestry("block/farm/control"))
		);
	}

	/**
	 * @return The texture sprite from the type of the farm block
	 */
	@OnlyIn(Dist.CLIENT)
	public static TextureAtlasSprite getSprite(EnumFarmBlockType type, int side) {
		if (sprites == null) {
			return ResourceUtil.getMissingTexture();
		}
		switch (type) {
			case PLAIN: {
				if (side == 2) {
					return sprites.get(TYPE_REVERSE);
				} else if (side == 0 || side == 1) {
					return sprites.get(TYPE_TOP);
				}
				return sprites.get(TYPE_PLAIN);
			}
			case GEARBOX:
				return sprites.get(TYPE_GEARS);
			case HATCH:
				return sprites.get(TYPE_HATCH);
			case VALVE:
				return sprites.get(TYPE_VALVE);
			case CONTROL:
				return sprites.get(TYPE_CONTROL);
			default:
				return sprites.get(TYPE_PLAIN);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite[] getSprites() {
		TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
		for (int side = 0; side < textures.length; side++) {
			textures[side] = getSprite(this, side);
		}
		return textures;
	}

	@OnlyIn(Dist.CLIENT)
	public static TextureAtlasSprite[] getBandSprites() {
		TextureAtlasSprite band = sprites == null ? ResourceUtil.getMissingTexture() : sprites.get(TYPE_BAND);
		TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
		Arrays.fill(textures, band);
		return textures;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

}
