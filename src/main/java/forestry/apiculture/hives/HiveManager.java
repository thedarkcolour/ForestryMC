/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.apiculture.hives;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Objects;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.hives.IHive;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.apiculture.hives.IHiveManager;
import forestry.apiculture.VillageHive;

public class HiveManager implements IHiveManager {
	private final ImmutableMap<ResourceLocation, IHive> registry;
	private final ImmutableList<VillageHive> commonVillageHives;
	private final ImmutableList<VillageHive> rareVillageHives;

	public HiveManager(ImmutableMap<ResourceLocation, IHive> registry, ImmutableList<VillageHive> commonVillageHives, ImmutableList<VillageHive> rareVillageHives) {
		this.registry = registry;
		this.commonVillageHives = commonVillageHives;
		this.rareVillageHives = rareVillageHives;
	}

	@Override
	public List<IHive> getHives() {
		return this.registry.values().asList();
	}

	@Override
	public List<IHiveDrop> getDrops(ResourceLocation id) {
		return Objects.requireNonNull(this.registry.get(id), "No hive registered with name " + id).getDrops();
	}
}
