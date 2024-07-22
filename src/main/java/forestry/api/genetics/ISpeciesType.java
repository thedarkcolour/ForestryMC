package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.alleles.IKaryotype;

public interface ISpeciesType<S extends ISpecies<?>> {
	/**
	 * @return The unique ID of this species type.
	 */
	ResourceLocation id();

	/**
	 * @return The karyotype for all members of this species type.
	 */
	IKaryotype getKaryotype();

	@Nullable
	default ILifeStage getLifeStage(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL).map(IIndividual::getLifeStage).orElse(null);
	}

	/**
	 * Called when all species of this type have been registered and modified.
	 * Used to initialize species statistics.
	 *
	 * @param allSpecies The list of every species of this type.
	 */
	void onSpeciesRegistered(List<S> allSpecies);

	/**
	 * @return The mutation manager for this species type.
	 */
	IMutationManager<S> getMutations();

	/**
	 * @return The list of all members of this species type.
	 * @throws IllegalStateException If not all species have been registered yet.
	 */
	List<S> getSpecies();

	/**
	 * @return The species of this type registered with the given ID.
	 * @throws RuntimeException      If no species was found with that ID.
	 * @throws IllegalStateException If not all species have been registered yet.
	 */
	S getSpeciesById(ResourceLocation id);

	/**
	 * @return Integer denoting the number of non-secret species of this type in the world.
	 * @throws IllegalStateException If not all species have been registered yet.
	 */
	int getSpeciesCount();

	/**
	 * Life stage used to represent this species in icons
	 */
	ILifeStage getDefaultStage();

	IBreedingTracker getBreedingTracker(LevelAccessor world, @Nullable GameProfile player);

	/**
	 * The type of the species that will be used at the given position of the mutation recipe in the gui.
	 *
	 * @param position 0 = first parent, 1 = second parent, 2 = result
	 */
	// todo check if this is needed
	default ILifeStage getTypeForMutation(int position) {
		return getDefaultStage();
	}

	/**
	 * Plugin to add information for the handheld genetic analyzer.
	 */
	IAlyzerPlugin getAlyzerPlugin();

	/**
	 * Plugin to add information for the handheld genetic analyzer and the database.
	 *
	 * @since 5.7
	 */
	@Nullable
	default IPollinatable getSpeciesPlugin() {
		return null;
	}

	ItemStack createStack(IIndividual species, ILifeStage type);

	IIndividual createIndividual(ItemStack stack);

	/**
	 * Used to check whether the given {@link IIndividual} is member of this class.
	 *
	 * @param individual {@link IIndividual} to check.
	 * @return true if the individual is member of this class, false otherwise.
	 */
	boolean isMember(IIndividual individual);

	@SuppressWarnings({"DataFlowIssue", "ConstantValue"})
	default boolean isMember(ItemStack stack) {
		IIndividual individual = stack.getCapability(ForestryCapabilities.INDIVIDUAL).orElse(null);
		return individual != null && isMember(individual);
	}
}
