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
package forestry.core.gui;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.core.tooltips.IToolTipProvider;
import forestry.api.core.tooltips.ToolTip;

public class GuiUtil {
	public static void drawItemStack(PoseStack transform, GuiForestry<?> gui, ItemStack stack, int xPos, int yPos) {
		drawItemStack(transform, gui.getFontRenderer(), stack, xPos, yPos);
	}

	public static void drawItemStack(PoseStack transform, Font font, ItemStack stack, int xPos, int yPos) {
		PoseStack itemRendererStack = RenderSystem.getModelViewStack();
		itemRendererStack.pushPose();
		itemRendererStack.mulPoseMatrix(transform.last().pose());
		ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
		itemRender.renderAndDecorateItem(stack, xPos, yPos);
		itemRender.renderGuiItemDecorations(font, stack, xPos, yPos, null);
		itemRendererStack.popPose();
		RenderSystem.applyModelViewMatrix();
	}

	public static <S extends Screen & IGuiSizable> void drawToolTips(PoseStack transform, S screen, @Nullable IToolTipProvider provider, ToolTip toolTips, int mouseX, int mouseY) {
		if (!toolTips.isEmpty()) {
			transform.pushPose();
			if (provider == null || provider.isRelativeToGui()) {
				transform.translate(-screen.getGuiLeft(), -screen.getGuiTop(), 0);
			}
			screen.renderTooltip(transform, toolTips.getLines(), Optional.empty(), mouseX, mouseY);
			transform.popPose();
		}
	}

	public static <S extends Screen & IGuiSizable> void drawToolTips(PoseStack transform, S screen, Collection<?> objects, int mouseX, int mouseY) {
		for (Object obj : objects) {
			if (!(obj instanceof IToolTipProvider provider)) {
				continue;
			}
			if (!provider.isToolTipVisible()) {
				continue;
			}
			int mX = mouseX;
			int mY = mouseY;
			if (provider.isRelativeToGui()) {
				mX -= screen.getGuiLeft();
				mY -= screen.getGuiTop();
			}
			ToolTip tips = provider.getToolTip(mX, mY);
			if (tips == null) {
				continue;
			}
			boolean mouseOver = provider.isHovering(mX, mY);
			tips.onTick(mouseOver);
			if (mouseOver && tips.isReady()) {
				tips.refresh();
				drawToolTips(transform, screen, provider, tips, mouseX, mouseY);
			}
		}
	}

	public static String formatEnergyValue(int energy) {
		NumberFormat format = NumberFormat.getIntegerInstance(Minecraft.getInstance().getLocale());
		return format.format(energy) + " RF";
	}

	public static String formatRate(int rate) {
		return formatEnergyValue(rate) + "/t";
	}
}
