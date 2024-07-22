package forestry.api.plugin;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;

/**
 * Entry point for apiculture related registration.
 * Obtain an instance by overriding {@link IForestryPlugin#registerApiculture)}.
 */
public interface IApicultureRegistration {
	/**
	 * Register a new bee species.
	 *
	 * @param id       The unique ID for this species.
	 * @param genus    The scientific name of the genus containing this species. See {@link forestry.api.genetics.ForestryTaxa}.
	 * @param species  The scientific name of this species without the genus. (Ex. mellifera, for <i>Apis mellifera</i>)
	 * @param dominant Whether this species appears as a dominant allele in a genome.
	 * @param outline  The color used for tinting the bee's outline. IntelliJ should show a nice color preview.
	 * @throws IllegalStateException If a species has already been registered with the given ID.
	 */
	IBeeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, Color outline);

	/**
	 * Modify a species that was already registered.
	 * You can change all the same things as in {@link #registerSpecies} except for the genus and species names.
	 *
	 * @param id     The ID of the species to modify.
	 * @param action A function that receives the builder of the species to modify.
	 */
	void modifySpecies(ResourceLocation id, Consumer<IBeeSpeciesBuilder> action);

	/**
	 * Adds a bee species to the possible bees found in apiaries in the Apiarist villager houses.
	 *
	 * @param id   The ID of the species to add.
	 * @param rare If true, this bee goes into the "rare" village bee pool, which is chosen 25% of the time instead of the "common" pool.
	 */
	void addVillageBee(ResourceLocation id, boolean rare);

	/**
	 * Register a wild hive for world generation.
	 */
	IHiveBuilder registerHive(ResourceLocation id, IHiveDefinition definition);

	void addHiveDrop(ResourceLocation id, IHiveDrop drop);

	default void addHiveDrops(ResourceLocation id, List<IHiveDrop> drops) {
		for (IHiveDrop drop : drops) {
			addHiveDrop(id, drop);
		}
	}

	void registerFlowerType(IFlowerType type);

	void registerBeeEffect(IBeeEffect effect);
}
