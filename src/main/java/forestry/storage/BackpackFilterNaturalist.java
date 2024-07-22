package forestry.storage;

import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;

public class BackpackFilterNaturalist implements Predicate<ItemStack> {
	private final ResourceLocation speciesRootUid;

	public BackpackFilterNaturalist(ResourceLocation speciesType) {
		this.speciesRootUid = speciesType;
	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL)
				.map(individual -> this.speciesRootUid.equals(individual.getType().id()))
				.orElse(false);
	}

}
