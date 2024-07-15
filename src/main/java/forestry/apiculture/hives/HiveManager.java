/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.apiculture.hives;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Objects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.apiculture.hives.IHiveGen;
import forestry.api.apiculture.hives.IHiveManager;

public class HiveManager implements IHiveManager {
	private final ImmutableMap<ResourceLocation, Hive> registry;

	public HiveManager(ImmutableMap<ResourceLocation, Hive> registry) {
		this.registry = registry;
	}

	@Override
	public List<Hive> getHives() {
		return this.registry.values().asList();
	}

	@Override
	public List<IHiveDrop> getDrops(ResourceLocation id) {
		return Objects.requireNonNull(this.registry.get(id), "No hive registered with name " + id).getDrops();
	}

	@Override
	public IHiveGen createGroundGen(Block... validGroundBlocks) {
		return new HiveGenGround(validGroundBlocks);
	}

	@Override
	public IHiveGen createTreeGen() {
		return HiveGenTree.INSTANCE;
	}
}
