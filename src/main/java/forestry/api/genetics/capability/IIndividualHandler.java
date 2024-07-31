package forestry.api.genetics.capability;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

/**
 * The individual handler manages an item's genetic information.
 * It contains the {@link IIndividual} and {@link ILifeStage} of the item.
 * This class can be thought of as the {@link IIndividual} analog of IFluidHandler.
 * In 1.21, this will be replaced by Components.
 */
public interface IIndividualHandler {
	/**
	 * @return The species type of this individual. Used for serialization/deserialization purposes, among other things.
	 */
	ISpeciesType<?, ?> getSpeciesType();
	/**
	 * @return The life stage of this individual
	 */
	ILifeStage getStage();

	/**
	 * @return The individual contained in this handler
	 */
	IIndividual getIndividual();

	/**
	 * @return {@code true} if this individual is the genetic form. Returns false for things like Vanilla saplings.
	 */
	boolean isGeneticForm();
}
