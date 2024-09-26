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

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import com.mojang.blaze3d.vertex.PoseStack;

import forestry.core.render.ColourProperties;

public class TextLayoutHelper {
	private static final int LINE_HEIGHT = 12;

	private final GuiForestry<?> guiForestry;
	private final int defaultFontColor;

	public int column0;
	public int column1;
	public int column2;
	public int line;

	public TextLayoutHelper(GuiForestry<?> gui, ColourProperties fontColour) {
		this.guiForestry = gui;
		this.defaultFontColor = fontColour.get("gui.screen");
	}

	public void startPage(PoseStack matrices) {
		line = LINE_HEIGHT;
		matrices.pushPose();
	}

	public void startPage(PoseStack matrices, int column0, int column1, int column2) {
		this.column0 = column0;
		this.column1 = column1;
		this.column2 = column2;

		startPage(matrices);
	}

	public int getLineY() {
		return line;
	}

	public void newLine() {
		line += LINE_HEIGHT;
	}

	public void newLineCompressed() {
		line += LINE_HEIGHT - 2;
	}

	public void newLine(int lineHeight) {
		line += lineHeight;
	}

	public void endPage(PoseStack matrices) {
		matrices.popPose();
	}

	public void drawRow(PoseStack transform, Component text0, Component text1, Component text2, int colour0, int colour1, int colour2) {
		drawLine(transform, text0, column0, colour0);
		drawLine(transform, text1, column1, colour1);
		drawLine(transform, text2, column2, colour2);
	}

	public void drawRow(PoseStack transform, Component text0, Component text1, int colour0, int colour1){
		drawLine(transform, text0, column0, colour0);
		drawLine(transform, text1, column1, colour1);
	}

	public void drawLine(PoseStack transform, Component text, int x) {
		drawLine(transform, text, x, defaultFontColor);
	}

	public void drawCenteredLine(PoseStack transform, Component text, int x, int color) {
		drawCenteredLine(transform, text, x, guiForestry.getSizeX(), color);
	}

	public void drawCenteredLine(PoseStack transform, Component text, int x, int width, int color) {
		drawCenteredLine(transform, text, x, 0, width, color);
	}

	public void drawCenteredLine(PoseStack transform, Component text, int x, int y, int width, int color) {
		guiForestry.getFontRenderer().draw(transform, text, guiForestry.getGuiLeft() + x + getCenteredOffset(text, width), guiForestry.getGuiTop() + y + line, color);
	}

	public void drawLine(PoseStack transform, String text, int x, int color) {
		drawLine(transform, text, x, 0, color);
	}

	public void drawLine(PoseStack transform, Component text, int x, int color) {
		drawLine(transform, text, x, 0, color);
	}

	public void drawLine(PoseStack transform, String text, int x, int y, int color) {
		guiForestry.getFontRenderer().draw(transform, text, guiForestry.getGuiLeft() + x, guiForestry.getGuiTop() + y + line, color);
	}

	public void drawLine(PoseStack transform, Component text, int x, int y, int color) {
		guiForestry.getFontRenderer().draw(transform, text, guiForestry.getGuiLeft() + x, guiForestry.getGuiTop() + y + line, color);
	}

	public void drawSplitLine(PoseStack transform, String text, int x, int maxWidth, int color) {
		drawSplitLine(transform, Component.literal(text), x, maxWidth, color);
	}

	public void drawSplitLine(PoseStack transform, Component text, int x, int maxWidth, int color) {
		// Modified copy of Font.drawWordWrap that uses PoseStack
		Font font = guiForestry.getFontRenderer();
		float xStart = guiForestry.getGuiLeft() + x;
		float yStart = guiForestry.getGuiTop() + line;

		for (FormattedCharSequence formattedcharsequence : font.split(text, maxWidth)) {
			font.draw(transform, formattedcharsequence, xStart, yStart, color);
			yStart += 9;
		}
	}

	public int getCenteredOffset(Component text) {
		return getCenteredOffset(text, guiForestry.getSizeX());
	}

	public int getCenteredOffset(Component text, int xWidth) {
		return (xWidth - guiForestry.getFontRenderer().width(text)) / 2;
	}
}
