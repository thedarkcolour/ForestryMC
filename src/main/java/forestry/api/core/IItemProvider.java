package forestry.api.core;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import forestry.modules.features.StackOption;

public interface IItemProvider<I extends Item> {
	I item();

	default ItemStack stack() {
		return stack(1);
	}

	default ItemStack stack(int amount) {
		return new ItemStack(item(), amount);
	}

	default ItemStack stack(StackOption... options) {
		ItemStack stack = stack();
		for (StackOption option : options) {
			option.accept(stack);
		}
		return stack;
	}

	default boolean itemEqual(ItemStack stack) {
		return !stack.isEmpty() && itemEqual(stack.getItem());
	}

	default boolean itemEqual(Item item) {
		return item() == item;
	}
}
