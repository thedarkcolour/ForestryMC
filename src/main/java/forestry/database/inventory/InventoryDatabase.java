package forestry.database.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

import forestry.api.genetics.capability.IIndividualHandlerItem;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.database.tiles.TileDatabase;

public class InventoryDatabase extends InventoryAdapterTile<TileDatabase> {
	public InventoryDatabase(TileDatabase tile) {
		super(tile, 136, "Items");
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack stack) {
		return IIndividualHandlerItem.isIndividual(stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction side) {
		return super.canTakeItemThroughFace(slotIndex, stack, side);
	}
}
