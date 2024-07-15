package forestry.api.plugin;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ITaxonBuilder;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;

/**
 * Handles registration of genetic-related data. Accessed from {@link IForestryPlugin#registerGenetics(IGeneticRegistration)}.
 */
public interface IGeneticRegistration {
	/**
	 * Defines a new taxon under an existing parent. Taxa are displayed as classifications in the Forestry analyzer.
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

	<A extends IAllele> IChromosomeBuilder<A> registerChromosome(IChromosome<A> chromosomeType);

	/**
	 * Register a new type of species. Can only be called from {@link IForestryPlugin#registerGenetics}.
	 * @param id The ID of the species.
	 * @return A builder that can be used to define properties of the species.
	 */
	ISpeciesTypeBuilder registerSpeciesType(ResourceLocation id);

	/**
	 * Modify an existing species, for example, adding an extra chromosome to bees, or adding additional permitted alleles to chromosomes.
	 * @param id
	 * @param action
	 */
	void modifySpeciesType(ResourceLocation id, Consumer<ISpeciesTypeBuilder> action);
}
