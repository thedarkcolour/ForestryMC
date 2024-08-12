package forestry.api.genetics;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.alleles.AllelePair;

/**
 * Represents a mutation between two species (in any order) into a third species.
 * A mutation should never have both parents of the same species.
 */
public interface IMutation<S extends ISpecies<?>> {
	/**
	 * @return {@link ISpeciesType} this mutation is associated with.
	 */
	ISpeciesType<S, ?> getType();

	/**
	 * @return One of the species required for this mutation.
	 */
	S getFirstParent();

	/**
	 * @return The other species required for this mutation.
	 */
	S getSecondParent();

	/**
	 * @return The species resulting from this mutation.
	 */
	S getResult();

	/**
	 * @return The list of alleles the resulting individual should have. Usually the default genome of result species.
	 */
	ImmutableList<AllelePair<?>> getResultAlleles();

	/**
	 * @return Unmodified base chance for mutation to fire.
	 */
	int getBaseChance();

	/**
	 * @return Collection of localized, human-readable strings describing special mutation conditions, if any.
	 */
	Collection<Component> getSpecialConditions();

	/**
	 * @return List of all conditions required for this mutation to occur.
	 */
	List<IMutationCondition> getConditions();

	/**
	 * @return {@code true} if the passed species is one of the alleles participating in this mutation.
	 */
	boolean isPartner(ISpecies<?> species);

	/**
	 * @return The other parent in this mutation besides the given species.
	 */
	ISpecies<?> getPartner(ISpecies<?> species);

	/**
	 * @return true if the mutation should not be displayed in a gui that displays all mutations.
	 */
	boolean isSecret();

	ItemStack getMutationNote(GameProfile researcher);
}
