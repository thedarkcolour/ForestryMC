package genetics.api.individual;

import forestry.api.genetics.IGenome;

import forestry.api.genetics.alleles.IAlleleSpecies;
import genetics.api.root.IIndividualRootBuilder;

/**
 * Help interface that can be used to define genetic species.
 * It provides method to get the an instance of the default template, the default genome or default individual
 * of the defined species.
 * It also can be used to register mutations or other species related data to the {@link IRootComponentBuilder}s of the
 * {@link IIndividualRootBuilder} of the root that the species belongs to.
 */
public interface ISpeciesDefinition<I extends IIndividual> extends ITemplateProvider {

	/**
	 * @return An instance of the genome that contains the default template of this species.
	 */
	IGenome getGenome();

	/**
	 * @return Creates a instance of the {@link IIndividual} that contains the {@link #getGenome()}.
	 */
	I createIndividual();
}
