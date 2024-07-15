package forestry.storage;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IRootDefinition;

public class BackpackFilterNaturalist implements Predicate<ItemStack> {
	private final String speciesRootUid;

	public BackpackFilterNaturalist(String speciesRootUid) {
		this.speciesRootUid = speciesRootUid;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		IRootDefinition<ISpeciesType<IIndividual>> definition = GeneticsAPI.apiInstance.getRoot(speciesRootUid);
		return definition.test(root -> root.isMember(itemStack));
	}

}
