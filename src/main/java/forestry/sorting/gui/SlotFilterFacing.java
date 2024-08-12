package forestry.sorting.gui;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.core.gui.slots.ISlotTextured;

public class SlotFilterFacing extends Slot implements ISlotTextured {
	private static final ResourceLocation SPRITE = ForestryConstants.forestry("slots/bee");

	public SlotFilterFacing(Container inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ResourceLocation getBackgroundTexture() {
		return SPRITE;
	}
}
