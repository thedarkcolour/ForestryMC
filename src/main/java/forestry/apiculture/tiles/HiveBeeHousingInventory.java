package forestry.apiculture.tiles;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.core.utils.SpeciesUtil;

class HiveBeeHousingInventory implements IBeeHousingInventory {
	@Nullable
	private ItemStack queen;
	@Nullable
	private ItemStack drone;

	private final TileHive hive;

	public HiveBeeHousingInventory(TileHive hive) {
		this.hive = hive;
	}

	@Override
	public ItemStack getQueen() {
		if (queen == null) {
			IBee bee = hive.getContainedBee();
			queen = SpeciesUtil.BEE_TYPE.get().getTypes().createStack(bee, BeeLifeStage.QUEEN);
		}
		return queen;
	}

	@Override
	public ItemStack getDrone() {
		if (drone == null) {
			IBee bee = hive.getContainedBee();
			drone = SpeciesUtil.BEE_TYPE.get().getTypes().createStack(bee, BeeLifeStage.DRONE);
		}
		return drone;
	}

	@Override
	public void setQueen(ItemStack stack) {
		this.queen = stack;
	}

	@Override
	public void setDrone(ItemStack stack) {
		this.drone = stack;
	}

	@Override
	public boolean addProduct(ItemStack product, boolean all) {
		return true;
	}
}
