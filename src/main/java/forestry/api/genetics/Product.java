package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.Hash;

public record Product(Item item, int count, @Nullable CompoundTag nbt, float chance) {
	// Hash for quick de-duping
	public static final Hash.Strategy<Product> ITEM_ONLY_STRATEGY = new Hash.Strategy<>() {
		@Override
		public int hashCode(Product o) {
			return o.item.hashCode();
		}

		@Override
		public boolean equals(Product a, Product b) {
			return a.item == b.item;
		}
	};

	public ItemStack createStack() {
		ItemStack stack = new ItemStack(item, count);
		stack.setTag(nbt);
		return stack;
	}

	public static Product of(Item item) {
		return new Product(item, 1, null, 1f);
	}

	public static Product of(Item item, int amount, float chance) {
		return new Product(item, amount, null, chance);
	}
}
