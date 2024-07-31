package forestry.api.apiculture.hives;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

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
}
