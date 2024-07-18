package forestry.api.genetics;

import java.util.Collection;

import net.minecraft.network.chat.Component;

import forestry.api.apiculture.IBeeSpecies;
import forestry.api.genetics.alleles.IAllele;


/**
 */
public interface IMutation<S extends ISpecies<?>> {

	/**
	 * @return {@link ISpeciesType} this mutation is associated with.
	 */
	ISpeciesType<S> getType();

	/**
	 * @return first of the alleles implementing IAlleleSpecies required for this mutation.
	 */
	S getFirstParent();

	/**
	 * @return second of the alleles implementing IAlleleSpecies required for this mutation.
	 */
	S getSecondParent();

	/**
	 * @return the allele implementing IAlleleSpecies the resulted of this mutation.
	 */
	S getResultingSpecies();

	/**
	 * @return Array of {@link IAllele} representing the full default genome of the mutated side.
	 * <p>
	 * Make sure to return a proper array for the species class. Returning an allele of the wrong type will cause cast errors on runtime.
	 */
	IAllele[] getTemplate();

	/**
	 * @return Unmodified base chance for mutation to fire.
	 */
	int getBaseChance();

	/**
	 * @return Collection of localized, human-readable strings describing special mutation conditions, if any.
	 */
	Collection<Component> getSpecialConditions();

	/**
	 * @return true if the passed allele is one of the alleles participating in this mutation.
	 */
	boolean isPartner(IAllele allele);

	/**
	 * @return the other allele which was not passed as argument.
	 */
	IAllele getPartner(IAllele allele);

	/**
	 * @return true if the mutation should not be displayed in a gui that displays all mutations.
	 */
	boolean isSecret();
}
