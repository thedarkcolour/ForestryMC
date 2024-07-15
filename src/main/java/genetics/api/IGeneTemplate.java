package genetics.api;

import javax.annotation.Nullable;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpeciesType;

/**
 * The IGeneTemplate is a interface that can be implemented as a capability if a item should represent a allele at a
 * specific {@link IChromosome} at the {@link IGenome} of a individual that is described by a specific
 * {@link ISpeciesType}.
 * For example the templates of Gendustry and the Genetics Mod of Binnie Mods.
 * <p>
 * All returned values of this interface are only empty if the template is empty.
 * <p>
 * You can use {@link IGeneticFactory#createGeneTemplate()}  to create an
 * instance of this or you can use your own implementation.
 */
public interface IGeneTemplate {

	/**
	 * @return The allele that this template contains.
	 */
	@Nullable
	IAllele getAllele();

	/**
	 * @return The gene type at that the chromosome of the allele is positioned at the chromosome array.
	 */
	@Nullable
	IChromosome getType();

	/**
	 * @return The genetic definition that describes the definition to that the {@link IChromosome} belongs to.
	 */
	@Nullable
	ISpeciesType getRoot();

	/**
	 * Sets the information of this template.
	 */
	void setAllele(@Nullable IChromosome type, @Nullable IAllele allele);
}
