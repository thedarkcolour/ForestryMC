package genetics.api;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.IGenome;

import forestry.api.genetics.alleles.IAllele;
import genetics.api.individual.IIndividual;
import genetics.api.organism.IIndividualCapability;
import genetics.api.organism.IOrganismHandler;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

/**
 * A helper class that contains some help methods.
 */
public class GeneticHelper {
	public static boolean isValidTemplate(@Nullable IAllele[] template, ISpeciesType<?> root) {
		return template != null && template.length >= root.getTemplates().getKaryotype().size();
	}

	@Nullable
	public static IGenome genomeFromTemplate(String templateName, ISpeciesType<?> root) {
		IAllele[] template = root.getTemplates().getTemplate(templateName);
		if (GeneticHelper.isValidTemplate(template, root)) {
			return root.getKaryotype().templateAsGenome(template);
		}
		return null;
	}

	public static <I extends IIndividual> I createOrganism(ItemStack itemStack, ILifeStage type, ISpeciesType<I> root) {
		IGeneticFactory geneticFactory = GeneticsAPI.apiInstance.getGeneticFactory();
		return geneticFactory.createOrganism(itemStack, type, root);
	}

	public static <I extends IIndividual> IIndividualCapability<I> getOrganism(ItemStack itemStack) {
		return itemStack.getCapability(ForestryCapabilities.INDIVIDUAL).orElse(EMPTY);
	}

	public static <I extends IIndividual> boolean setIndividual(ItemStack itemStack, I individual) {
		IIndividualCapability<I> organism = getOrganism(itemStack);
		return organism.setIndividual(individual);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <I extends IIndividual> I getIndividual(ItemStack itemStack) {
		return (I) itemStack.getCapability(ForestryCapabilities.INDIVIDUAL).orElse(null);
	}

	public static IOrganismHandler<IIndividual> getOrganismHandler(ISpeciesType<IIndividual> root, ILifeStage type) {
		IOrganismHandler<IIndividual> handler = root.getTypes().getHandler(type);
		if (handler == null) {
			throw new IllegalArgumentException(String.format("No organism handler was registered for the organism type '%s'", type.getName()));
		}
		return handler;
	}
}
