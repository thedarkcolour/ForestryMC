package forestry.api.apiculture.hives;

import com.google.common.collect.ImmutableList;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import forestry.apiculture.VillageHive;

public interface IHiveManager {
	/**
	 * @return List of all registered hives.
	 */
	List<IHive> getHives();

	/**
	 * @return List of common hives used when picking hives for Forestry village houses.
	 */
	ImmutableList<VillageHive> getCommonVillageHives();

	/**
	 * @return List of rare hives used when picking hives for Forestry village houses.
	 */
	ImmutableList<VillageHive> getRareVillageHives();

	/**
	 * @return A list of potential drops for the hive with the specified ID.
	 */
	List<IHiveDrop> getDrops(ResourceLocation id);

	/**
	 * Used to determine if an item is usable as fuel in an Alveary Swarmer for queen rearing.
	 * In default Forestry, only Royal Jelly is usable and returns {@code 0.01f}, or 1%.
	 *
	 * @return The chance
	 */
	float getSwarmingMaterialChance(Item swarmItem);
}
