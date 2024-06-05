package forestry.core.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class DummyMenu extends AbstractContainerMenu {
	public static final DummyMenu INSTANCE = new DummyMenu();

	protected DummyMenu() {
		super(null, 0);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return null;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return true;
	}
}
