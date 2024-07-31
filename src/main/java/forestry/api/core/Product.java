package forestry.api.core;

import javax.annotation.Nullable;
import java.util.Optional;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.Hash;

public record Product(Item item, int count, @Nullable CompoundTag tag, float chance) {
	// Hash for quick de-duping
	public static final Hash.Strategy<Product> ITEM_ONLY_STRATEGY;
	public static final Codec<Product> CODEC;
	// todo StreamCodec in 1.21

	public ItemStack createStack() {
		ItemStack stack = new ItemStack(item, count);
		stack.setTag(tag);
		return stack;
	}

	public static Product of(Item item) {
		return new Product(item, 1, null, 1f);
	}

	public static Product of(Item item, int amount, float chance) {
		return new Product(item, amount, null, chance);
	}

	public static void toNetwork(FriendlyByteBuf buffer, Product product) {
		buffer.writeId(Registry.ITEM, product.item);
		buffer.writeByte(product.count);
		buffer.writeNbt(product.tag);
		buffer.writeFloat(product.chance);
	}

	public static Product fromNetwork(FriendlyByteBuf buffer) {
		Item item = buffer.readById(Registry.ITEM);
		int count = buffer.readByte();
		CompoundTag tag = buffer.readNbt();
		float chance = buffer.readFloat();

		if (item == null) {
			throw new IllegalStateException("Received invalid item ID");
		}

		return new Product(item, count, tag, chance);
	}

	static {
		ITEM_ONLY_STRATEGY = new Hash.Strategy<>() {
			@Override
			public int hashCode(Product o) {
				return o.item.hashCode();
			}

			@Override
			public boolean equals(Product a, Product b) {
				return a.item == b.item;
			}
		};

		CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Registry.ITEM.byNameCodec().fieldOf("item").forGetter(Product::item),
				Codec.intRange(1, 64).optionalFieldOf("count", 1).forGetter(Product::count),
				CompoundTag.CODEC.optionalFieldOf("tag").forGetter(product -> Optional.ofNullable(product.tag)),
				Codec.floatRange(0f, 1f).fieldOf("chance").forGetter(Product::chance)
		).apply(instance, (item, count, tag, chance) -> new Product(item, count, tag.orElse(null), chance)));
	}
}
