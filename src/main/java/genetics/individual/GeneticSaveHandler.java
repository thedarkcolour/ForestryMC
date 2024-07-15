package genetics.individual;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.Forestry;

import genetics.ApiInstance;
import genetics.api.GeneticHelper;
import genetics.api.IGeneticSaveHandler;

import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import genetics.api.alleles.IAlleleTemplate;

import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import genetics.api.individual.IIndividual;
import genetics.api.individual.IKaryotype;
import genetics.api.organism.IOrganismHandler;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.ITemplateContainer;

public enum GeneticSaveHandler implements IGeneticSaveHandler {
	INSTANCE;

	private static final String GENOME_TAG = "Genome";
	private static final String SLOT_TAG = "Slot";
	private static final String CHROMOSOMES_TAG = "Chromosomes";

	@Override
	public CompoundTag writeTag(ChromosomePair[] chromosomes, CompoundTag tagCompound) {
		ListTag tagList = new ListTag();
		for (int i = 0; i < chromosomes.length; i++) {
			if (chromosomes[i] != null) {
				CompoundTag chromosomeTag = new CompoundTag();
				chromosomeTag.putByte(SLOT_TAG, (byte) i);
				chromosomes[i].writeToNBT(chromosomeTag);
				tagList.add(chromosomeTag);
			}
		}
		tagCompound.put(CHROMOSOMES_TAG, tagList);
		return tagCompound;
	}

	@Nullable
	@Override
	public ChromosomePair[] readTag(IKaryotype karyotype, CompoundTag tagCompound) {
		IChromosome[] geneTypes = karyotype.getChromosomeTypes();
		if (!tagCompound.contains(CHROMOSOMES_TAG)) {
			return null;
		}
		ListTag chromosomesNBT = tagCompound.getList(CHROMOSOMES_TAG, Tag.TAG_COMPOUND);
		ChromosomePair[] chromosomes = new ChromosomePair[geneTypes.length];
		ResourceLocation primaryTemplateIdentifier = null;
		ResourceLocation secondaryTemplateIdentifier = null;

		for (int i = 0; i < chromosomesNBT.size(); i++) {
			CompoundTag chromosomeNBT = chromosomesNBT.getCompound(i);
			byte chromosomeOrdinal = chromosomeNBT.getByte(SLOT_TAG);

			if (chromosomeOrdinal >= 0 && chromosomeOrdinal < chromosomes.length) {
				IChromosome geneType = geneTypes[chromosomeOrdinal];
				ChromosomePair chromosome = ChromosomePair.create(primaryTemplateIdentifier, secondaryTemplateIdentifier, geneType, chromosomeNBT);
				chromosomes[chromosomeOrdinal] = chromosome;

				if (geneType.equals(karyotype.getSpeciesChromosome())) {
					primaryTemplateIdentifier = chromosome.active().id();
					secondaryTemplateIdentifier = chromosome.inactive().id();
				}
			}
		}

		return chromosomes;
	}

	@Override
	@Nullable
	public IAllele getAlleleDirectly(CompoundTag genomeNBT, IChromosome chromosomeType, boolean active) {
		ListTag tagList = genomeNBT.getList(CHROMOSOMES_TAG, Tag.TAG_COMPOUND);
		if (tagList.isEmpty()) {
			return null;
		}
		CompoundTag chromosomeTag = tagList.getCompound(chromosomeType.ordinal());
		if (chromosomeTag.isEmpty()) {
			return null;
		}
		return (active ? ChromosomePair.getActiveAllele(chromosomeTag) : ChromosomePair.getInactiveAllele(chromosomeTag));
	}

	/**
	 * Quickly gets the species without loading the whole genome. And without creating absent chromosomes.
	 */
	@Override
	@Nullable
	public IAllele getAlleleDirectly(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType, boolean active) {
		CompoundTag nbtTagCompound = itemStack.getTag();
		if (nbtTagCompound == null || nbtTagCompound.isEmpty()) {
			return null;
		}

		CompoundTag individualNBT = getIndividualDataDirectly(itemStack, type, chromosomeType.getSpecies());
		if (individualNBT == null || individualNBT.isEmpty()) {
			return null;
		}

		CompoundTag genomeNBT = individualNBT.getCompound(GENOME_TAG);
		if (genomeNBT.isEmpty()) {
			return null;
		}
		IAllele allele = getAlleleDirectly(genomeNBT, chromosomeType, active);
		IAlleleRegistry alleleRegistry = ApiInstance.INSTANCE.getAlleleRegistry();
		if (allele == null || !alleleRegistry.isValidAllele(allele, chromosomeType)) {
			return null;
		}
		return allele;
	}

	// NBT RETRIEVAL

	@Override
	public IAllele getAllele(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType, boolean active) {
		ChromosomePair chromosome = getSpecificChromosome(itemStack, type, chromosomeType);
		return active ? chromosome.active() : chromosome.inactive();
	}

	@Override
	public ChromosomePair getSpecificChromosome(CompoundTag genomeNBT, IChromosome chromosomeType) {
		ChromosomePair[] chromosomes = readTag(chromosomeType.getSpecies().getKaryotype(), genomeNBT);
		if (chromosomes == null) {
			throw new IllegalStateException("Failed to read specific chromosome from NBT");
		}
		return chromosomes[chromosomeType.ordinal()];
	}

	@Override
	public ChromosomePair getSpecificChromosome(ItemStack itemStack, ILifeStage type, IChromosome chromosomeType) {
		itemStack.getOrCreateTag();

		CompoundTag individualNBT = getIndividualData(itemStack, type, chromosomeType.getSpecies());
		CompoundTag genomeNBT = individualNBT.getCompound(GENOME_TAG);

		return getSpecificChromosome(genomeNBT, chromosomeType);
	}

	@Nullable
	@Override
	public CompoundTag getIndividualDataDirectly(ItemStack itemStack, ILifeStage type, ISpeciesType<IIndividual> root) {
		IOrganismHandler<IIndividual> organismHandler = GeneticHelper.getOrganismHandler(root, type);
		return organismHandler.getIndividualData(itemStack);
	}

	@Override
	public CompoundTag getIndividualData(ItemStack itemStack, ILifeStage type, ISpeciesType<IIndividual> root) {
		IOrganismHandler<IIndividual> organismHandler = GeneticHelper.getOrganismHandler(root, type);
		CompoundTag compound = organismHandler.getIndividualData(itemStack);
		if (compound != null) {
			return compound;
		}
		compound = new CompoundTag();
		CompoundTag genomeNBT = compound.getCompound(GENOME_TAG);

		if (genomeNBT.isEmpty()) {
			Forestry.LOGGER.error("Got a genetic item with no genome, setting it to a default value.", new Object[]{});
			genomeNBT = new CompoundTag();

			ITemplateContainer<IIndividual> container = root.getTemplates();
			IAlleleTemplate defaultTemplate = container.getKaryotype().getDefaultTemplate();
			IGenome genome = defaultTemplate.toGenome(null);
			genome.writeToNBT(genomeNBT);
			compound.put(GENOME_TAG, genomeNBT);
		}
		organismHandler.setIndividualData(itemStack, compound);
		return compound;
	}
}
