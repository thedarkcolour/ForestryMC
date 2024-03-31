/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.jarjar.nio.util.LambdaExceptionUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Util methods used at the installation of the game or at the reloading or baking of resources like models or
 * textures.
 */
@OnlyIn(Dist.CLIENT)
public class ResourceUtil {

	private ResourceUtil() {
	}

	public static ResourceManager resourceManager() {
		return Minecraft.getInstance().getResourceManager();
	}

	public static TextureAtlasSprite getMissingTexture() {
		return getSprite(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation());
	}

	public static TextureAtlasSprite getSprite(ResourceLocation atlas, ResourceLocation sprite) {
		return Minecraft.getInstance().getTextureAtlas(atlas).apply(sprite);
	}

	public static TextureAtlasSprite getBlockSprite(ResourceLocation location) {
		return getSprite(InventoryMenu.BLOCK_ATLAS, location);
	}

	public static TextureAtlasSprite getBlockSprite(String location) {
		return getBlockSprite(new ResourceLocation(location));
	}

	public static boolean resourceExists(ResourceLocation location) {
		return resourceManager().getResource(location).isPresent();
	}

	public static Optional<Resource> getResource(ResourceLocation location) {
		return resourceManager().getResource(location);
	}

	/**
	 * @return The model from the item of the stack.
	 */
	@Nullable
	public static BakedModel getModel(ItemStack stack) {
		ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
        return renderItem.getItemModelShaper().getItemModel(stack.getItem());
	}

	public static SimpleModelState loadTransform(ResourceLocation location) {
		// todo
		return new SimpleModelState(Transformation.identity());
	}

	private static ItemTransforms loadTransformFromJson(ResourceLocation location) {
		Optional<Reader> readerOptional = getReaderForResource(location);
		if (readerOptional.isPresent()) {
			try (Reader reader = readerOptional.get()) {
				return BlockModel.fromStream(reader).getTransforms();
			} catch (IOException e) {
                e.printStackTrace();
            }
        }
		return ItemTransforms.NO_TRANSFORMS;
	}

	private static Optional<Reader> getReaderForResource(ResourceLocation location) {
		ResourceLocation file = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
		return resourceManager().getResource(file).map(resource -> {
			try {
				return new BufferedReader(new InputStreamReader(resource.open(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		});
	}
}
