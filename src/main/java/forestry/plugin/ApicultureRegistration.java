package forestry.plugin;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.plugin.IApicultureRegistration;
import forestry.apiculture.genetics.IBeeDefinition;

public class ApicultureRegistration implements IApicultureRegistration {
	@Override
	public void registerSpecies(IBeeDefinition species) {

	}

	@Override
	public void registerHive(ResourceLocation id, IHiveDefinition definition) {

	}

	@Override
	public void addHiveDrop(ResourceLocation id, IHiveDrop drop) {

	}

	@Override
	public void addHiveDrops(ResourceLocation id, List<IHiveDrop> drops) {

	}
}
