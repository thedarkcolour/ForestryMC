package forestry.apiculture.genetics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import forestry.api.core.IProduct;

// used by secret Patriotic bee species
public record FireworkProduct(float chance) implements IProduct {
	private static DyeColor[] COLORS = {DyeColor.RED, DyeColor.WHITE, DyeColor.BLUE};

	@Override
	public Item item() {
		return Items.FIREWORK_ROCKET;
	}

	@Override
	public ItemStack createStack() {
		return new ItemStack(Items.FIREWORK_ROCKET);
	}

	@Override
	public ItemStack createRandomStack(RandomSource random) {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
		// firework data
		CompoundTag fireworksTag = firework.getOrCreateTagElement("Fireworks");
		// list of explosions (firework stars)
		ListTag explosionsTag = new ListTag();

		// one explosion with random dye color, shape, and 50% chance for flicker
		CompoundTag explosion = new CompoundTag();
		DyeColor color = COLORS[random.nextInt(3)];
		explosion.putIntArray("Colors", new int[]{color.getFireworkColor()});
		explosion.putByte("Type", (byte) random.nextInt(5));
		explosion.putBoolean("Flicker", random.nextBoolean());

		// add to tag
		explosionsTag.add(explosion);
		fireworksTag.put("Explosions", explosionsTag);
		fireworksTag.putByte("Flight", (byte) 2);

		return firework;
	}
}
