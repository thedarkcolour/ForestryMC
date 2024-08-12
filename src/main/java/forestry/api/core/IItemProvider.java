package forestry.api.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemProvider<I extends Item> {
	I item();

	default ItemStack stack() {
		return stack(1);
	}

	default ItemStack stack(int amount) {
		return new ItemStack(item(), amount);
	}

	default boolean itemEqual(ItemStack stack) {
		return !stack.isEmpty() && itemEqual(stack.getItem());
	}

	default boolean itemEqual(Item item) {
		return item() == item;
	}
}
