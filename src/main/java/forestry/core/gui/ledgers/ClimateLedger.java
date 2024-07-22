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
package forestry.core.gui.ledgers;

import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.client.IForestryClientApi;
import forestry.api.climate.IClimatised;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ClimateHelper;

import net.minecraft.network.chat.Component;

/**
 * A ledger containing climate information.
 */
public class ClimateLedger extends Ledger {

	private final IClimatised tile;

	public ClimateLedger(LedgerManager manager, IClimatised tile) {
		super(manager, "climate");
		this.tile = tile;
		maxHeight = 72;
	}

	@Override
	public void draw(PoseStack transform, int y, int x) {

		TemperatureType temperature = tile.temperature();

		// Draw background
		drawBackground(transform, y, x);

		// Draw icon
		drawSprite(transform, IForestryClientApi.INSTANCE.getTextureManager().getSprite(temperature), x + 3, y + 4);

		if (!isFullyOpened()) {
			return;
		}

		drawHeader(transform, Component.translatable("for.gui.climate"), x + 22, y + 8);

		drawSubheader(transform, Component.translatable("for.gui.temperature").append(":"), x + 22, y + 20);
		drawText(transform, ClimateHelper.toDisplay(temperature).getString() + ' ' + tile.temperature(), x + 22, y + 32);

		drawSubheader(transform, Component.translatable("for.gui.humidity").append(":"), x + 22, y + 44);
		drawText(transform, ClimateHelper.toDisplay(tile.humidity()).getString() + ' ' + tile.humidity(), x + 22, y + 56);
	}

	@Override
	public Component getTooltip() {
		return Component.literal("T: ")
			.append(ClimateHelper.toDisplay(tile.temperature()))
			.append(Component.literal(" / H: "))
			.append(ClimateHelper.toDisplay(tile.humidity()));
	}

}
