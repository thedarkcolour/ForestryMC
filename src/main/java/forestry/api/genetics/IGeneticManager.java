package forestry.api.genetics;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.lepidopterology.genetics.IButterfly;

public interface IGeneticManager {
	Taxon getTaxon(String name);

	/**
	 * Retrieves the registry of possible mutations for a given species type.
	 * Register mutations using a {@link forestry.api.plugin.IForestryPlugin}.
	 *
	 * @param speciesTypeId The ID of the species type. See {@link ForestrySpeciesTypes} for types added by base Forestry.
	 * @return A registry of possible mutations for the given species type.
	 */
	<S extends ISpecies<?>> IMutationManager<S> getMutations(ResourceLocation speciesTypeId);

	IBreedingTracker getBreedingTracker(Level level, GameProfile player);

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

	IButterfly createRandomIndividual(ResourceLocation typeId, RandomSource rand, ILifeStage stage);
}
