package forestry.api.apiculture.hives;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import forestry.apiculture.hives.Hive;

public interface IHiveManager {
	/**
	 * @return List of all registered hives.
	 */
	List<Hive> getHives();

	/**
	 * @return A list of potential drops for the hive with the specified ID.
	 */
	List<IHiveDrop> getDrops(ResourceLocation id);

	/**
	 * Returns a hiveGen for a hive that spawns on the ground.
	 * validGroundBlocks specifies which block materials it can spawn on.
	 */
	IHiveGen createGroundGen(Block... validGroundBlocks);

	/**
	 * Returns a hiveGen for a hive that spawns in trees.
	 */
	IHiveGen createTreeGen();
}
