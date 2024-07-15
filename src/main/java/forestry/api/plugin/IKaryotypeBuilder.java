package forestry.api.plugin;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IBooleanAllele;
import forestry.api.genetics.alleles.IBooleanChromosome;
import forestry.api.genetics.alleles.IFloatAllele;
import forestry.api.genetics.alleles.IFloatChromosome;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IIntegerChromosome;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.IValueChromosome;

/**
 * Used to configure the default alleles of a species.
 */
public interface IKaryotypeBuilder {
	default void set(IBooleanChromosome chromosome, boolean defaultAllele) {
		set(chromosome, defaultAllele ? ForestryAlleles.TRUE : ForestryAlleles.FALSE);
	}

	void set(IBooleanChromosome chromosome, IBooleanAllele defaultAllele);

	IChromosomeBuilder<IFloatAllele> set(IFloatChromosome chromosome, IFloatAllele defaultAllele);

	IChromosomeBuilder<IIntegerAllele> set(IIntegerChromosome chromosome, IIntegerAllele defaultAllele);

	<V> IChromosomeBuilder<IValueAllele<V>> set(IValueChromosome<V> chromosome, IValueAllele<V> defaultAllele);

	<E extends Enum<E>> void set(IValueChromosome<E> chromosome, E defaultAllele);

	/**
	 * Sets the species chromosome of the karyotype for this species type.
	 *
	 * @param species   The species chromosome.
	 * @param defaultId The ID of the default species, used as a fallback when a genome is not available or corrupt.
	 */
	void setSpecies(ISpeciesChromosome<? extends ISpeciesType<?>> species, ResourceLocation defaultId);
}
