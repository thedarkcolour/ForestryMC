package forestry.api.genetics;

import java.util.Collection;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import com.mojang.serialization.Codec;

/**
 * Home to all things genetics.
 * Keeps track of registered species types, which track all their registered member species and are
 * responsible for creating instances of {@link IIndividual} and {@link net.minecraft.world.item.ItemStack}.
 * Keeps track of all taxonomy, breeding trackers, and mutations.
 */
public interface IGeneticManager {
	/**
	 * @return The taxon instance with the given (lowercase) name. See {@link ForestryTaxa} for all names used by base Forestry.
	 * @throws IllegalStateException If no taxon was registered with that name.
	 */
	Taxon getTaxon(String name);

	/**
	 * Retrieves the registry of possible mutations for a given species type.
	 * Register mutations using a {@link forestry.api.plugin.IForestryPlugin}.
	 *
	 * @param speciesTypeId The ID of the species type. See {@link ForestrySpeciesTypes} for types added by base Forestry.
	 * @return A registry of possible mutations for the given species type.
	 */
	<S extends ISpecies<?>> IMutationManager<S> getMutations(ResourceLocation speciesTypeId);

	/**
	 * @return The {@link ISpeciesType} with the given ID. Unchecked cast.
	 * @throws IllegalStateException If no species type was found with that ID.
	 */
	<T extends ISpeciesType<?>> T getSpeciesType(ResourceLocation speciesTypeId);

	/**
	 * @return The {@link ISpeciesType} with the given ID and type.
	 */
	default <T extends ISpeciesType<?>> T getSpeciesType(ResourceLocation speciesTypeId, Class<T> typeClass) {
		T type = getSpeciesType(speciesTypeId);
		if (typeClass.isAssignableFrom(type.getClass())) {
			return type;
		} else {
			throw new ClassCastException("ISpeciesType with ID " + speciesTypeId + " cannot be cast to type " + typeClass);
		}
	}

	/**
	 * Creates a new individual using the default genome of the given species type.
	 *
	 * @param id  The ID of the species type to create an individual for.
	 * @param <I> The generic type of individual. Unchecked cast.
	 * @return The new individual with a default genome.
	 */
	<I extends IIndividual> I createDefaultIndividual(ResourceLocation id);

	/**
	 * @param typeId
	 * @param speciesId
	 * @param stage
	 * @param <I>       The generic type of the individual. Unchecked cast.
	 * @return
	 */
	<I extends IIndividual> I createIndividual(ResourceLocation typeId, ResourceLocation speciesId, ILifeStage stage);

	// todo move these methods to the species type.
	IIndividual createRandomIndividual(ResourceLocation typeId, RandomSource rand, ILifeStage stage);

	Collection<ISpeciesType<?, ?>> getSpeciesTypes();
}
