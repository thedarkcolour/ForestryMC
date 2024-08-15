/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.apiculture.hives;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.hives.IHive;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.apiculture.hives.IHiveManager;
import forestry.apiculture.BeeHousingListener;
import forestry.apiculture.BeeHousingModifier;
import forestry.apiculture.BeekeepingLogic;
import forestry.apiculture.VillageHive;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;

public class HiveManager implements IHiveManager {
	private final ImmutableMap<ResourceLocation, IHive> registry;

	private final ImmutableList<VillageHive> commonVillageHives;
	private final ImmutableList<VillageHive> rareVillageHives;
	private final Object2FloatOpenHashMap<Item> swarmerMaterials;

	public HiveManager(ImmutableMap<ResourceLocation, IHive> registry, ImmutableList<VillageHive> commonVillageHives, ImmutableList<VillageHive> rareVillageHives, Object2FloatOpenHashMap<Item> swarmerMaterials) {
		this.registry = registry;
		this.commonVillageHives = commonVillageHives;
		this.rareVillageHives = rareVillageHives;
		this.swarmerMaterials = swarmerMaterials;
	}

	@Override
	public List<IHive> getHives() {
		return this.registry.values().asList();
	}

	@Override
	public ImmutableList<VillageHive> getCommonVillageHives() {
		return commonVillageHives;
	}

	@Override
	public ImmutableList<VillageHive> getRareVillageHives() {
		return rareVillageHives;
	}

	@Override
	public List<IHiveDrop> getDrops(ResourceLocation id) {
		IHive hive = this.registry.get(id);
		if (hive == null) {
			return List.of();
		} else {
			return hive.getDrops();
		}
	}

	@Override
	public float getSwarmingMaterialChance(Item swarmItem) {
		return swarmerMaterials.getFloat(swarmItem);
	}

	@Override
	public IBeekeepingLogic createBeekeepingLogic(IBeeHousing housing) {
		return new BeekeepingLogic(housing);
	}

	@Override
	public IBeeModifier createBeeHousingModifier(IBeeHousing housing) {
		return new BeeHousingModifier(housing);
	}

	@Override
	public IBeeListener createBeeHousingListener(IBeeHousing housing) {
		return new BeeHousingListener(housing);
	}
}
