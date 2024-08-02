package forestry.plugin;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.item.Item;

import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableFactory;
import forestry.api.plugin.IWindfallFarmableBuilder;

public class WindfallFarmableBuilder implements IWindfallFarmableBuilder {
	private final ImmutableSet.Builder<Item> windfalls = new ImmutableSet.Builder<>();
	private final IFarmableFactory factory;

	public WindfallFarmableBuilder(IFarmableFactory factory) {
		this.factory = factory;
	}

	@Override
	public IWindfallFarmableBuilder addWindfall(Item windfall) {
		this.windfalls.add(windfall);
		return this;
	}

	@Override
	public IWindfallFarmableBuilder addWindfalls(Iterable<? extends Item> windfall) {
		this.windfalls.addAll(windfall);
		return this;
	}

	public IFarmable build(Item germling) {
		return this.factory.create(germling, this.windfalls.build());
	}
}
