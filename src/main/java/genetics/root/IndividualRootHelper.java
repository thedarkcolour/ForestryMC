package genetics.root;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

import genetics.api.GeneticHelper;
import genetics.api.GeneticsAPI;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.individual.IIndividual;
import genetics.api.organism.IIndividualCapability;
import genetics.api.root.EmptyRootDefinition;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IIndividualRootHelper;
import genetics.api.root.IRootDefinition;

public enum IndividualRootHelper implements IIndividualRootHelper {
	INSTANCE;

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <R extends ISpeciesType<?>> R getSpeciesRoot(ItemStack stack) {
		return (R) getSpeciesRoot(stack, ISpeciesType.class);
	}

	@Nullable
	@Override
	public <R extends ISpeciesType<?>> R getSpeciesRoot(ItemStack stack, Class<? extends R> rootClass) {
		if (stack.isEmpty()) {
			return null;
		}

		Map<String, IRootDefinition> definitions = GeneticsAPI.apiInstance.getRoots();
		for (IRootDefinition definition : definitions.values()) {
			if (!definition.isPresent()) {
				continue;
			}
			ISpeciesType root = definition.get();
			if (!root.isMember(stack) || !rootClass.isInstance(root)) {
				continue;
			}
			return (IRootDefinition<R>) definition;
		}
		return EmptyRootDefinition.empty();
	}

	@Override
	public R getSpeciesRoot(Class<I> individualClass) {
		return getSpeciesRoot(individualClass, ISpeciesType.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends ISpeciesType> IRootDefinition<R> getSpeciesRoot(Class<? extends IIndividual> individualClass, Class<? extends R> rootClass) {
		Map<String, IRootDefinition> definitions = GeneticsAPI.apiInstance.getRoots();
		for (IRootDefinition rootDefinition : definitions.values()) {
			if (!rootDefinition.isPresent()) {
				continue;
			}
			ISpeciesType<?> root = rootDefinition.get();
			if (!root.getMemberClass().isAssignableFrom(individualClass) || rootClass.isInstance(root)) {
				continue;
			}
			return (IRootDefinition<R>) rootDefinition;
		}
		return EmptyRootDefinition.empty();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends ISpeciesType<?>> R getSpeciesRoot(IIndividual individual) {
		return (R) individual.getRoot();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends ISpeciesType> IRootDefinition<R> getSpeciesRoot(IIndividual individual, Class<? extends R> rootClass) {
		ISpeciesType root = individual.getRoot();
		return rootClass.isInstance(root) ? (IRootDefinition<R>) root.getDefinition() : EmptyRootDefinition.empty();
	}

	@Override
	public boolean isIndividual(ItemStack stack) {
		return getSpeciesRoot(stack).isPresent();
	}

	@Nullable
	@Override
	public IIndividual getIndividual(ItemStack stack) {
		IIndividualCapability<IIndividual> organism = GeneticHelper.getOrganism(stack);
		return organism.getIndividual();
	}

	@Override
	public IAlleleTemplateBuilder createTemplate(String uid) {
		GeneticsAPI.apiInstance.getRoot(uid);
		// todo why is this returning null? also this method seems entirely unused
		return null;
	}
}
