package forestry.api.plugin;

import java.awt.Color;
import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.ForestryFlowerType;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.apiculture.genetics.IBeeDefinition;

/**
 * Entry point for apiculture related registration.
 * Obtain an instance by overriding {@link IForestryPlugin#registerApiculture)}.
 */
public interface IApicultureRegistration {
	/**
	 * Register a new bee species.
	 *
	 * @param id       The unique ID for this species.
	 * @param genus    The name of the genus containing this species. See {@link forestry.api.genetics.ForestryTaxa}.
	 * @param dominant Whether this species appears as a dominant allele in a genome.
	 * @param outline  The color used for tinting the bee's outline. IntelliJ should show a nice color preview.
	 */
	IBeeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, Color outline);

	/**
	 * Adds a bee species to the possible bee spawns in Apiarist villager houses.
	 */
	void addVillageBee(IBeeDefinition species, boolean rare);

	/**
	 * Register a wild hive for world generation.
	 */
	void registerHive(ResourceLocation id, IHiveDefinition definition);

	void addHiveDrop(ResourceLocation id, IHiveDrop drop);

	default void addHiveDrops(ResourceLocation id, List<IHiveDrop> drops) {
		for (IHiveDrop drop : drops) {
			addHiveDrop(id, drop);
		}
	}

	void registerFlowerType(ForestryFlowerType type);
}
