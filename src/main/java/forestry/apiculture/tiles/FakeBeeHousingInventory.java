package forestry.apiculture.tiles;

import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeHousingInventory;

public enum FakeBeeHousingInventory implements IBeeHousingInventory {
	INSTANCE;

	@Override
	public ItemStack getQueen() {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getDrone() {
		return ItemStack.EMPTY;
	}

	@Override
	public void setQueen(ItemStack itemstack) {
	}

	@Override
	public void setDrone(ItemStack itemstack) {
	}

	@Override
	public boolean addProduct(ItemStack product, boolean all) {
		return false;
	}
}
