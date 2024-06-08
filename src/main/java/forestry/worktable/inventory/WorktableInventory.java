package forestry.worktable.inventory;

import net.minecraft.world.item.ItemStack;

import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.utils.SlotUtil;
import forestry.worktable.tiles.WorktableTile;

public class WorktableInventory extends InventoryAdapterTile<WorktableTile> {
	public static final int SLOT_INVENTORY_1 = 0;
	public static final int SLOT_INVENTORY_COUNT = 18;

	public WorktableInventory(WorktableTile worktable) {
		super(worktable, 18, "Items");
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack stack) {
		return SlotUtil.isSlotInRange(slotIndex, SLOT_INVENTORY_1, SLOT_INVENTORY_COUNT);
	}
}
