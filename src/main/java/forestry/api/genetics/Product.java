package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record Product(Item item, int count, @Nullable CompoundTag nbt, float chance) {
	public ItemStack getStack() {
		ItemStack stack = new ItemStack(item, count);
		stack.setTag(nbt);
		return stack;
	}
}
