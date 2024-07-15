/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************//*

package forestry.climatology.gui.elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.ForestryConstants;
import forestry.api.client.ForestrySprites;
import forestry.api.climate.IClimateTransformer;
import forestry.api.climate.IClimatised;
import forestry.climatology.gui.GuiHabitatFormer;
import forestry.core.gui.elements.GuiElement;
import forestry.core.gui.elements.layouts.ContainerElement;

// todo fix
@OnlyIn(Dist.CLIENT)
public class HabitatSelectionElement extends ContainerElement {
	private static final Comparator<ClimateButton> BUTTON_COMPARATOR = Comparator.comparingDouble(ClimateButton::getComparingCode);
	private static final ResourceLocation TEXTURE = new ResourceLocation(ForestryConstants.MOD_ID, "textures/gui/habitat_former.png");
	private final List<ClimateButton> buttons = new ArrayList<>();
	private final IClimateTransformer transformer;

	public HabitatSelectionElement(int xPos, int yPos, IClimateTransformer transformer) {
		setPos(xPos, yPos);
		setSize(60, 40);
		this.transformer = transformer;
		;
		int x = 0;
		int y = 0;
		for (EnumClimate climate : EnumClimate.values()) {
			ClimateButton button = new ClimateButton(climate, x * 20, y * 20);
			buttons.add(button);
			add(button);
			x++;
			if (x >= 3) {
				y++;
				x = 0;
			}
		}
	}

	@Override
	public void drawElement(PoseStack transform, int mouseX, int mouseY) {
		super.drawElement(transform, mouseX, mouseY);
		RenderSystem.setShaderTexture(0, new ResourceLocation(ForestryConstants.MOD_ID, "textures/gui/habitat_former.png"));
		Optional<ClimateButton> optional = buttons.stream().min(BUTTON_COMPARATOR);
		if (!optional.isPresent()) {
			return;
		}
		ClimateButton button = optional.get();
		blit(transform, button.getX() - 1, button.getY() - 1, 0, 233, 22, 22);
	}

	//TODO: Fix
	private enum EnumClimate {
		*/
/*ICY("habitats/snow", Biomes.SNOWY_TUNDRA),
		COLD("habitats/taiga", Biomes.TAIGA),
		HILLS("habitats/hills", Biomes.SWAMP),
		NORMAL("habitats/plains", Biomes.PLAINS),
		WARM("habitats/jungle", Biomes.JUNGLE),
		HOT("habitats/desert", Biomes.DESERT)*//*
;
		private IClimatised climateState;
		private String spriteName;

		EnumClimate(String spriteName, Biome biome) {
			//climateState = ClimateStateHelper.of(biome.getBaseTemperature(), biome.getDownfall());
			this.spriteName = spriteName;
		}

		@OnlyIn(Dist.CLIENT)
		public TextureAtlasSprite getSprite() {
			throw new RuntimeException("");
			//return IForestryClientApi.INSTANCE.getTextureManager().getSprite(spriteName);
		}
	}

	private class ClimateButton extends GuiElement {
		final EnumClimate climate;

		private ClimateButton(EnumClimate climate, int xPos, int yPos) {
			super(xPos, yPos);
			setSize(20, 20);
			this.climate = climate;
			*/
/*addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
				IClimateState climateState = climate.climateState;
				GuiHabitatFormer former = (GuiHabitatFormer) getWindow().getGui();
				former.setClimate(climateState);
				former.sendClimateUpdate();
			});*//*

			addTooltip((tooltip, element, mouseX, mouseY) -> {
				tooltip.add(Component.literal("T: " + climate.climateState.temperature()));
				tooltip.add(Component.literal("H: " + climate.climateState.humidity()));
			});
		}

		@Override
		public boolean onMouseClicked(double mouseX, double mouseY, int mouseButton) {
			IClimatised climateState = climate.climateState;
			GuiHabitatFormer former = (GuiHabitatFormer) getWindow().getGui();
			former.setClimate(climateState);
			former.sendClimateUpdate();
			return true;
		}

		@Override
		public void drawElement(PoseStack transform, int mouseX, int mouseY) {
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0F);
			RenderSystem.setShaderTexture(0, TEXTURE);
			blit(transform, 0, 0, 204, 46, 20, 20);
			RenderSystem.setShaderTexture(0, ForestrySprites.TEXTURE_ATLAS);
			blit(transform, 2, 2, getBlitOffset(), 16, 16, climate.getSprite());
		}

		private double getComparingCode() {
			IClimatised target = transformer.getTarget();
			IClimatised state = climate.climateState;
			//double temp = target.temperature() - state.temperature();
			//double hem = target.humidity() - state.humidity();
			return 0.0;//Math.abs(temp + hem);
		}
	}
}
*/
