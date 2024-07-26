package forestry.api.plugin;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;

/**
 * Handles registration of genetic-related data. Accessed from {@link IForestryPlugin#registerGenetics(IGeneticRegistration)}.
 */
public interface IGeneticRegistration {
	/**
	 * Defines a new taxon under an existing parent. Taxa are displayed as classifications in the Forestry analyzer.
	 * When defining a new tree of taxa, it is better to use {@link #defineTaxon(String, String, Consumer)}
	 * instead of making repeated calls to this method.
	 *
	 * @param parent The name of the parent taxon.
	 * @param name   The name of the taxon. Must be unique.
	 * @see forestry.api.genetics.ForestryTaxa For builtin taxon names.
	 */
	void defineTaxon(String parent, String name);

	/**
	 * Defines a new taxon or retrieves an existing taxon, allows for adding subtaxa and species.
	 *
	 * @param parent The taxonomic rank.
	 * @param name   The name of the taxon. Must be unique for all taxa in the same rank.
	 * @param action A consumer that adds additional information to the taxon after it is defined.
	 */
	void defineTaxon(String parent, String name, Consumer<ITaxonBuilder> action);

	/**
	 * Register a new type of species. Can only be called from {@link IForestryPlugin#registerGenetics}.
	 *
	 * @param id          The ID of the species.
	 * @param typeFactory The function to use to create the species type, given the completed karyotype.
	 * @return A builder that can be used to define properties of the species.
	 */
	ISpeciesTypeBuilder registerSpeciesType(ResourceLocation id, ISpeciesTypeFactory typeFactory);

	/**
	 * Modify an existing species, for example, adding an extra chromosome to bees, or adding additional permitted alleles to chromosomes.
	 * Called after all species types are registered.
	 *
	 * @param id     The ID of the species to modify.
	 * @param action The modifications to be made.
	 */
	void modifySpeciesType(ResourceLocation id, Consumer<ISpeciesTypeBuilder> action);

	<A extends IAllele> IChromosomeBuilder<A> registerChromosome(IChromosome<A> chromosomeType);
}
