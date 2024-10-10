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
package forestry.core.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.ForestryConstants;
import forestry.core.config.Constants;

public class GuiBetterButton extends Button {
	public static final ResourceLocation TEXTURE = ForestryConstants.forestry(Constants.TEXTURE_PATH_GUI + "/buttons.png");

	protected final IButtonTextureSet texture;

	public GuiBetterButton(int x, int y, IButtonTextureSet texture, OnPress handler) {
		super(x, y, texture.getWidth(), texture.getHeight(), Component.empty(), handler);
		this.texture = texture;
	}

	@Override
	public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		// VANILLA COPY EXCEPT FOR TEXTURE AND COORDINATES
		int xOffset = this.texture.getX();
		int yOffset = this.texture.getY();
		int h = this.height;
		int w = this.width;

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHoveredOrFocused());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		blit(pPoseStack, x, y, xOffset, yOffset + i * h, w, h);
		int j = getFGColor();
		drawCenteredString(pPoseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
	}
}
