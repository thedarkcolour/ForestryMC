package forestry.api.genetics;

/**
 * The replacement for the old ISpeciesType class. Denotes different forms of the same species (ex. larvae -> princess -> queen)
 * @see forestry.api.apiculture.genetics.BeeLifeStage
 * @see forestry.api.arboriculture.genetics.TreeLifeStage
 * @see forestry.api.lepidopterology.genetics.ButterflyLifeStage
 */
public interface ILifeStage {
	String getName();
}
