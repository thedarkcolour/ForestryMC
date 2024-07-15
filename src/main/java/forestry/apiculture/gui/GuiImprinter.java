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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;

import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.inventory.ItemInventoryImprinter;
import forestry.core.config.Constants;
import forestry.core.gui.GuiForestry;
import forestry.core.gui.GuiUtil;
import forestry.core.network.packets.PacketGuiSelectRequest;
import forestry.core.render.ColourProperties;
import forestry.core.utils.NetworkUtil;

import genetics.api.GeneticHelper;
import genetics.api.organism.IIndividualCapability;

public class GuiImprinter extends GuiForestry<ContainerImprinter> {
	private final ItemInventoryImprinter itemInventory;
	private int startX;
	private int startY;

	private final Map<String, ItemStack> iconStacks = new HashMap<>();

	public GuiImprinter(ContainerImprinter container, Inventory inventoryplayer, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/imprinter.png", container, inventoryplayer, title);

		this.itemInventory = container.getItemInventory();
		this.imageWidth = 176;
		this.imageHeight = 185;

		NonNullList<ItemStack> beeList = NonNullList.create();
		ApicultureItems.BEE_DRONE.item().addCreativeItems(beeList, false);
		for (ItemStack beeStack : beeList) {
			IIndividualCapability<?> organism = GeneticHelper.getOrganism(beeStack);
			if (organism.isEmpty()) {
				continue;
			}
			IAlleleBeeSpecies species = organism.getAllele((IChromosome<ISpeciesType<?>>) BeeChromosomes.SPECIES, true);
			iconStacks.put(species.getId().toString(), beeStack);
		}
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseY, int mouseX) {
		super.renderBg(transform, partialTicks, mouseY, mouseX);

		int offset = (138 - getFontRenderer().width(Component.translatable("for.gui.imprinter"))) / 2;
		getFontRenderer().draw(transform, Component.translatable("for.gui.imprinter"), startX + 8 + offset, startY + 16, ColourProperties.INSTANCE.get("gui.screen"));

		IAlleleBeeSpecies primary = itemInventory.getPrimary();
		drawBeeSpeciesIcon(transform, primary, startX + 12, startY + 32);
		getFontRenderer().draw(transform, primary.getDisplayName().getString(), startX + 32, startY + 36, ColourProperties.INSTANCE.get("gui.screen"));

		IAlleleBeeSpecies secondary = itemInventory.getSecondary();
		drawBeeSpeciesIcon(transform, secondary, startX + 12, startY + 52);
		getFontRenderer().draw(transform, secondary.getDisplayName().getString(), startX + 32, startY + 56, ColourProperties.INSTANCE.get("gui.screen"));

		Component youCheater = Component.translatable("for.gui.imprinter.cheater");
		offset = (138 - getFontRenderer().width(youCheater)) / 2;
		getFontRenderer().draw(transform, youCheater, startX + 8 + offset, startY + 76, ColourProperties.INSTANCE.get("gui.screen"));

	}

	private void drawBeeSpeciesIcon(PoseStack transform, IAlleleBeeSpecies bee, int x, int y) {
		GuiUtil.drawItemStack(transform, this, iconStacks.get(bee.getId().toString()), x, y);
	}

	private static int getHabitatSlotAtPosition(double i, double j) {
		int[] xPos = new int[]{12, 12};
		int[] yPos = new int[]{32, 52};

		for (int l = 0; l < xPos.length; l++) {
			if (i >= xPos[l] && i <= xPos[l] + 16 && j >= yPos[l] && j <= yPos[l] + 16) {
				return l;
			}
		}

		return -1;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int k) {
		if (super.mouseClicked(mouseX, mouseY, k)) {
			return true;
		}

		int cornerX = (width - imageWidth) / 2;
		int cornerY = (height - imageHeight) / 2;

		int slot = getHabitatSlotAtPosition(mouseX - cornerX, mouseY - cornerY);
		if (slot < 0) {
			return false;
		}

		if (k == 0) {
			advanceSelection(slot);
		} else {
			regressSelection(slot);
		}
		return true;
	}

	@Override
	public void init() {
		super.init();

		startX = (this.width - this.imageWidth) / 2;
		startY = (this.height - this.imageHeight) / 2;
	}

	private static void advanceSelection(int index) {
		sendSelectionChange(index, 0);
	}

	private static void regressSelection(int index) {
		sendSelectionChange(index, 1);
	}

	private static void sendSelectionChange(int index, int advance) {
		NetworkUtil.sendToServer(new PacketGuiSelectRequest(index, advance));
	}

	@Override
	protected void addLedgers() {

	}
}
