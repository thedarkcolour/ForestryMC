package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.INamedValue;

import genetics.api.individual.IGenomeWrapper;
import genetics.api.individual.IIndividual;
import genetics.api.individual.IKaryotype;
import genetics.api.root.IDisplayHelper;
import genetics.api.root.IIndividualRootBuilder;
import genetics.api.root.ITemplateContainer;
import genetics.api.root.translator.IIndividualTranslator;

public interface ISpeciesType<I extends IIndividual> extends INamedValue {
	/**
	 * Uses the information that the NBT-Data contains to create a {@link IIndividual}.
	 */
	I create(CompoundTag compound);

	/**
	 * Creates a {@link IIndividual} that contains this genome.
	 */
	I create(IGenome genome);

	@Nullable
	default I create(ItemStack stack) {
		return getTypes().createIndividual(stack);
	}

	default ItemStack createStack(I individual, ILifeStage type) {
		return getTypes().createStack(individual, type);
	}

	@Nullable
	default ILifeStage getLifeStage(ItemStack stack) {
		return getTypes().getType(stack);
	}

	default IAllele[] getTemplate(String identifier) {
		return getTemplates().getTemplate(identifier);
	}

	Class<? extends I> getMemberClass();

	/**
	 * Creates a optional that describes an {@link IIndividual} that contains the {@link IAllele} template that is
	 * associated with the given identifier.
	 *
	 * @param templateIdentifier A identifier that is associate with a {@link IAllele} template at the
	 *                           {@link ITemplateContainer} of this root.
	 */
	@Nullable
	I create(String templateIdentifier);

	/**
	 * Creates a {@link IIndividual} that contains the alleles of the template in a genome.
	 *
	 * @param template The alleles of the genome.
	 */
	default I templateAsIndividual(IAllele[] template) {
		return templateAsIndividual(template, null);
	}

	/**
	 * Creates a {@link IIndividual} that contains the alleles of the two templates in a genome.
	 *
	 * @param templateActive   The active alleles of the genome.
	 * @param templateInactive The inactive alleles of the genome.
	 */
	default I templateAsIndividual(IAllele[] templateActive, @Nullable IAllele[] templateInactive) {
		IGenome genome = templateAsGenome(templateActive, templateInactive);
		return create(genome);
	}

	IGenome templateAsGenome(IAllele[] templateActive, IAllele[] templateInactive);

	default IGenome templateAsGenome(IAllele[] template) {
		return templateAsGenome(template, template);
	}

	/* Item Stacks */

	/**
	 * Creates an {@link ItemStack} that uses the {@link IAllele} template of the given allele and has the
	 * given organism type.
	 *
	 * @param allele The template identifier
	 * @param type   The type whose {@link IOrganismHandler} will be used to create the stack with
	 *               {@link IOrganismHandler#createStack(IIndividual)}.
	 * @return A stack with the given {@link ILifeStage} and the allele template of the given allele.
	 */
	ItemStack createStack(IAllele allele, ILifeStage type);

	boolean isMember(ItemStack stack);

	/* Genome */

	/**
	 * Creates a wrapper that can be used to give access to the values of the alleles that the genome contains.
	 */
	IGenomeWrapper createWrapper(IGenome genome);

	/* Individuals */

	List<I> getIndividualTemplates();
	/* Components */

	/**
	 * Returns the template container that contains all registered templates for the individual of this root.
	 * Templates have to be registered at the {@link IIndividualRootBuilder} of the root before the root itself was
	 * built.
	 *
	 * @return The template container of this root.
	 */
	ITemplateContainer<I> getTemplates();

	/**
	 * The Karyotype defines how many {@link IChromosome}s the {@link IGenome} of an
	 * {@link IIndividual} has.
	 *
	 * @return The karyotype of this root.
	 */
	IKaryotype getKaryotype();

	/**
	 * A translator that can be used to translate {@link BlockState} and
	 * {@link ItemStack} without any genetic information  into {@link IIndividual}s or into a {@link ItemStack} that
	 * contains a {@link IIndividualCapability}.
	 *
	 * @return A translator that can be used to translate {@link BlockState} and
	 * {@link ItemStack} into {@link IIndividual}s.
	 */
	IIndividualTranslator<I> getTranslator();

	/**
	 * Translates {@link BlockState}s into genetic data.
	 */
	@Nullable
	default I translateMember(BlockState objectToTranslate) {
		return getTranslator().translateMember(objectToTranslate);
	}

	/**
	 * Translates {@link ItemStack}s into genetic data.
	 */
	@Nullable
	default I translateMember(ItemStack objectToTranslate) {
		return getTranslator().translateMember(objectToTranslate);
	}

	<T extends ISpeciesType<?>> T cast();

	String getTranslationKey();

	default MutableComponent getDisplayName() {
		return Component.translatable(getTranslationKey());
	}
}
