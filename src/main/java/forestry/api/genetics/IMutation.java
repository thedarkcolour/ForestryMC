package forestry.api.genetics;

import java.util.Collection;

import net.minecraft.network.chat.Component;

/**
 * Represents a mutation between two species (in any order) into a third species.
 * A mutation should never have both parents of the same species.
 */
public interface IMutation<S extends ISpecies<?>> {
	/**
	 * @return {@link ISpeciesType} this mutation is associated with.
	 */
	ISpeciesType<S> getType();

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
	 * @return Unmodified base chance for mutation to fire.
	 */
	int getBaseChance();

	/**
	 * @return Collection of localized, human-readable strings describing special mutation conditions, if any.
	 */
	Collection<Component> getSpecialConditions();

	/**
	 * @return {@code true} if the passed allele is one of the alleles participating in this mutation.
	 */
	boolean isPartner(ISpecies<?> allele);

	/**
	 * @return The other allele which was not passed as argument.
	 */
	ISpecies<?> getPartner(ISpecies<?> allele);

	/**
	 * @return true if the mutation should not be displayed in a gui that displays all mutations.
	 */
	boolean isSecret();
}
