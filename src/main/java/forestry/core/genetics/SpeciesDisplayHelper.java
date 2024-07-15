package forestry.core.genetics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.alleles.IAlleleSpecies;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;

import forestry.api.genetics.IForestrySpeciesType;
import forestry.api.genetics.ISpeciesDisplayHelper;


public class SpeciesDisplayHelper implements ISpeciesDisplayHelper {
	private final Table<ILifeStage, String, ItemStack> iconStacks = HashBasedTable.create();
	private final IForestrySpeciesType<IIndividual> root;

	public SpeciesDisplayHelper(IForestrySpeciesType<IIndividual> root) {
		this.root = root;
		ILifeStage type = root.getIconType();
		for (IIndividual individual : root.getIndividualTemplates()) {
			ItemStack itemStack = root.getTypes().createStack(individual, type);
			iconStacks.put(type, individual.getGenome().getPrimarySpecies().id().toString(), itemStack);
		}
	}

	@Override
	public ItemStack getDisplayStack(IAlleleSpecies species, ILifeStage type) {
		ItemStack stack = iconStacks.get(type, species.getId().toString());
		if (stack == null) {
			stack = root.getTypes().createStack(root.templateAsIndividual(root.getTemplates().getTemplate(species.getId().toString())), type);
			iconStacks.put(type, species.getId().toString(), stack);
		}
		return stack;
	}

	@Override
	public ItemStack getDisplayStack(IAlleleSpecies species) {
		return getDisplayStack(species, root.getIconType());
	}
}
