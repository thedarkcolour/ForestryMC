package forestry.farming;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import forestry.api.farming.IFarmProperties;
import forestry.api.farming.IFarmPropertiesBuilder;
import forestry.api.farming.IFarmRegistry;
import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableInfo;
import forestry.farming.logic.farmables.FarmableInfo;

public class DummyFarmRegistry implements IFarmRegistry {
	@Override
	public void registerFarmables(String identifier, IFarmable... farmable) {
	}

	@Override
	public Collection<IFarmable> getFarmables(String identifier) {
		return Collections.emptyList();
	}

	@Override
	public IFarmableInfo getFarmableInfo(String identifier) {
		return new FarmableInfo(identifier);
	}

	@Override
	public IFarmProperties registerLogic(String identifier, IFarmProperties farmInstance) {
		return null;
	}

	@Override
	public IFarmPropertiesBuilder getPropertiesBuilder(String identifier) {
		return null;
	}

	@Override
	public void registerFertilizer(Ingredient fertilizer, int value) {
	}

	@Override
	public int getFertilizeValue(ItemStack stack) {
		return 0;
	}

	@Nullable
	@Override
	public IFarmProperties getProperties(String identifier) {
		return null;
	}
}
