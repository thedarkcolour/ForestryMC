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
package forestry.apiculture.gui;

import java.util.Locale;
import java.util.Set;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.ForestryConstants;
import forestry.api.client.ForestrySprites;
import forestry.api.client.IForestryClientApi;
import forestry.api.core.tooltips.ToolTip;
import forestry.core.gui.widgets.Widget;
import forestry.core.gui.widgets.WidgetManager;

public class HabitatSlot extends Widget {
	private final TagKey<Biome> biomes;
	private final String name;
	private final ResourceLocation iconIndex;
	public boolean isActive = false;

	public HabitatSlot(WidgetManager widgetManager, int xPos, int yPos, String name, TagKey<Biome> biomes) {
		super(widgetManager, xPos, yPos);
		this.biomes = biomes;
		this.name = name;
		this.iconIndex = ForestryConstants.forestry("habitats/" + name.toLowerCase(Locale.ENGLISH));
	}

	@Override
	public ToolTip getToolTip(int mouseX, int mouseY) {
		ToolTip tooltip = new ToolTip();
		tooltip.add(Component.literal(name));
		return tooltip;
	}

	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite getIcon() {
		return IForestryClientApi.INSTANCE.getTextureManager().getSprite(iconIndex);
	}

	public void setActive(Set<TagKey<Biome>> biomes) {
		isActive = biomes.contains(this.biomes);
	}

	@Override
	public void draw(PoseStack transform, int startX, int startY) {
		if (!isActive) {
			RenderSystem.setShaderColor(0.2f, 0.2f, 0.2f, 0.2f);
		} else {
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		RenderSystem.setShaderTexture(0, ForestrySprites.TEXTURE_ATLAS);
		GuiComponent.blit(transform, startX + xPos, startY + yPos, manager.gui.getBlitOffset(), 16, 16, getIcon());
	}
}
