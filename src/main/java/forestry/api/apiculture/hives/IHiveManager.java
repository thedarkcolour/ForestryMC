package forestry.api.apiculture.hives;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

public interface IHiveManager {
	/**
	 * @return List of all registered hives.
	 */
	List<IHive> getHives();

	/**
	 * @return A list of potential drops for the hive with the specified ID.
	 */
	List<IHiveDrop> getDrops(ResourceLocation id);
}
