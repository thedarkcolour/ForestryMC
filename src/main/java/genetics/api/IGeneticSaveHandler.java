package genetics.api;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import genetics.api.individual.IIndividual;
import genetics.api.individual.IKaryotype;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

/**
 * This handler provides some functions to save and load {@link IAllele}s, {@link ChromosomePair}s and {@link IGenome}s.
 * <p>
 * Get the instance from {@link IGeneticApiInstance#getSaveHandler()}.
 */
public interface IGeneticSaveHandler {

	/**
	 * Writes the given chromosomes to a NBT-Data.
	 *
	 * @param chromosomes The chromosomes that you want to write to the NBT-Data.
	 * @param tagCompound The NBT-Data to that the data of the chromosomes should be written.
	 */
	CompoundTag writeTag(ChromosomePair[] chromosomes, CompoundTag tagCompound);

	/**
	 * Loads the chromosomes form the NBt-Data.
	 *
	 * @param karyotype   The karyotype of the chromosomes that the NBT-Data contains.
	 * @param tagCompound The NBT-Data that contain the information of the chromosomes
	 * @return The saved chromosomes, or {@code null} if there was an error reading from NBT
	 */
	@Nullable
	ChromosomePair[] readTag(IKaryotype karyotype, CompoundTag tagCompound);

	/**
	 * Quickly gets the species without loading the whole genome. And without creating absent chromosomes.
	 *
	 * @param genomeNBT      The NBT-Data that contains the information about the chromosome
	 * @param chromosomeType The gene type of the chromosome.
	 * @param active         if the returned allele should be the active one.
	 * @return The active or inactive allele of the chromosome if present, {@code null} otherwise.
	 */
	@Nullable
	IAllele getAlleleDirectly(CompoundTag genomeNBT, IChromosome chromosomeType, boolean active);

	/**
	 * Quickly gets the allele without loading the whole genome. And without creating absent chromosomes.
	 *
	 * @param itemStack      The stack that contains the information about the chromosome
	 * @param type			 Type of organism this stack is.
	 * @param chromosomeType The gene type of the chromosome.   @return The active or inactive allele of the chromosome if the chromosome is present.
	 * @param active         if the returned allele should be the active one.
	 */
	@Nullable
	IAllele getAlleleDirectly(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType, boolean active);

	/**
	 * Tries to load the chromosome of the given type and creates it if it is absent.
	 *
	 * @param itemStack      The stack that contains the information about the chromosome
	 * @param type			 Type of organism this stack is.
	 * @param chromosomeType The gene type of the chromosome.
	 * @param active         if the returned allele should be the active one.   @return The active or inactive allele of the chromosome.
	 */
	IAllele getAllele(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType, boolean active);

	/**
	 * Tries to load a specific chromosome and creates it if it is absent.
	 *
	 * @param genomeNBT      The NBT-Data that contains the information about the chromosome
	 * @param chromosomeType The gene type of the chromosome.
	 * @return The chromosome.
	 * @throws IllegalStateException If the chromosome was not found in NBT.
	 */
	ChromosomePair getSpecificChromosome(CompoundTag genomeNBT, IChromosome chromosomeType);

	/**
	 * Tries to load a specific chromosome and creates it if it is absent.
	 *
	 * @param itemStack      The stack that contains the information about the chromosome
	 * @param type           Type of organism this stack is.
	 * @param chromosomeType The gene type of the chromosome.
	 * @return The chromosome.
	 * @throws IllegalStateException If the chromosome was not found in NBT.
	 */
	ChromosomePair getSpecificChromosome(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType);

	@Nullable
	CompoundTag getIndividualDataDirectly(ItemStack itemStack, ILifeStage type, ISpeciesType<IIndividual> root);

	CompoundTag getIndividualData(ItemStack itemStack, ILifeStage type, ISpeciesType<IIndividual> root);

}
