package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

/**
 * Home to all things genetics.
 * Keeps track of registered species types, which track all their registered member species and are
 * responsible for creating instances of {@link IIndividual} and {@link net.minecraft.world.item.ItemStack}.
 * Keeps track of all taxonomy, breeding trackers, and mutations.
 */
public interface IGeneticManager {
	/**
	 * @return The taxon instance with the given (lowercase) name. See {@link ForestryTaxa} for all names used by base Forestry.
	 * @throws IllegalArgumentException If no taxon was registered with that name.
	 */
	ITaxon getTaxon(String name);

	/**
	 * Returns a list containing this taxon and its parents in order of taxonomic rank. See {@link TaxonomicRank}.
	 *
	 * @param name The name of the taxon whose parent taxa to retrieve.
	 * @return A list containing this taxon and its parents in order of taxonomic rank.
	 */
	ITaxon[] getParentTaxa(String name);

	/**
	 * Retrieves the registry of possible mutations for a given species type.
	 * Register mutations using a {@link forestry.api.plugin.IForestryPlugin}.
	 *
	 * @param speciesType The species type.
	 * @return A registry of possible mutations for the given species type.
	 * @throws IllegalStateException If mutations have not been registered yet.
	 */
	<S extends ISpecies<?>> IMutationManager<S> getMutations(ISpeciesType<?, ?> speciesType);

	default <S extends ISpecies<?>> IMutationManager<S> getMutations(ResourceLocation speciesTypeId) {
		return getMutations(getSpeciesType(speciesTypeId));
	}

	/**
	 * Gets a registered species type by its ID. Use {@link ISpeciesType#cast} to get the desired type.
	 *
	 * @return The {@link ISpeciesType} with the given ID.
	 * @throws IllegalArgumentException If no species type was found with that ID.
	 */
	ISpeciesType<?, ?> getSpeciesType(ResourceLocation speciesTypeId);

	/**
	 * Gets a registered species type by its ID, or returns {@code null} if no species type was found.
	 *
	 * @return The {@link ISpeciesType} with the given ID, or {@code null} if no species type was registered with that ID.
	 */
	@Nullable
	ISpeciesType<?, ?> getSpeciesTypeSafe(ResourceLocation speciesTypeId);

	/**
	 * @return The {@link ISpeciesType} with the given ID and type.
	 * @throws IllegalArgumentException If no species type was found with that ID.
	 */
	default <T extends ISpeciesType<?, ?>> T getSpeciesType(ResourceLocation speciesTypeId, Class<T> typeClass) {
		return typeClass.cast(getSpeciesType(speciesTypeId));
	}

	/**
	 * Creates a new individual using the default genome of the given species type.
	 *
	 * @param typeId The ID of the species type to create an individual for.
	 * @param <I>    The generic type of individual. Unchecked cast.
	 * @return The new individual with a default genome.
	 */
	default <I extends IIndividual> I createDefaultIndividual(ResourceLocation typeId) {
		ISpeciesType<?, I> o = getSpeciesType(typeId).cast();

		return o.getDefaultSpecies().createIndividual();
	}

	/**
	 * @return A collection of all registered species types.
	 */
	Collection<ISpeciesType<?, ?>> getSpeciesTypes();
}
