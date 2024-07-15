package genetics.api.individual;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IValueChromosome;

import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.root.ITemplateContainer;

/**
 * The IKaryotype defines how many chromosomes a {@link IGenome} contains and which type the {@link ChromosomePair}s have.
 * <p>
 * You can use a {@link IKaryotypeBuilder} to create an instance or you create the instance directly with
 * {@link IKaryotypeFactory#createKaryotype(String, Class)} if you have a enum that contains your {@link IChromosome}s.
 */
public interface IKaryotype extends Iterable<IChromosome> {
	/**
	 * @return Short identifier that is only used if something went wrong.
	 */
	ResourceLocation getId();

	/**
	 * @return All gene types of this IKaryotype.
	 */
	IChromosome[] getChromosomeTypes();

	/**
	 * Checks if this karyotype contains the given type.
	 */
	boolean contains(IChromosome type);

	/**
	 * @return The {@link IChromosome} that is used by the {@link ITemplateContainer} to identify the different templates.
	 * It uses the {@link IAllele#id()} of the allele that is at the active position of the template in the
	 * chromosome with this type.
	 */
	IValueChromosome<? extends ISpeciesType<?>> getSpeciesChromosome();

	/**
	 * Creates a template builder that contains a copy of the default template allele array.
	 */
	IAlleleTemplateBuilder createTemplate();

	/**
	 * Creates a template builder that contains a copy of the allele array.
	 */
	IAlleleTemplateBuilder createTemplate(IAllele[] alleles);

	IAlleleTemplateBuilder createEmptyTemplate();

	/**
	 * @return Default individual template for use when stuff breaks.
	 */
	IAlleleTemplate getDefaultTemplate();

	int size();

	/*
	 * @return The default template as a IGenome.
	 */
	IGenome getDefaultGenome();

	default ChromosomePair[] templateAsChromosomes(IAllele[] template) {
		return templateAsChromosomes(template, null);
	}

	ChromosomePair[] templateAsChromosomes(IAllele[] templateActive, @Nullable IAllele[] templateInactive);

	default IGenome templateAsGenome(IAllele[] template) {
		return templateAsGenome(template, null);
	}

	IGenome templateAsGenome(IAllele[] templateActive, @Nullable IAllele[] templateInactive);

	@Override
	default Iterator<IChromosome> iterator() {
		return Arrays.stream(getChromosomeTypes()).iterator();
	}
}
