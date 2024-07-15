package genetics.individual;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.alleles.IAllele;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;

import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import genetics.api.individual.IKaryotype;

public class Karyotype implements IKaryotype {
	private final ResourceLocation uid;
	private final IChromosome[] chromosomeTypes;
	private final IChromosome speciesType;
	private final Function<IAlleleTemplateBuilder, IAlleleTemplate> defaultTemplateSupplier;
	private final BiFunction<IKaryotype, IAllele[], IAlleleTemplateBuilder> templateFactory;
	@Nullable
	private IAlleleTemplate defaultTemplate = null;
	@Nullable
	private IGenome defaultGenome = null;

	public Karyotype(ResourceLocation uid, List<IChromosome> chromosomeTypes, IChromosome speciesType, BiFunction<IKaryotype, IAllele[], IAlleleTemplateBuilder> templateFactory, Function<IAlleleTemplateBuilder, IAlleleTemplate> defaultTemplateSupplier) {
		this.uid = uid;
		this.speciesType = speciesType;
		this.chromosomeTypes = new IChromosome[chromosomeTypes.size()];
		this.templateFactory = templateFactory;
		for (IChromosome key : chromosomeTypes) {
			this.chromosomeTypes[key.ordinal()] = key;
		}
		this.defaultTemplateSupplier = defaultTemplateSupplier;
	}

	@Override
	public ResourceLocation getId() {
		return uid;
	}

	@Override
	public IChromosome[] getChromosomeTypes() {
		return chromosomeTypes;
	}

	@Override
	public boolean contains(IChromosome type) {
		return Arrays.asList(chromosomeTypes).contains(type);
	}

	@Override
	public IChromosome getSpeciesChromosome() {
		return speciesType;
	}

	@Override
	public IAlleleTemplate getDefaultTemplate() {
		if (defaultTemplate == null) {
			defaultTemplate = defaultTemplateSupplier.apply(createEmptyTemplate());
		}
		return defaultTemplate;
	}

	@Override
	public int size() {
		return chromosomeTypes.length;
	}

	@Override
	public IGenome getDefaultGenome() {
		if (defaultGenome == null) {
			defaultGenome = getDefaultTemplate().toGenome();
		}
		return defaultGenome;
	}

	@Override
	public IAlleleTemplateBuilder createTemplate() {
		return getDefaultTemplate().createBuilder();
	}

	@Override
	public IAlleleTemplateBuilder createTemplate(IAllele[] alleles) {
		return templateFactory.apply(this, alleles);
	}

	@Override
	public IAlleleTemplateBuilder createEmptyTemplate() {
		return templateFactory.apply(this, new IAllele[chromosomeTypes.length]);
	}

	@Override
	public ChromosomePair[] templateAsChromosomes(IAllele[] templateActive, @Nullable IAllele[] templateInactive) {
		ChromosomePair[] chromosomes = new ChromosomePair[chromosomeTypes.length];
		for (int i = 0; i < chromosomeTypes.length; i++) {
			if (templateInactive == null) {
				chromosomes[i] = ChromosomePair.create(templateActive[i], chromosomeTypes[i]);
			} else {
				chromosomes[i] = ChromosomePair.create(chromosomeTypes[i], templateActive[i], templateInactive[i]);
			}
		}

		return chromosomes;
	}

	@Override
	public IGenome templateAsGenome(IAllele[] templateActive, @Nullable IAllele[] templateInactive) {
		return new Genome(this, templateAsChromosomes(templateActive, templateInactive));
	}
}
