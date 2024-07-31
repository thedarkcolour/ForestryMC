package forestry.cultivation.gui.widgets;

import javax.annotation.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.core.tooltips.ToolTip;
import forestry.api.farming.HorizontalDirection;
import forestry.core.gui.widgets.ItemStackWidget;
import forestry.core.gui.widgets.WidgetManager;
import forestry.core.render.ColourProperties;
import forestry.cultivation.inventory.InventoryPlanter;

public class GhostItemStackWidget extends ItemStackWidget {
	private final Slot slot;

	public GhostItemStackWidget(WidgetManager widgetManager, int xPos, int yPos, ItemStack itemStack, Slot slot) {
		super(widgetManager, xPos, yPos, itemStack);
		this.slot = slot;
	}

	@Override
	public void draw(PoseStack transform, int startX, int startY) {
		if (!slot.hasItem()) {
			super.draw(transform, startX, startY);
		}
		// RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();

		Component directionString = getDirectionString();
		if (directionString != null) {
			Font fontRenderer = manager.minecraft.font;
			fontRenderer.drawShadow(transform, directionString, xPos + startX + 5, yPos + startY + 4, ColourProperties.INSTANCE.get("gui.screen"));
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);

		RenderSystem.setShaderTexture(0, manager.gui.textureFile);
		manager.gui.blit(transform, xPos + startX, yPos + startY, 206, 0, 16, 16);

		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
		// RenderSystem.enableLighting();
	}

	@Nullable
	private Component getDirectionString() {
		if (slot.getSlotIndex() >= InventoryPlanter.CONFIG.productionStart
				|| slot.getSlotIndex() < InventoryPlanter.CONFIG.productionStart + InventoryPlanter.CONFIG.productionCount) {
			return null;
		}
		int index = slot.getSlotIndex() % 4;
		Direction direction = HorizontalDirection.VALUES.get(index);
		String directionString = direction.getSerializedName();
		return Component.translatable("for.gui.planter." + directionString);
	}

	@Nullable
	@Override
	public ToolTip getToolTip(int mouseX, int mouseY) {
		return null;
	}
}
