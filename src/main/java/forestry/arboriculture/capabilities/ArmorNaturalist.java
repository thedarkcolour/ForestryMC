package forestry.arboriculture.capabilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import forestry.api.core.IArmorNaturalist;

public enum ArmorNaturalist implements IArmorNaturalist {
	INSTANCE;

	@Override
	public boolean canSeePollination(Player player, ItemStack armor, boolean doSee) {
		return true;
	}
}
