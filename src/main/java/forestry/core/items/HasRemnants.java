package forestry.core.items;

import deleteme.Todos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

import java.util.function.Supplier;

// todo why is this an interface?
public interface HasRemnants {

	ItemStack getRemnants();

	default void todo() {
		// mixin ItemStack#hurtAndBreak
		Todos.todo();
	}

	class Pickaxe extends PickaxeItem implements HasRemnants {

		private final Supplier<ItemStack> remnants;

		public Pickaxe(Tier tier, int damageBonus, float speedModifier, Properties properties, Supplier<ItemStack> remnants) {
			super(tier, damageBonus, speedModifier, properties);
			this.remnants = remnants;
		}

		@Override
		public ItemStack getRemnants() {
			return remnants.get();
		}
	}

	class Shovel extends ShovelItem implements HasRemnants {

		private final Supplier<ItemStack> remnants;

		public Shovel(Tier tier, float damageBonus, float speedModifier, Properties properties, Supplier<ItemStack> remnants) {
			super(tier, damageBonus, speedModifier, properties);
			this.remnants = remnants;
		}

		@Override
		public ItemStack getRemnants() {
			return remnants.get();
		}
	}
}
